package com.cubowbot.cubow.handler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class AutoModHandler {

    public void checkForBadWord(MessageReceivedEvent event) {

        List<String> badWords = new ArrayList<String>();
        badWords.add("卐");
        badWords.add("卍");

        String content = event.getMessage().getContentDisplay();
        for (String word : badWords) {
            if (content.contains(word)) {

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Bad Word");
                embedBuilder.setDescription("This Word or Symbol is not allowed on this server");
                embedBuilder.addField("Word detected", word, true);
                embedBuilder.setColor(Color.MAGENTA);

                event.getMessage().replyEmbeds(embedBuilder.build()).queue();
                event.getMessage().delete().queue();
                return;
            }
        }
    }
}