package com.cubowbot.cubow.listener;

import com.cubowbot.cubow.handler.ConfigHandler;
import com.cubowbot.cubow.handler.WelcomeHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.OffsetDateTime;

public class EventListener extends ListenerAdapter {
     //private static final List<Long> CHANNEL_IDS = Arrays.asList(1053403251082534933L, 1134085819729203300L);

    /*@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (CHANNEL_IDS.contains(event.getChannel().getIdLong())) {
            if (!event.getAuthor().isBot()) {

                EmbedBuilder eb = new EmbedBuilder();

                String content = event.getMessage().getContentDisplay();

                String title = null;
                String description = null;

                String[] lines = content.split("\\R", 2);
                try {
                    title = lines[0];
                    description = lines[1];
                } catch (Exception e) {

                    event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
                    event.getChannel().sendMessage("Message need Title and Text..").queue(message -> {
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                   throw e;
                };

                String author = event.getMember().getNickname();
                String pb = event.getMember().getUser().getEffectiveAvatarUrl();

                Message message = event.getMessage();
                Mentions mentions = message.getMentions();

                eb.setTitle(title, null); 
                eb.setColor(Color.magenta);  
                eb.setDescription(description);

                eb.setFooter(author, pb);

                List<Role> mentionedRoles = mentions.getRoles();
                if (!mentionedRoles.isEmpty()) {
                    String mention = mentionedRoles.get(0).getName();

                    description = description.replace("@" + mention, "");
                    title = title.replace("@" + mention, "");
                    
                    eb.setDescription("@" + mention + " " + description);
                    eb.setTitle(title);
                }

                if (!event.getMessage().getAttachments().isEmpty()) {
                    Attachment attachment = event.getMessage().getAttachments().get(0);
                    eb.setImage(attachment.getUrl());
                }
        
                event.getMessage().delete().queue();
                event.getChannel().sendMessageEmbeds(eb.build()).queue();
            }
        }
    }*/

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