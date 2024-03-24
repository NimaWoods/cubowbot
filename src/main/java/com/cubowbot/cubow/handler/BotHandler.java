package com.cubowbot.cubow.handler;

import com.cubowbot.cubow.CubowApplication;
import com.cubowbot.cubow.listener.ContextMenuListener;
import com.cubowbot.cubow.listener.EventListener;
import com.cubowbot.cubow.listener.ModalListener;
import com.cubowbot.cubow.listener.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumSet;

public class BotHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(BotHandler.class);

    ConfigHandler configHandler = new ConfigHandler();

    public JDABuilder build() {
        logger.info("Building JDA...");

        logger.info("Retrieving Token...");
        String token = null;

        configHandler.checkConfigs();
        try {
            token = configHandler.loadToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JDABuilder builder = JDABuilder.createDefault(token);

        // Load Handler
        EventListener eventListener = new EventListener();
        ButtonHandler buttonHandler = new ButtonHandler();
        SlashCommandListener slashCommandListener = new SlashCommandListener();
        AutoCompleteHandler autoCompleteHandler = new AutoCompleteHandler();
        ModalListener modalListener = new ModalListener();
        ContextMenuListener contextMenuListener = new ContextMenuListener();


        // Load Event Listener
        logger.info("\nLoading Event Listener...");

        logger.info("slashCommandListener");
        builder.addEventListeners(slashCommandListener);
        System.out.print(" -> LOADED\n");

        logger.info("eventListener");
        builder.addEventListeners(eventListener);
        System.out.print(" -> LOADED\n");

        logger.info("buttonHandler");
        builder.addEventListeners(buttonHandler);
        System.out.print(" -> LOADED\n");

        logger.info("autoCompleteHandler");
        builder.addEventListeners(autoCompleteHandler);
        System.out.print(" -> LOADED\n");

        logger.info("modalListener");
        builder.addEventListeners(modalListener);
        System.out.print(" -> LOADED\n");

        logger.info("contextMenuListener");
        builder.addEventListeners(contextMenuListener);
        System.out.print(" -> LOADED\n");

        System.out.println(" ");

        builder.setStatus(OnlineStatus.ONLINE);

        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

        EnumSet<CacheFlag> enumSet = EnumSet.of(CacheFlag.ONLINE_STATUS, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.VOICE_STATE);

        builder.enableCache(enumSet);

        return builder;
    }

    public void run() {
        String line = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    
        try {
            while((line = reader.readLine()) != null) {
    
                if(line.equalsIgnoreCase("exit")) {
                    logger.info("System Exit..");
                    System.exit(0);
                }

                if (line.equalsIgnoreCase("restart")) {
                    new RestartBot();
                }

                if(line.equalsIgnoreCase("restart")) {
                    try {
                        // Pfad zur Java-Anwendung
                        String javaPath = "/usr/bin/java"; // Der Pfad zu Ihrer Java-Installation
                        String classPath = "com.cubowbot.cubow.Main"; // Der Hauptklasse Ihrer Anwendung

                        // Erstellen Sie ein ProcessBuilder-Objekt
                        ProcessBuilder builder = new ProcessBuilder();

                        // Setzen Sie das Kommando zum Starten der Java-Anwendung
                        builder.command(javaPath, "-cp", "your_classpath", classPath); // Ersetzen Sie "your_classpath" durch den tatsächlichen Klassenpfad Ihrer Anwendung

                        // Starten Sie den Prozess
                        Process process = builder.start();

                        // Warten Sie, bis der Prozess beendet ist
                        int exitCode = process.waitFor();

                        // Überprüfen Sie den Exit-Code
                        if (exitCode == 0) {
                            logger.info("Application restarted successfully");
                            // Beenden Sie die aktuelle Anwendung
                            System.exit(0);
                        } else {
                            logger.info("Application restart failed with exit code: " + exitCode);
                        }
                    } catch (IOException e) {
                        logger.info("Error restarting application: " + e.getMessage());
                    } catch (InterruptedException e) {
                        // Handle the InterruptedException
                        Thread.currentThread().interrupt(); // Preserve interrupt status
                        logger.info("Application restart interrupted");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeActivity(JDA bot) {

        // Load Activity Text
        String activityText = "cubow.nimawoods.de";

        Activity.ActivityType activityType = null;
        activityType = getStatusType();

        // Set Activity
        bot.getPresence().setActivity(Activity.of(activityType, activityText));
    }

    private Activity.ActivityType getStatusType() {

        String activityType = "watching";

        return switch (activityType.toLowerCase()) {
            case "watching" -> Activity.ActivityType.WATCHING;
            case "listening" -> Activity.ActivityType.LISTENING;
            case "streaming" -> Activity.ActivityType.STREAMING;
            default -> Activity.ActivityType.PLAYING;
        };
    }

    public class RestartBot {

        public static void main(String[] args) {
            try {
                logger.info("Initiating restart...");
                Runtime.getRuntime().exec("./start_bot.sh");
                System.exit(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
