package com.cubowbot.cubow.Handler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ImageHandler {
    public void downloadWelcomeImage(JDA bot) {
        try {
            Guild guild = bot.getGuildById("1052647405616644216");
            if (guild == null) {
                System.out.println("Guild not found.");
                return;
            }

            TextChannel channel = guild.getTextChannelById("1116302198113062963");
            if (channel == null) {
                System.out.println("Channel not found.");
                return;
            }

            channel.retrieveMessageById("1207470487354220555").queue(message -> {
                List<Message.Attachment> attachments = message.getAttachments();
                String destinationFile = "welcome.jpg";

                attachments.stream()
                        .filter(Message.Attachment::isImage)
                        .findFirst()
                        .ifPresent(attachment -> {
                            Path path = Paths.get(destinationFile);
                            if (!Files.exists(path)) {
                                try {
                                    // The downloadImage method should be defined to handle the download
                                    downloadImage(attachment.getUrl(), destinationFile);
                                    System.out.println("Welcome Image downloaded successfully.");
                                } catch (IOException e) {
                                    System.out.println("Error downloading Welcome image: " + e.getMessage());
                                }
                            }
                        });
            });
        } catch (Exception e) {
            System.out.println("Error downloading Welcome image: " + e.getMessage());
        }
    }

    public static void downloadImage(String imageUrl, String fileName) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream();
             OutputStream out = new BufferedOutputStream(new FileOutputStream(fileName))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
