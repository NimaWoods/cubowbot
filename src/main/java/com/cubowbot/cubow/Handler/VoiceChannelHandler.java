package com.cubowbot.cubow.Handler;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.EnumSet;

public class VoiceChannelHandler {
    public net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel createVoiceChannel(Guild server, Member member, String channelName, Role... roles) {
        Category category = server.getCategoryById("1052662732505223188");

        ChannelAction<net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel> channelAction = category.createVoiceChannel(channelName);

        channelAction.addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL), null);

        for (Role role : roles) {
            channelAction.addPermissionOverride(role, EnumSet.of(Permission.VIEW_CHANNEL), null);
        }

        channelAction.addPermissionOverride(server.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL));

        // Create the channel and get the result
        net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel createdChannel = channelAction.complete();

        return createdChannel;
    }
}
