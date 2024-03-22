package com.cubowbot.cubow.handler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BotHandler extends Thread {

    ConfigHandler configHandler = new ConfigHandler();

    public void run() {
        String line = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    
        try {
            while((line = reader.readLine()) != null) {
    
                if(line.equalsIgnoreCase("exit")) {
                    System.out.println("System Exit..");
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
                            System.out.println("Application restarted successfully");
                            // Beenden Sie die aktuelle Anwendung
                            System.exit(0);
                        } else {
                            System.out.println("Application restart failed with exit code: " + exitCode);
                        }
                    } catch (IOException e) {
                        System.out.println("Error restarting application: " + e.getMessage());
                    } catch (InterruptedException e) {
                        // Handle the InterruptedException
                        Thread.currentThread().interrupt(); // Preserve interrupt status
                        System.out.println("Application restart interrupted");
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
                System.out.println("Initiating restart...");
                Runtime.getRuntime().exec("./start_bot.sh");
                System.exit(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
