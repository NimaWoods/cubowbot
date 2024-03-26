package com.cubowbot.cubow.handler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AutoModHandler {

    public AutoModHandler(MessageReceivedEvent event) {
        this.event = event;
    }

    MessageReceivedEvent event = this.event;

    public void checkForBadWord() {

        List<String> badWordsList = new ArrayList<String>();
        String urlEN = "https://raw.githubusercontent.com/LDNOOBW/List-of-Dirty-Naughty-Obscene-and-Otherwise-Bad-Words/master/en";
        String urlDE = "https://raw.githubusercontent.com/LDNOOBW/List-of-Dirty-Naughty-Obscene-and-Otherwise-Bad-Words/master/de";

        badWordsList.add("卐");
        badWordsList.add("卍");


        // Loading BadWords list
        try {
            // English
            URL fileUrlEN = new URL(urlEN);
            BufferedReader readerEN = new BufferedReader(new InputStreamReader(fileUrlEN.openStream()));

            String lineEN;
            while ((lineEN = readerEN.readLine()) != null) {
                badWordsList.add(lineEN);
            }
            readerEN.close();

            // Deutsch
            URL fileUrlDE = new URL(urlDE);
            BufferedReader readerDE = new BufferedReader(new InputStreamReader(fileUrlDE.openStream()));

            String lineDE;
            while ((lineDE = readerDE.readLine()) != null) {
                badWordsList.add(lineDE);
            }
            readerDE.close();
        } catch (IOException e) {
            System.err.println("Error reading from URL: " + e.getMessage());
            e.printStackTrace();
        }

        String content = event.getMessage().getContentDisplay();
        for (String word : badWordsList) {
            if (content.contains(word)) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Bad Word");
                embedBuilder.setDescription("Your message contains a Word that is not allowed on this server");
                embedBuilder.addField("User", event.getMember().getAsMention(), true);
                embedBuilder.setColor(Color.MAGENTA);

                event.getMessage().replyEmbeds(embedBuilder.build())
                        .addActionRow(Button.link("https://github.com/LDNOOBW/List-of-Dirty-Naughty-Obscene-and-Otherwise-Bad-Words/tree/master", "Bad Word List"))
                        .queue();
                event.getMessage().delete().queue();
                return;
            }
        }
    } public void checkForLink() {
        String message = event.getMessage().getContentRaw();

        if (message.contains("\\(?\\bhttp://[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Bad Word");
            embedBuilder.setDescription("Your message contains a Link that is not allowed on this server");
            embedBuilder.addField("User", event.getMember().getAsMention(), true);
            embedBuilder.setColor(Color.MAGENTA);

            event.getMessage().replyEmbeds(embedBuilder.build()).queue();
            event.getMessage().delete().queue();
            return;
        };
    }
}