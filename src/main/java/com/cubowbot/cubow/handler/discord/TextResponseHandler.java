package com.cubowbot.cubow.handler.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.OffsetDateTime;
import java.util.Objects;

public class TextResponseHandler {

    private final SlashCommandInteractionEvent event;

    public TextResponseHandler(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void getWebsite() {
        String website = ConfigHandler.getServerConfig(Objects.requireNonNull(event.getGuild()).getId(), "website");

        event.reply(website).setEphemeral(true).queue();
    }

    public void sendPing() {
        OffsetDateTime time = event.getTimeCreated();
        event.reply("pong!" + time).setEphemeral(true).queue();
    }
}
