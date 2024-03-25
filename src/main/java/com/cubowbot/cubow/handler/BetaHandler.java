package com.cubowbot.cubow.handler;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class BetaHandler {
    private static final Logger logger = LoggerFactory.getLogger(BetaHandler.class);

    public void sendBetaMessage() {
        CubowApplication cubowApplication = new CubowApplication();
        JDA bot = cubowApplication.getJDA();

        Guild cubowServer = bot.getGuildById("1217994812108832880");

        TextChannel channel = cubowServer.getTextChannelById("1221229660021588129");

        String title = "Cubow Beta";
        String description = "Sichere dir jetzt Zugang zum Cubow Beta-Programm. " +
                "\n\nCubow kann nur aktiviert werden, wenn ein Benutzer mit Administratorrechten auf dem Server am Beta-Programm teilnimmt.";

        // Create the embed message
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setFooter("Cubow sammelt keine Daten über dich oder deine Mitglieder!", bot.getSelfUser().getAvatarUrl());

        if (channel != null) {
            try {
                String channelID = channel.getLatestMessageId();
                // Edit the latest message embed with the updated values of embedBuilder

                Message message = channel.retrieveMessageById(channel.getLatestMessageId()).complete();

                MessageEmbed embed = message.getEmbeds().getFirst();

                // Only Edit if not equal
                if(!Objects.equals(embed.getTitle(), title) || !Objects.equals(embed.getDescription(), description)) {
                    channel.deleteMessageById(message.getId()).queue();
                    channel.sendMessageEmbeds(embedBuilder.build())
                            .setActionRow(Button.primary("betajoin", "Join Beta"))
                            .addActionRow(Button.link(bot.getInviteUrl(Permission.ADMINISTRATOR), "Invite"))
                            .queue();
                }
            } catch (Exception e) {

                channel.sendMessageEmbeds(embedBuilder.build())
                        .setActionRow(Button.primary("betajoin", "Join Beta"),
                                Button.link(bot.getInviteUrl(Permission.ADMINISTRATOR), "Invite"))
                        .queue();
            }
        } else {
            logger.info("Channel not found!");
        }
    }

    public void memberJoinedBeta(ButtonInteractionEvent event ) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        EmbedBuilder eb = new EmbedBuilder();

        if (dataBaseHandler.getAllBetaMembers().stream().count() < 10) {
            dataBaseHandler.saveBetaMember(event.getMember().getId());
            eb.setTitle("Joined Beta");
            eb.setDescription("Du wurdest ins Beta Programm aufgenommen und kannst Cubow jetzt einladen!");
            eb.setColor(Color.MAGENTA);

            event.replyEmbeds(eb.build())
                    .setEphemeral(true)
                    .setActionRow(Button.link("https://cubow.nimawoods.de/invite", "Cubow einladen"))
                    .queue();
        } else {
            eb.setTitle("Alle Plätze sind belegt");
            eb.setDescription("Leider schaffen unsere Server diesen Ansturm nicht und wir müssen upgraden. Wir benachrichtigen dich aber sobald wieder" +
                    "neue Plätze da sind");
            eb.setImage("https://i.imgur.com/9jD9bSF.jpg");
            eb.setColor(Color.MAGENTA);

            event.replyEmbeds(eb.build())
                    .setEphemeral(true)
                    .queue();
        }
    }

}
