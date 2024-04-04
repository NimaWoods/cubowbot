package com.cubowbot.cubow.handler.discord.music;

import com.cubowbot.cubow.CubowApplication;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.managers.AudioManager;

public class MusicHandler {

    private final SlashCommandInteractionEvent event;
    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

    public AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    public MusicHandler(SlashCommandInteractionEvent event) {
        AudioSourceManagers.registerRemoteSources(playerManager);
        this.playerManager = playerManager;

        this.event = event;
    }

    public void join() {

        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if (memberVoiceState.inAudioChannel()) {

                VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel().asVoiceChannel();
                AudioManager audioManager = event.getGuild().getAudioManager();

                audioManager.openAudioConnection(voiceChannel);

                event.reply("Joined VC " + voiceChannel.getAsMention());

        } else if(selfVoiceState.getChannel().asVoiceChannel().getId() == memberVoiceState.getChannel().asVoiceChannel().getId()) {
            event.reply("Bot already in VC");
        } else {
            event.reply("You are not in a VC");
        }
    }

    public void play() {
        join();
        AudioPlayer player = playerManager.createPlayer();
    }

    public void leave() {
        Member selfMember = event.getGuild().getSelfMember();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (selfMember.getVoiceState().inAudioChannel()) {
            audioManager.closeAudioConnection();
        }
    }

}
