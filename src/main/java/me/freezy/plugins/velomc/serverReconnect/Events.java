package me.freezy.plugins.velomc.serverReconnect;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.scheduler.ScheduledTask;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Event-Handler-Klasse für automatische Server-Reconnects.
 */
public class Events {

    // Maps zur Verwaltung geplanter Tasks und Servernamen pro Spieler
    @Getter
    private static final Map<UUID, ScheduledTask> reconnectTasks = new HashMap<>();
    @Getter
    private static final Map<UUID, ScheduledTask> animationTasks = new HashMap<>();
    @Getter
    private static final Map<UUID, String> playerServers = new HashMap<>();

    // Logger-Instanz
    private final Logger logger = ServerReconnect.getLogger();

    /**
     * Event-Handler: Wird ausgelöst, wenn ein Spieler von einem Server gekickt wird.
     *
     * @param event Das KickedFromServerEvent
     */
    @Subscribe
    public void onPlayerLeaveServer(KickedFromServerEvent event) {
        Player player = event.getPlayer();

        // Prüfen, ob der Kick-Grund "Server Closed" ist
        event.getServerKickReason().ifPresent(reason -> {
            String plainReason = PlainTextComponentSerializer.plainText().serialize(reason);
            if ("Server closed".equals(plainReason)) {
                UUID playerId = player.getUniqueId();
                String serverName = event.getServer().getServerInfo().getName();

                // Set the server name for the player
                playerServers.put(playerId, serverName);

                // Schedule a reconnect task
                ScheduledTask reconnectTask = ServerReconnect.getServer().getScheduler()
                        .buildTask(ServerReconnect.getInstance(), () -> {
                            logger.info("Attempting to reconnect player {} to server {}", player.getUsername(), serverName);
                            player.createConnectionRequest(event.getServer()).connect();
                        })
                        .repeat(2L, TimeUnit.SECONDS)
                        .schedule();

                // Schedule an animation task
                ScheduledTask animationTask = ServerReconnect.getServer().getScheduler()
                        .buildTask(ServerReconnect.getInstance(), () -> displayReconnectAnimation(player))
                        .repeat(750L, TimeUnit.MILLISECONDS)
                        .schedule();

                reconnectTasks.put(playerId, reconnectTask);
                animationTasks.put(playerId, animationTask);
            }
        });
    }

    /**
     * Zeigt eine animierte Nachricht für den Spieler an.
     *
     * @param player Der Spieler
     */
    private void displayReconnectAnimation(Player player) {
        UUID playerId = player.getUniqueId();
        String[] animationFrames = {
                "<color:#caff45>Reconnecting</color><color:#ff2a00>.  </color>",
                "<color:#caff45>Reconnecting</color><color:#ffee00>.. </color>",
                "<color:#caff45>Reconnecting</color><color:#26ff00>...</color>"
        };

        int frameIndex = (int) (System.currentTimeMillis() / 750L % animationFrames.length);
        String serverName = playerServers.get(playerId);

        if (serverName != null) {
            Title title = Title.title(
                    MiniMessage.miniMessage().deserialize(animationFrames[frameIndex]),
                    MiniMessage.miniMessage().deserialize("<color:#b4e33d>Server: </color><color:#4affcf>" + serverName + "</color>"),
                    Title.Times.times(Duration.ZERO, Duration.ofMillis(760L), Duration.ZERO)
            );
            player.showTitle(title);
        }
    }

    /**
     * Event-Handler: Wird ausgelöst, wenn ein Spieler erfolgreich eine Verbindung zu einem Server herstellt.
     *
     * @param event Das ServerConnectedEvent
     */
    @Subscribe
    public void onPlayerReconnectServer(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        String connectedServer = event.getServer().getServerInfo().getName();

        // Prüfen, ob der Spieler erfolgreich verbunden wurde
        if (playerServers.containsKey(playerId) && playerServers.get(playerId).equals(connectedServer)) {
            // Beenden der geplanten Tasks
            cancelPlayerTasks(playerId);
        }
    }

    /**
     * Beendet alle Tasks für einen Spieler.
     *
     * @param playerId Die UUID des Spielers
     */
    private void cancelPlayerTasks(UUID playerId) {
        Optional.ofNullable(reconnectTasks.remove(playerId)).ifPresent(ScheduledTask::cancel);
        Optional.ofNullable(animationTasks.remove(playerId)).ifPresent(ScheduledTask::cancel);
        playerServers.remove(playerId);
    }
}
