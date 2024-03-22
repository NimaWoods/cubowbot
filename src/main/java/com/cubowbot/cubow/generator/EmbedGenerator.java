package com.cubowbot.cubow.generator;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class EmbedGenerator {
    public void buildEmbed(
            SlashCommandInteractionEvent event,
            Guild server,
            String title,
            String description,
            Color color,
            String titlelink,
            String footer,
            String footericon,
            String author,
            String authorlink
    ) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        System.out.println("Building Embed on Server " + server.getName() + "...");
        if (title!= null) {
            if (titlelink != null) {
                embedBuilder.setTitle(title, titlelink);
            }
            embedBuilder.setTitle(title);
        }

        if (description!= null) {
            embedBuilder.setDescription(description);
        }

        if (color!= null) {
            embedBuilder.setColor(color);
        }

        if (footer!= null) {
            embedBuilder.setFooter(footer, footericon);
        }

        if (author!= null) {
            if (authorlink!= null) {
                embedBuilder.setAuthor(author, authorlink);
            }
            embedBuilder.setAuthor(author);
        }

        if (event.isAcknowledged()) {
            event.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
        } else {
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }

    }

    public void success(SlashCommandInteractionEvent event, String successMessage, boolean ephemeral) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setDescription(successMessage);
        embedBuilder.setTitle("Success");

        if (event.isAcknowledged()) {
            event.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
        } else {
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }
    }

    public void failure(SlashCommandInteractionEvent event, String failureMessage, boolean ephemeral) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setDescription(failureMessage);
        embedBuilder.setTitle("Failed");

        if (event.isAcknowledged()) {
            event.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
        } else {
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }
    }

    public void noPermissions(SlashCommandInteractionEvent event) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setDescription("Du hast nicht die Berechtigungen, diesen Befehl auszuführen.");
        embedBuilder.setTitle("Fehlende Berechtigungen");

        if (event.isAcknowledged()) {
            event.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
        } else {
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }

    }

}