package com.cubowbot.cubow.handler;

import com.cubowbot.cubow.generator.ImageGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WelcomeHandler {
    public void sendWelcomingMessage(GuildMemberJoinEvent event) {
        try {
            Guild guild = event.getGuild();

            System.out.println(event.getMember() + " joined to the server!");

            // Send welcome message as an Embed to the specified channel
            ConfigHandler configHandler = new ConfigHandler();

            String welcomeID = configHandler.getServerConfig(event.getGuild().getId(), "Welcome_Channel");

            TextChannel welcomeChannel = guild.getTextChannelById(welcomeID);
            if (welcomeChannel != null) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Welcome to the server!");
                embed.setDescription("Welcome " + event.getMember().getAsMention() + " to the server!");
                embed.setColor(Color.MAGENTA);

                // Generiere Welcome Bild
                ImageGenerator.generateWelcomeImage(event.getMember());

                embed.setImage("attachment://welcome_image.png");

                Files.delete(Path.of("welcome_image.png"));
                welcomeChannel.sendMessageEmbeds(embed.build()).queue();

            } else {
                System.out.println("Welcome channel not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
