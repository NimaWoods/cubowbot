package com.cubowbot.cubow.handler;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class BetaHandler {
    private static final Logger logger = LoggerFactory.getLogger(BetaHandler.class);
    public void sendBetaMessage() {
        CubowApplication cubowApplication = new CubowApplication();
        JDA bot = cubowApplication.getJDA();

        Guild cubowServer = bot.getGuildById("1217994812108832880");

        TextChannel channel = cubowServer.getTextChannelById("1221229660021588129");

        if (channel != null) {
            try {
                // Create the embed message
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Cubow Beta");
                embedBuilder.setDescription("Bekomme einen Cubow Beta Key.\nZur Zeit agiert Cubow nur wenn der Owner des Servers im Beta Programm eingetragen ist.");
                embedBuilder.setColor(Color.MAGENTA);
                embedBuilder.setFooter("cubow", bot.getSelfUser().getAvatarUrl());

                // Send the message with action rows
                channel.sendMessageEmbeds(embedBuilder.build())
                        .setActionRow(Button.primary("betajoin", "Join Beta"))
                        .queue();

            } catch (Exception e) {
                logger.info("Error sending message: " + e.getMessage());
            }
        } else {
            logger.info("Channel not found!");
        }
    }
}
