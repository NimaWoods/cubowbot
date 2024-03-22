package com.cubowbot.cubow.handler;

import com.cubowbot.cubow.generator.EmbedGenerator;
import io.github.artemnefedov.javaai.dto.Chat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.Color;
import java.util.List;

public class AiCommandHandler {

    private final SlashCommandInteractionEvent event;

    public AiCommandHandler(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    ChatGPTHandler chatGPTHandler = new ChatGPTHandler();

    public void chatgpt() {

        RoleHandler roleHandler = new RoleHandler();

        Guild server = event.getGuild();
        Member member = event.getMember();
        List<Role> roles = member.getRoles();

        if (roleHandler.checkIfModerator(roles, server)) {
            event.deferReply().queue();

            OptionMapping prompt = event.getOption("prompt");

            assert prompt != null;

            String message = prompt.getAsString();
            String response = chatGPTHandler.generateText(message, event.getGuild());

            event.getHook().editOriginal(response).queue();
        } else {
            EmbedGenerator embedGenerator = new EmbedGenerator();
            embedGenerator.noPermissions(event);
        }
    }

    public void dalle() {

        RoleHandler roleHandler = new RoleHandler();

        Guild server = event.getGuild();
        Member member = event.getMember();
        List<Role> roles = member.getRoles();

        if (roleHandler.checkIfModerator(roles, server)) {
            event.deferReply().queue();

            ChatGPTHandler chatGPTHandler = new ChatGPTHandler();
            String imageUrl = chatGPTHandler.generateImage(String.valueOf(event.getOption("prompt")), event.getGuild());

            EmbedBuilder eb = new EmbedBuilder();
            if (imageUrl.contains("Content Policy Violation")) {
                eb.setDescription(imageUrl);
                eb.setColor(Color.RED);
            } else {
                eb.setImage(imageUrl);
                eb.setColor(Color.GREEN);
            }

            event.getHook().editOriginalEmbeds(eb.build()).queue();

        } else {
            EmbedGenerator embedGenerator = new EmbedGenerator();
            embedGenerator.noPermissions(event);
        }
    }

}
