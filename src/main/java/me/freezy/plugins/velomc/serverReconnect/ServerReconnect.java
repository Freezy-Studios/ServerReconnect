package me.freezy.plugins.velomc.serverReconnect;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "serverreconnect", name = "ServerReconnect", version = BuildConstants.VERSION, url = "https://freezy.me", authors = {"DaTTV", "Freezy"})
public class ServerReconnect {
    @Getter
    private static Logger logger;
    @Getter
    private static ProxyServer server;
    @Getter
    private static Path dataDirectory;
    @Getter
    private static ServerReconnect instance;

    @Inject
    public ServerReconnect(Logger logger, ProxyServer server, @DataDirectory Path dataDirectory) {
        ServerReconnect.logger = logger;
        ServerReconnect.server = server;
        ServerReconnect.dataDirectory = dataDirectory;
        ServerReconnect.instance = this;

        logger.info("ServerReconnect has been enabled!");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new Events());
    }
}
