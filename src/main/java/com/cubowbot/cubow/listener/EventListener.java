package com.cubowbot.cubow.listener;

import com.cubowbot.cubow.handler.discord.AutoModHandler;
import com.cubowbot.cubow.handler.discord.ConfigHandler;
import com.cubowbot.cubow.handler.discord.WelcomeHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.OffsetDateTime;

public class EventListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EventListener.class);

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        Role role = guild.getRoleById(ConfigHandler.getServerConfig(event.getGuild().getId(),"Join_Autorole"));

        if (role != null) {
            guild.addRoleToMember(event.getMember(), role).queue();
        } else {
            logger.info("Role not found");
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

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Hallo, ich bin Cubow");
        embedBuilder.setDescription("Vielen Dank, dass du Cubow nutzt. Wir sind gerade noch mitten im Aufbau, aber wir freuen uns, dass du dabei bist.");
        embedBuilder.addField("Cubow einrichten", "/setup", true);
        embedBuilder.setColor(Color.MAGENTA);
        event.getGuild().getDefaultChannel().asTextChannel().sendMessageEmbeds(embedBuilder.build())
                .setActionRow(Button.link("https://discord.com/oauth2/authorize?client_id=1217485873508253839", "Cubow einladen"),
                              Button.link("https://discord.gg/xHYD4Bm5x6", "Cubow Discord Server"))
                .queue();
    }

    @Override
    public void onUserActivityStart(UserActivityStartEvent event) {
        if (ConfigHandler.getServerConfig(event.getGuild().getId(), "Live_Notification_Channel") != null) {
            if (event.getNewActivity().getType() == Activity.ActivityType.STREAMING) {
                System.out.println(event.getMember() + " ist live");
                TextChannel channel = event.getJDA().getTextChannelById(ConfigHandler.getServerConfig(event.getGuild().getId(), "Live_Notification_Channel"));
                String streamerName = event.getUser().getName();
                String streamUrl = event.getNewActivity().getUrl();
                channel.sendMessage(streamerName + " ist Live auf " + streamUrl).queue();
            }
        }
    }

    @Override
    public void onUserActivityEnd(UserActivityEndEvent event) {
        if (ConfigHandler.getServerConfig(event.getGuild().getId(), "Offline_Notification_Channel") != null) {
            if (event.getOldActivity().getType() == Activity.ActivityType.STREAMING) {
                TextChannel channel = event.getJDA().getTextChannelById(ConfigHandler.getServerConfig(event.getGuild().getId(), "Live_Notification_Channel"));
                String streamerName = event.getUser().getName();
                channel.sendMessage(streamerName + " has ended the stream.").queue();
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        AutoModHandler autoModHandler = new AutoModHandler(event);
        /*autoModHandler.checkForBadWord();*/
        autoModHandler.checkForLink();
    }
}