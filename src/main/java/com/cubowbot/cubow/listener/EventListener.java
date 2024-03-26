package com.cubowbot.cubow.listener;

import com.cubowbot.cubow.CubowApplication;
import com.cubowbot.cubow.handler.AutoModHandler;
import com.cubowbot.cubow.handler.ConfigHandler;
import com.cubowbot.cubow.handler.DataBaseHandler;
import com.cubowbot.cubow.handler.WelcomeHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EventListener.class);

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        Role role = guild.getRoleById("1052663439044120686");

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
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        EmbedBuilder eb = new EmbedBuilder();

        Guild guild = event.getGuild();

        List<Document> betaMembers = dataBaseHandler.getAllBetaMembers();
        List<String> betaMemberIds = betaMembers.stream()
                .map(doc -> doc.getString("userID"))
                .collect(Collectors.toList());

        List<Member> adminMembers = guild.getMembers().stream()
                .filter(member -> member.getRoles().stream()
                        .anyMatch(role -> role.getName().equalsIgnoreCase("admin")))
                .collect(Collectors.toList());

        boolean isAdminBetaMember = adminMembers.stream()
                .anyMatch(member -> betaMemberIds.contains(member.getId()));

        if (isAdminBetaMember) {
            logger.info("Cubow joined server " + event.getGuild().getName());
        } else {
            TextChannel textChannel = (TextChannel) guild.getDefaultChannel();

            eb.setTitle("Cubow Bot");
            eb.setDescription("Jemand hat gerade versucht, Cubow einzuladen, obwohl keiner der Administratoren dieses Servers am Cubow-Betaprogramm teilnimmt." +
                    "Derzeit verfügen wir über eine begrenzte Bandbreite und müssen das Interesse an Cubow abschätzen. Daher gewähren wir derzeit nur Zugang zur Beta." +
                    "\n\n" +
                    "Wenn du Interesse an Betaplätzen oder allgemeinen Updates zu Cubow hast, kannst du unserem offiziellen Discord-Server beitreten. " +
                    "Dort kannst du Cubow ausprobieren oder der Beta beitreten.");
            eb.setColor(Color.MAGENTA);

            textChannel.sendMessageEmbeds(eb.build())
                    .setActionRow(Button.link("https://discord.gg/xHYD4Bm5x6", "Cubow Discord"))
                    .queue();

            guild.leave().queue();
        }

    }

    @Override
    public void onUserActivityStart(UserActivityStartEvent event) {
        if (event.getNewActivity().getType() == Activity.ActivityType.STREAMING) {
            TextChannel channel = event.getJDA().getTextChannelById(ConfigHandler.getServerConfig(event.getGuild().getId(), "Live_Notification_Channel"));
            String streamerName = event.getUser().getName();
            String streamUrl = event.getNewActivity().getUrl();
            channel.sendMessage(streamerName + " ist Live auf " + streamUrl).queue();
        }
    }

    /*@Override
    public void onUserActivityEnd(UserActivityEndEvent event) {
        if (event.getOldActivity().getType() == Activity.ActivityType.STREAMING) {
            TextChannel channel = event.getJDA().getTextChannelById(ConfigHandler.getServerConfig(event.getGuild().getId(), "Live_Notification_Channel"));
            String streamerName = event.getUser().getName();
            channel.sendMessage(streamerName + " has ended the stream.").queue();
        }
    }*/

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        AutoModHandler autoModHandler = new AutoModHandler(event);
        autoModHandler.checkForBadWord();
        autoModHandler.checkForLink();
    }
}