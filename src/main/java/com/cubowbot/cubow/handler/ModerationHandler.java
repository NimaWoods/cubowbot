package com.cubowbot.cubow.handler;

import com.cubowbot.cubow.CubowApplication;
import com.cubowbot.cubow.generator.EmbedGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.io.ObjectInputFilter.Config;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ModerationHandler {

    private final SlashCommandInteractionEvent event;
    private static final Logger logger = LoggerFactory.getLogger(ModerationHandler.class);

    public ModerationHandler(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void ban() {

        EmbedGenerator embedConstructor = new EmbedGenerator();

        Member member = event.getMember();

        if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
            User user = event.getOption("user").getAsUser();
            event.getGuild().getMember(user).ban(0, TimeUnit.MINUTES).queue(
                    success -> embedConstructor.success(event, "Successfully banned " + user.getAsMention(), true),
                    error -> {
                        embedConstructor.failure(event, "Failed to ban " + user.getAsMention() + "\n" + error.getMessage(), true);
                    }
            );
            event.reply("Banned " + user.getAsMention());
        } else {
            embedConstructor.noPermissions(event);
        }

    }

    public void kick() {
        EmbedGenerator embedConstructor = new EmbedGenerator();

        Member member = event.getMember();
        if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
            User user = event.getOption("user").getAsUser();
            event.getGuild().kick(user).queue(
                    success -> embedConstructor.success(event, "Successfully kicked " + user.getAsMention() + " on server " + event.getGuild().getId(), true),
                    error -> embedConstructor.failure(event, "Failed to kick " + user.getAsMention() + "\n" + error.getMessage(), true)
            );
        } else {
            embedConstructor.noPermissions(event);
        }

    }

    public void timeout() {
        Member member = event.getMember();
        EmbedGenerator embedConstructor = new EmbedGenerator();

        assert member != null;
        if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
            User user = Objects.requireNonNull(event.getOption("user")).getAsUser();
            OptionMapping durationOption = event.getOption("duration"); // Get the duration option
            int duration = durationOption != null ? (int) durationOption.getAsLong() : 0; // If the duration option is not null, get its value. Otherwise, set duration to 0.

            long hours = event.getOption("hours") != null ? Objects.requireNonNull(event.getOption("hours")).getAsLong() : 0;
            long minutes = event.getOption("minutes") != null ? Objects.requireNonNull(event.getOption("minutes")).getAsLong() : 0;
            long seconds = event.getOption("seconds") != null ? Objects.requireNonNull(event.getOption("seconds")).getAsLong() : 0;

            long totalSeconds = hours * 3600 + minutes * 60 + seconds;

            Objects.requireNonNull(event.getGuild()).timeoutFor(user, totalSeconds, TimeUnit.SECONDS).queue(
                    success -> embedConstructor.success(event, "Successfully timeouted" + user.getAsMention(), true),
                    error -> embedConstructor.failure(event, "Failed to timeout " + user.getAsMention() + "\n" + error.getMessage(), true)
            );
        } else {
            embedConstructor.noPermissions(event);
        }
    }

    public void removeTimeout() {
        EmbedGenerator embedConstructor = new EmbedGenerator();
        Member member = event.getMember();
        if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
            Member user = Objects.requireNonNull(event.getOption("user")).getAsMember();
            event.getGuild().removeTimeout(user);
        } else {
            embedConstructor.noPermissions(event);
        }
    }

    public void lockdownchannel() {
        MessageChannelUnion channelUnion = event.getChannel();
        TextChannel channel = (TextChannel) channelUnion;
        Role role = event.getGuild().getPublicRole(); // This gets the @everyone role.

        channel.upsertPermissionOverride(role)
                .deny(Permission.MESSAGE_SEND)
                .queue();

        channel.sendMessage("Channel locked down.").queue();

        int minutes = event.getOption("minuten").getAsInt();
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                channel.upsertPermissionOverride(role)
                        .grant(Permission.MESSAGE_SEND)
                        .queue();
                channel.sendMessage("Channel unlocked.").queue();
            }
        }, (long) minutes * 60 * 1000);
    }

    public void unban() {

        EmbedGenerator embedConstructor = new EmbedGenerator();

        Member member = event.getMember();

        if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
            OptionMapping userOption = event.getOption("user");

            Long userId = userOption.getAsLong();
            UserSnowflake userSnowflake = User.fromId(userId);
            event.getGuild().unban(userSnowflake).queue(
                    success -> embedConstructor.success(event, "Successfully unbanned " + userSnowflake.getAsMention(), true),
                    error -> embedConstructor.failure(event, "Failed to unban " + userSnowflake.getAsMention() + ". Error: " + error.getMessage(), true)
            );
        } else {
            embedConstructor.noPermissions(event);
        }
    }

    public void report() {
        ConfigHandler configHandler = new ConfigHandler();

        String reportChannel = null;

        reportChannel = ConfigHandler.getServerConfig(event.getGuild().getId(), "Report_Notification_Channel");

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Report von " + event.getMember().getEffectiveName());

        String reportedUser = event.getOption("user").getAsMember().getAsMention();
        String reason = event.getOption("reason").getAsString();

        eb.addField("Report von", event.getMember().getAsMention(), true);
        eb.addField("Reported", reportedUser, true);
        eb.addField("Grund", reason, false);

        //TODO Button um direkt ein Ticket zu erstellen mit Berechtigungen für den Reporter

        eb.setColor(Color.MAGENTA);
        eb.setThumbnail(event.getMember().getEffectiveAvatarUrl());

        TextChannel channel = event.getGuild().getTextChannelById(reportChannel);

        String modRoleIDsString = ConfigHandler.getServerConfig(event.getGuild().getId(), "Moderation_Roles");
        String[] modRoleIDs = modRoleIDsString.split(",");

        for (String roleId : modRoleIDs) {
            Role role = event.getGuild().getRoleById(roleId);
            if (role != null) {
                channel.sendMessage(role.getAsMention()).queue();
            } else {
                // Handle the case where a role with the given ID doesn't exist
                logger.info("Role with ID " + roleId + " not found.");
            }
        }

        channel.sendMessageEmbeds(eb.build()).queue();

        eb.setTitle("Erfolgreich");
        eb.setDescription("Dein Report wurde gesendet.");
        eb.setColor(Color.GREEN);

        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
        logger.info(channel.getName());
    }

    public boolean isUrl(String url){
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

}
