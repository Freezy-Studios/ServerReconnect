# ServerReconnect Plugin

ServerReconnect is a plugin for Velocity proxy servers that automatically reconnects players to a server if they are kicked due to the server being closed. It provides a smooth user experience by displaying a reconnect animation while attempting to reestablish the connection.

---

## Features
- Automatically reconnects players to the last server if kicked for "Server closed."
- Displays a reconnect animation during the reconnection process.
- Fully configurable via source code adjustments.
- Lightweight and efficient.

---

## Prerequisites
- **Java Version**: Java 21
- **Build System**: Gradle
- **Supported Operating Systems**: macOS, Linux, Windows
- **Proxy Server**: Velocity 3.4.0

---

## Download and Installation
### Prebuilt Releases
1. Visit the [Releases](https://github.com/Freezy-Studios/ServerReconnect/releases) page.
2. Download the latest version of the `ServerReconnect` plugin jar.
3. Place the jar file in the `plugins` folder of your Velocity proxy server.

### Building from Source
If you prefer to build the plugin from the source code, follow the steps below:

#### 1. Clone the Repository
```bash
# Clone the repository
$ git clone https://github.com/Freezy-Studios/ServerReconnect.git
$ cd ServerReconnect
```

#### 2. Build the Plugin
##### macOS and Linux
```bash
# Run Gradle wrapper to build the project
$ ./gradlew build

# The compiled jar file will be in the 'build/libs/' directory
```

##### Windows
```cmd
# Run Gradle wrapper to build the project
> gradlew build

# The compiled jar file will be in the 'build/libs/' directory
```

---

## Usage
1. **Install the Plugin**:
    - Place the `ServerReconnect` jar file in the `plugins` folder of your Velocity server.
2. **Start the Server**:
    - Run your Velocity server to initialize the plugin.
3. **Logs**:
    - Check the logs for `ServerReconnect has been enabled!` to ensure the plugin is running.

---

## Contributing
Contributions are welcome! If you'd like to contribute, please fork the repository and create a pull request with your changes.

---

## Support
For issues or feature requests, please open an issue on the [GitHub repository](https://github.com/Freezy-Studios/ServerReconnect/issues).

---

## License
This project is licensed under the [MIT License](https://github.com/Freezy-Studios/ServerReconnect/blob/master/LICENSE).

---

### Contact
- **Author**: DaTTV, Freezy
- **Website**: [https://freezy.me](https://freezy.me)

