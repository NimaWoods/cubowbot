package com.cubowbot.cubow.generator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.cubowbot.cubow.handler.ImageHandler;
import net.dv8tion.jda.api.entities.Member;

public class ImageGenerator {
    public static void generateWelcomeImage(Member member) throws IOException {

        ImageHandler.downloadImage(member.getEffectiveAvatarUrl(), "avatar.png");

        BufferedImage background = ImageIO.read(new File("welcome.jpg"));
        BufferedImage profileImage = ImageIO.read(new File(String.valueOf("avatar.png")));

        // Hintergrund hinuzfügen
        BufferedImage resultImage = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g = resultImage.createGraphics();
        g.drawImage(background, 0, 0, null);

        // Position des Profilbildes in der Mitte des Bildes
        int x = (background.getWidth() - profileImage.getWidth()) / 2;
        int y = (background.getHeight() - profileImage.getHeight()) / 2;

        g.drawImage(profileImage, x, y, null);
        g.dispose();

        // Speichere das generierte Bild
        ImageIO.write(resultImage, "png", new File("welcome_image.png"));

        Files.delete(Path.of("welcome.jpg"));
        Files.delete(Path.of("avatar.png"));
    }
}
