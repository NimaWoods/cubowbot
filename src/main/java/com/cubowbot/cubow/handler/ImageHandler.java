package com.cubowbot.cubow.handler;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.List;

public class ImageHandler {
    private static final Logger logger = LoggerFactory.getLogger(ImageHandler.class);
    public void downloadWelcomeImage(JDA bot) {
        try {
            Guild guild = bot.getGuildById("1052647405616644216");
            if (guild == null) {
                logger.info("Guild not found.");
                return;
            }

            TextChannel channel = guild.getTextChannelById("1116302198113062963");
            if (channel == null) {
                logger.info("Channel not found.");
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
                                    logger.info("Welcome Image downloaded successfully.");
                                } catch (IOException e) {
                                    logger.info("Error downloading Welcome image: " + e.getMessage());
                                }
                            }
                        });
            });
        } catch (Exception e) {
            logger.info("Error downloading Welcome image: " + e.getMessage());
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
