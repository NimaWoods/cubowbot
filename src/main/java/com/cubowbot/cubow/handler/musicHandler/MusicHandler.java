package com.cubowbot.cubow.handler.musicHandler;

import com.cubowbot.cubow.generator.EmbedGenerator;
import com.cubowbot.cubow.handler.EmbedHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Widget.VoiceState;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MusicHandler {

    public void play(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState voiceState = member.getVoiceState();

        if(!voiceState.inAudioChannel()) {
            EmbedGenerator embedGenerator = new EmbedGenerator();
            embedGenerator.failure(event, "You have to be in a voice channel for this command to be working", true);
            return;
        }

        event.getGuild().getAudioManager().openAudioConnection(voiceState.getChannel());
        PlayerManager playerManager = PlayerManager.get();
        playerManager.play(event.getGuild(), event.getOption("link").getAsString());
    }
}
