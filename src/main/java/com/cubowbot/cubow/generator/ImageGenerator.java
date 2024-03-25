package com.cubowbot.cubow.generator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;

public class ImageGenerator {
    public static void generateWelcomeImage(Member member, Runnable callback) {

        CubowApplication cubowApplication = new CubowApplication();
        JDA bot = cubowApplication.getJDA();

        BufferedImage[] images = new BufferedImage[2];

        bot.getGuildById("1052647405616644216").getTextChannelById("1116302198113062963").retrieveMessageById("1207470487354220555").queue(message -> {
            try {
                images[0] = ImageIO.read(new URL(message.getAttachments().getFirst().getUrl())); // Hintergrundbild
                images[1] = ImageIO.read(new URL(member.getEffectiveAvatarUrl())); // Profilbild

                // Hintergrund hinzufügen
                BufferedImage background = images[0];
                BufferedImage profileImage = images[1];

                // Überprüfen, ob das Profilbild nicht null ist
                if (profileImage != null) {
                    // Bildgröße einstellen
                    BufferedImage resultImage = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_RGB);

                    Graphics2D g = resultImage.createGraphics();
                    g.drawImage(background, 0, 0, null);

                    // Position des Profilbildes in der Mitte des Bildes
                    int x = (background.getWidth() - profileImage.getWidth()) / 2;
                    int y = (background.getHeight() - profileImage.getHeight()) / 2;

                    g.drawImage(profileImage, x, y, null);
                    g.dispose();

                    // Speichern des generierten Bildes
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ImageIO.write(resultImage, "png", new File("welcome_image.png"));

                    ImageIO.read(new File("welcome_image.png"));

                    // Benachrichtigung über Abschluss des Vorgangs
                    callback.run();
                } else {
                    System.err.println("Profilbild ist null.");
                }
            } catch (Exception e) {
                System.err.println("Fehler beim Lesen der Datei: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}