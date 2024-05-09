package com.cubowbot.cubow.handler.discord;

import com.cubowbot.cubow.CubowApplication;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class ConfigHandler {
    private static final Logger logger = LoggerFactory.getLogger(ConfigHandler.class);

    public String loadToken() throws IOException {
        String result = null;
        try {

            String fileName = "config.properties";

            File file = new File(fileName);
            if (!file.exists()) {
                throw new FileNotFoundException(fileName + " wurde im aktuellen Verzeichnis nicht gefunden");
            } else {
                // Lese den Inhalt der config.properties-Datei
                Properties properties = new Properties();
                FileInputStream fileInputStream = new FileInputStream(file);
                properties.load(fileInputStream);
                fileInputStream.close();

                // Holen des Werts für das Schlüsselwort
                result = properties.getProperty("token");
            }

            if (result == null) {
                throw new IllegalArgumentException("Token wurde nicht in " + fileName + " gefunden");
            } else {
                return result;
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public String loadDatabaseLink() throws IOException {
        String result = null;
        try {

            String fileName = "config.properties";

            File file = new File(fileName);
            if (!file.exists()) {
                throw new FileNotFoundException(fileName + " wurde im aktuellen Verzeichnis nicht gefunden");
            } else {
                // Lese den Inhalt der config.properties-Datei
                Properties properties = new Properties();
                FileInputStream fileInputStream = new FileInputStream(file);
                properties.load(fileInputStream);
                fileInputStream.close();

                // Holen des Werts für das Schlüsselwort
                result = properties.getProperty("mongoURI");
            }

            if (result == null) {
                throw new IllegalArgumentException("Token wurde nicht in " + fileName + " gefunden");
            } else {
                return result;
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static String getServerConfig(String serverID, String configName) {
        ObjectMapper objectMapper = new ObjectMapper();

        String config = null;
        try {
            // Read JSON file
            JsonNode rootNode = objectMapper.readTree(new File("server_configs.json"));

            // Access server configurations
            JsonNode serverConfigs = rootNode.get("server_configs");

            // Iterate over server configurations
            for (JsonNode serverIdNode : serverConfigs) {
                JsonNode serverConfig = serverConfigs.get(serverID);

                if (serverConfig != null) {
                    JsonNode configValue = serverConfig.get(configName);

                    if (configValue != null) {
                        config = configValue.asText();
                        break;
                    } else {
                       // test
                    }
                } else {
                    // Create new Config Part
                    ObjectNode newServerConfig = objectMapper.createObjectNode();
                    CubowApplication bot = new CubowApplication();
                    newServerConfig.put("name", bot.getJDA().getGuildById(serverID).getName());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return config;
    }

    public void checkConfigs() {
        // Define the list of required files
        String[] requiredFiles = {"config.properties", "colors.json"};

        // Loop through each required file
        for (String fileName : requiredFiles) {
            File file = new File(fileName);
            if (!file.exists()) {
                try (InputStream inputStream = CubowApplication.class.getResourceAsStream("/" + fileName);
                     FileOutputStream outputStream = new FileOutputStream(fileName)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    logger.info("Copied " + fileName + " from resources.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
