package com.cubowbot.cubow.listener;

import com.cubowbot.cubow.handler.ConfigHandler;
import com.cubowbot.cubow.handler.WelcomeHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.OffsetDateTime;

public class EventListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        Role role = guild.getRoleById("1052663439044120686");

        if (role != null) {
            guild.addRoleToMember(event.getMember(), role).queue();
        } else {
            System.out.println("Role not found");
        }

        WelcomeHandler welcomeHandler = new WelcomeHandler();
        welcomeHandler.sendWelcomingMessage(event);
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        User bannedUser = event.getUser();
        User banningUser = event.getGuild().retrieveBan(event.getUser()).complete().getUser();
        Guild guild = event.getGuild();

        String banReason = event.getGuild().retrieveBan(event.getUser()).complete().getReason();
        OffsetDateTime banTime = event.getGuild().retrieveBan(event.getUser()).complete().getUser().getTimeCreated();

        if (banReason == null) {
            banReason = "none";
        }

        String moderationNotificationChannel = ConfigHandler.getServerConfig(event.getGuild().getId(), "Moderation_Notification_Channel");

        TextChannel channel = event.getGuild().getTextChannelById(moderationNotificationChannel);

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(banningUser.getAsMention() + " banned " + bannedUser.getAsMention());
        eb.addField("Ban Reason", banReason, true);
        eb.addField("Ban Time", banTime.toString(), true);

        channel.sendMessageEmbeds(eb.build()).queue();
    }
}