package com.cubowbot.cubow.handler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Objects;

public class FeedbackHandler {

    private final SlashCommandInteractionEvent event;

    public FeedbackHandler(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void bug() {
        OkHttpClient client = new OkHttpClient();

        String link = null;
        try {
            if (event.getOption("link").getAsString() != null) {
                link = event.getOption("link").getAsString();
            }
        } catch (NullPointerException e) {
            link = "Not provided";
        }

        String json = "{\"content\": \"" +
                event.getOption("title").getAsString() +
                "\\n\\nBug Beschreibung: " + event.getOption("description").getAsString() +
                "\\n\\nTest Link: " + link +
                "\\n\\nTicket by: " + event.getMember().getNickname() +
                "\\n\\From Server: " + event.getGuild().getName() + " with ID " + event.getGuild().getId() +
                "\", \"severity\": \"high\"}";

        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));

        System.out.println(requestBody.toString());

        Request request = new Request.Builder()
                .url("https://api.codecks.io/user-report/v1/create-report?token=rt_qLe9UjUvXCSLnZWi8huRtfCs")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
            } else {
                System.out.println("Request failed: " + response.code() + " - " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void suggest() {
        OkHttpClient client = new OkHttpClient();

        String link = null;
        try {
            if (event.getOption("link").getAsString() != null) {
                link = event.getOption("link").getAsString();
            }
        } catch (NullPointerException e) {
            link = "Not provided";
        }

        String json = "{\"content\": \"" +
                event.getOption("title").getAsString() +
                "\\n\\nBug Beschreibung: " + event.getOption("description").getAsString() +
                "\\n\\nTestLink: " + link +
                "\\n\\nTicket by: " + event.getMember().getNickname() +
                "\\n\\From Server: " + event.getGuild().getName() + " with ID " + event.getGuild().getId() +
                "\", \"severity\": \"high\"}";

        System.out.println(json);

        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));

        System.out.println(requestBody.toString());

        Request request = new Request.Builder()
                .url("https://api.codecks.io/user-report/v1/create-report?token=rt_NWIJ7raAtKB4YDDOtWo6Im7O")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(event.getOption("title").getAsString(), "https://open.codecks.io/cubowbot/decks/14-suggestions");
                embedBuilder.setDescription("Dein Vorschlag wurde erfolgreich eingetragen. Du kannst den Fortschritt hier verfolgen: ");
                embedBuilder.addField("suggestslink", "Suggestions",false);
                embedBuilder.setFooter(event.getMember().getEffectiveName(), event.getMember().getEffectiveAvatarUrl());

                event.replyEmbeds(embedBuilder.build()).queue();
            } else {
                System.out.println("Request failed: " + response.code() + " - " + response.message());
                event.reply("Something went wrong :/")
                        .setEphemeral(true)
                        .queue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
