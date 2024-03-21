package com.cubowbot.cubow.Listener;

import java.awt.Color;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.cubowbot.cubow.Handler.*;
import net.dv8tion.jda.api.entities.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.Widget.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import com.cubowbot.cubow.Generator.EmbedGenerator;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

            EmbedGenerator embedConstructor = new EmbedGenerator();
            System.out.println(event.getName());

            // Website
            if (event.getName().equals("website")) {
                event.reply("https://craftex.nimawoods.de").setEphemeral(true).queue();
            // Ping
            } else if (event.getName().equals("ping")) {
                event.reply("pong!").setEphemeral(true).queue();
            }

            // Moderation
            // Ban
             else if (event.getName().equals("ban")) {

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

            // timeout
            else if (event.getName().equals("timeout")) {
                Member member = event.getMember();

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

            // remove timeout
            else if (event.getName().equals("removetimeout")) {
                Member member = event.getMember();
                if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
                    Member user = Objects.requireNonNull(event.getOption("user")).getAsMember();
                    event.getGuild().removeTimeout(user);
                } else {
                    embedConstructor.noPermissions(event);
                }
            }

            // kick
            else if (event.getName().equals("kick")) {
                Member member = event.getMember();
                if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
                    User user = event.getOption("user").getAsUser();
                    event.getGuild().kick(user).queue(
                            success -> embedConstructor.success(event, "Successfully kicked " + user.getAsMention(), true),
                            error -> embedConstructor.failure(event, "Failed to kick " + user.getAsMention() + "\n" + error.getMessage(), true)
                    );
                } else {
                    embedConstructor.noPermissions(event);
                }
            }  else if (event.getName().equals("lockdownchannel")) {
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
                }, minutes * 60 * 1000); // Convert minutes to milliseconds
            }

            else if (event.getName().equals("avatar")) {
                Member member = event.getOption("member").getAsMember();
                String avatarUrl = member.getEffectiveAvatarUrl();
                EmbedBuilder eb = new EmbedBuilder();

                eb.setTitle(member.getUser().getName() + "'s Avatar");
                eb.setColor(Color.MAGENTA);
                eb.setDescription("[png](" +avatarUrl + ") [jpg](" + avatarUrl.replace(".png", ".jpg") + ") [webp](" + avatarUrl.replace(".png", ".webp") + ")");
                eb.setImage(avatarUrl + "?size=1024");
                event.replyEmbeds(eb.build()).queue();

                //TODO Extend Embed Constructor
            }

            // addEmoji
            else if (event.getName().equals("addemoji")) {
                Member member = event.getMember();
                if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
                    OptionMapping imageOption = event.getOption("image");
                    if (imageOption != null) {
                        Message.Attachment attachment = imageOption.getAsAttachment();
                        attachment.retrieveInputStream().thenAccept(inputStream -> {
                            Icon image = null;
                            try {
                                image = Icon.from(inputStream);
                                String name = event.getOption("name").getAsString();

                                event.getGuild().createEmoji(name, image).queue( emoji -> {
                                            event.reply(emoji.getAsMention()).queue();
                                        },
                                        error -> embedConstructor.failure(event, "Could not add emoji to Server!"+ "\n" + error.getMessage() + "\n Please note:\\n\" +\n" +
                                                        "\"File type: The image must be in JPEG, PNG, or GIF format\\n\" +\n" +
                                                        "\"\\n\" +\n" +
                                                        "\"File size: The image must be under 256KB in size\\n\" +\n" +
                                                        "\"\\n\" +\n" +
                                                        "\"Dimensions: For optimal resolution, you can upload custom emojis in sizes up to 128×128 pixels, but they will be resized to 32×32 pixels when uploaded to Discord\\n\" +\n"
                                                        , true)
                                );
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }).exceptionally(throwable -> {
                            // Handle any exceptions here
                            throwable.printStackTrace();
                            return null;
                        });
                    }
                } else {
                    embedConstructor.noPermissions(event);
                }
            } else if (event.getName().equals("removeemoji")) {
                String emoji = event.getOption("emoji").getAsString();
                String emojiID = emoji.replaceAll("<:.*:(\\d+)>", "$1");
                Objects.requireNonNull(event.getGuild()).getEmojiById(emojiID).delete().queue(
                        success -> embedConstructor.success(event, "Successfully removed emoji from Server!", true),
                        error -> embedConstructor.failure(event, "Could not remove emoji from Server!"+ "\n" + error.getMessage(), true)
                );
            }

            // commands
            if (event.getName().equals("commands") || event.getName().equals("help")) {
                CommandList(event, commandList -> {
                    // Sort the commandList array
                    String[] sortedCommands = commandList.toArray(new String[0]);
                    Arrays.sort(sortedCommands);

                    // Create a StringBuilder to hold the sorted commands
                    StringBuilder commandListString = new StringBuilder();

                    // Append sorted commands to the StringBuilder
                    for (String command : sortedCommands) {
                        commandListString.append(command).append("\n");
                    }
                    if (!commandListString.isEmpty()) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Command List", null);
                        eb.setColor(Color.GREEN);
                        eb.setDescription(commandListString.toString());
                        event.replyEmbeds(eb.build()).queue();
                        //TODO turnable Pages
                    } else {
                        embedConstructor.failure(event, "No commands found!", true);
                    }
                }, failure -> embedConstructor.failure(event, "Failed: " + failure, true)
                );
            }
            // unban
            else if (event.getName().equals("unban")) {
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

            // serverstats
            else if (event.getName().equals("serverstats")) {
                Guild guild = event.getGuild();

                String serverName = guild.getName();
                String serverId = guild.getId();
                Integer memberCount = guild.getMemberCount();
                TextChannel rulesChannel = guild.getRulesChannel();
                OffsetDateTime timeCreated = guild.getTimeCreated();

                String banner = guild.getBannerUrl();

                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Server Stats");
                eb.setDescription("**Server:** " + serverName +  //
                "\n**Server Id:** " + serverId + //
                "\n**Members:** " + memberCount + //
                "\n**Rules Channel** "+ rulesChannel + //
                "**Created:** " + timeCreated);
                eb.setImage(banner);
                eb.setColor(Color.MAGENTA);
                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
            }

            // Tickets
            // resend
            else if (event.getName().equals("sendpanel")) {
                TicketHandler ticketHandler = new TicketHandler();
                ticketHandler.sendDashboard(Objects.requireNonNull(event.getGuild()));

                event.reply("success").setEphemeral(true).queue();
            }

            // remove
            else if (event.getName().equals("remove")) {

                // Send "Bot is thinking..."
                event.deferReply().queue();

                String channelNameRemoveString = event.getChannel().getName();
                if (channelNameRemoveString.contains("ticket")) {
                    Member member = event.getOption("user").getAsMember();
                    TextChannel channel = event.getChannel().asTextChannel();
                    if (member != null) {
                        EnumSet<Permission> deny = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
                        channel.upsertPermissionOverride(member)
                               .setPermissions(null, deny)
                               .queue(
                                    success -> embedConstructor.success(event, "Successfully removed " + member.getAsMention() + " from Ticket!", true),
                                    failure -> embedConstructor.failure(event, "Failed to remove " + member.getAsMention() + " from Ticket!", true)
                                );
                    }
                }

            }

            else if (event.getName().equals("close")) {
                String member = event.getMember().getAsMention();
                event.reply("Ticket wird von " + member + " geschlossen").queue();
                event.getChannel().delete().queueAfter(5, TimeUnit.SECONDS);
            }

            // claim
            else if (event.getName().equals("claim")) {

                event.deferReply().queue();

                Member member = event.getMember();
                if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
                    try {
                        MessageChannelUnion channelUnion = event.getChannel();
                        TextChannel channel = (TextChannel) channelUnion;
                        Role role = event.getGuild().getRoleById("1079231987090469016");
                        EnumSet<Permission> perm = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
                        Long memberID = member.getIdLong();

                        net.dv8tion.jda.api.entities.PermissionOverride permissionOverride = channel.getPermissionOverride(role);
                        if (permissionOverride != null) {

                            System.out.println(member.getNickname() + " claimed " + channel.getName());

                            System.out.println("Adding member " + member.getNickname() + " to Channel" + channel);
                            channel.getManager().putMemberPermissionOverride(memberID, perm, null).queue(
                                success -> {
                                    System.out.println("Removing permissions for role " + role.getName() + " from Channel " + channel);
                                    channel.getManager().removePermissionOverride(role.getIdLong()).queue(
                                        successRole -> {
                                            EmbedBuilder eb = new EmbedBuilder();
                                            eb.setTitle("Success", null);
                                            eb.setColor(Color.GREEN);
                                            eb.setDescription("Ticket claimed by " + member.getAsMention());
                                            event.getHook().editOriginalEmbeds(eb.build()).queue();
                                        },
                                        failureRole -> {
                                            EmbedBuilder eb = new EmbedBuilder();
                                            eb.setTitle("Failure", null);
                                            eb.setColor(Color.RED);
                                            eb.setDescription("FAILED: " + failureRole);
                                            event.getHook().editOriginalEmbeds(eb.build()).queue();
                                        }
                                    );
                                },
                                failure -> event.getHook().editOriginal("FAILED: " + failure).queue()
                            );
                        } else {
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setTitle("Failure", null);
                            eb.setColor(Color.RED);
                            eb.setDescription("Ticket already claimed");
                            event.getHook().editOriginalEmbeds(eb.build()).queue();
                        }
                    } catch (Exception e) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Error Occurred", null);
                        eb.setColor(Color.RED);
                        eb.setDescription("An error occurred: " + e.getMessage());
                        event.getHook().editOriginalEmbeds(eb.build()).queue();
                    }
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Failure", null);
                        eb.setColor(Color.RED);
                        eb.setDescription("Du hast nicht die Berechtigungen diesen Befehl auszuführen");
                        event.getHook().editOriginalEmbeds(eb.build()).queue();
                }
            }

            else if (event.getName().equals("transfer")) {

                event.deferReply().queue();

                Member member = event.getMember();
                if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
                    try {
                        MessageChannelUnion channelUnion = event.getChannel();
                        TextChannel channel = (TextChannel) channelUnion;
                        Role role = event.getGuild().getRoleById("1079231987090469016");
                        EnumSet<Permission> perm = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
                        Member transMember = event.getOption("user").getAsMember();

                        net.dv8tion.jda.api.entities.PermissionOverride permissionOverride = channel.getPermissionOverride(role);
                        if (permissionOverride == null) {
                            System.out.println("Adding member " + transMember.getEffectiveName() + " to Channel" + channel);
                            channel.getManager().putMemberPermissionOverride(transMember.getIdLong(), perm, null).queue(
                                success -> {
                                    System.out.println("Removing permissions for Member " + member.getEffectiveName() + " from Channel " + channel);
                                    channel.getManager().removePermissionOverride(member.getIdLong()).queue(
                                        successAdd -> {
                                            EmbedBuilder eb = new EmbedBuilder();
                                            eb.setTitle("Ticket Transfer", null);
                                            eb.setColor(Color.GREEN);
                                            eb.setDescription("Ticket transferred to " + transMember.getAsMention());
                                            event.getHook().editOriginalEmbeds(eb.build()).queue();
                                        },
                                        failureAdd -> {
                                            EmbedBuilder eb = new EmbedBuilder();
                                            eb.setTitle("Ticket Transfer Failed", null);
                                            eb.setColor(Color.RED);
                                            eb.setDescription("FAILED: " + failureAdd);
                                            event.getHook().editOriginalEmbeds(eb.build()).queue();
                                        }
                                    );
                                },
                                failure -> {
                                            EmbedBuilder eb = new EmbedBuilder();
                                            eb.setTitle("Ticket Transfer Failed", null);
                                            eb.setColor(Color.RED);
                                            eb.setDescription("FAILED: " + failure);
                                            event.getHook().editOriginalEmbeds(eb.build()).queue();
                                        }
                            );
                        } else {
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setTitle("Ticket Transfer Failed", null);
                            eb.setColor(Color.RED);
                            eb.setDescription("Ticket not Claimed");
                            event.getHook().editOriginalEmbeds(eb.build()).queue();
                        }
                    } catch (Exception e) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Error Occurred", null);
                        eb.setColor(Color.RED);
                        eb.setDescription("An error occurred: " + e.getMessage());
                        event.getHook().editOriginalEmbeds(eb.build()).queue();
                    }
                }
            }

            // unclaim
            else if (event.getName().equals("unclaim")) {

                event.deferReply().queue();

                Member member = event.getMember();
                if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
                    try {
                        MessageChannelUnion channelUnion = event.getChannel();
                        TextChannel channel = (TextChannel) channelUnion;
                        Role role = event.getGuild().getRoleById("1079231987090469016");
                        EnumSet<Permission> perm = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
                        Long memberID = member.getIdLong();

                        net.dv8tion.jda.api.entities.PermissionOverride permissionOverride = channel.getPermissionOverride(role);
                        if (permissionOverride == null) {

                            System.out.println(member.getNickname() + " unclaimed " + channel.getName());
                            System.out.println("Adding member " + member.getNickname() + " to Channel" + channel);

                            channel.getManager().removePermissionOverride(memberID).queue(
                                success -> {
                                    System.out.println("Removing permissions for role " + role.getName() + " from Channel " + channel);
                                    channel.getManager().putRolePermissionOverride(role.getIdLong(), perm, null).queue(
                                        successRole -> {
                                            EmbedBuilder eb = new EmbedBuilder();
                                            eb.setTitle("Ticket Status", null);
                                            eb.setColor(Color.GREEN);
                                            eb.setDescription("Ticket unclaimed by " + member.getAsMention());
                                            event.getHook().editOriginalEmbeds(eb.build()).queue();
                                            System.out.println("Ticket unclaimed by " + member.getAsMention());
                                        },
                                        failureRole -> {
                                            EmbedBuilder eb = new EmbedBuilder();
                                            eb.setTitle("Error", null);
                                            eb.setColor(Color.RED);
                                            eb.setDescription("FAILED: " + failureRole);
                                            event.getHook().editOriginalEmbeds(eb.build()).queue();
                                        }
                                    );
                                },
                                failure -> event.getHook().editOriginal("FAILED: " + failure).queue()
                            );
                        } else {
                            event.getHook().editOriginal("Ticket not claimed").queue();
                        }
                    } catch (Exception e) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Error Occurred", null);
                        eb.setColor(Color.RED);
                        eb.setDescription("An error occurred: " + e.getMessage());
                        event.getHook().editOriginalEmbeds(eb.build()).queue();
                    }
                } else {
                    event.getHook().editOriginal("Du hast nicht die Berechtigungen, diesen Befehl auszuführen.").queue();
                }
            }

            // add
            else if (event.getName().equals("add")) {

                // Send "Bot is thinking..."
                event.deferReply().queue();

                String channelNameAddString = event.getChannel().getName();
                if (channelNameAddString.contains("ticket")) {

                    EnumSet<Permission> perm = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND);
                    Member member = event.getOption("user").getAsMember();
                    TextChannel channel = event.getChannel().asTextChannel();

                    if (member != null) {
                        System.out.println("Adding member " + member.getNickname() + " to Channel" + channel);
                            channel.getManager().putMemberPermissionOverride(member.getIdLong(), perm, null).queue(
                                success -> {
                                        EmbedBuilder successEmbed = new EmbedBuilder();
                                        successEmbed.setTitle("Success");
                                        successEmbed.setDescription("Added " + member.getAsMention() + " to Ticket!");
                                        successEmbed.setColor(Color.GREEN);
                                        event.getHook().sendMessageEmbeds(successEmbed.build()).queue();
                                    },
                                failure -> {
                                        EmbedBuilder failureEmbed = new EmbedBuilder();
                                        failureEmbed.setTitle("Failure");
                                        failureEmbed.setDescription("Failed to add " + member.getAsMention() + " to Ticket!");
                                        failureEmbed.setColor(Color.RED);
                                        event.getHook().sendMessageEmbeds(failureEmbed.build()).queue();
                                    }
                            );
                    }
                } else {
                    event.reply("Not a Ticket Channel").queue();;
                }

            } else if (event.getName().equals("ticket")) {
                ModalsHandler modals = new ModalsHandler();
                modals.generateTicket(null ,event);
            }
            // closerequest
            if (event.getName().equals("closerequest")) {
                Guild server = event.getGuild();
                TicketHandler ticketHandler = new TicketHandler();

                MessageChannelUnion messageChannelUnion = event.getChannel();
                if (messageChannelUnion instanceof TextChannel) {
                    TextChannel textChannel = (TextChannel) messageChannelUnion;
                    ticketHandler.sendCloseRequest(textChannel, event);
                }
            }

            // giveaway
            // creategiveaway
            else if (event.getName().equals("creategiveaway")) {

                String gewinn = event.getOption("gewinn").getAsString();
                TextChannel channel = null;
                int numberOfWinners = event.getOption("winner").getAsInt();

                int seconds = event.getOption("seconds") != null ? event.getOption("seconds").getAsInt() : 0;
                int minutes = event.getOption("minutes") != null ? event.getOption("minutes").getAsInt() : 0;
                int hours = event.getOption("hours") != null ? event.getOption("hours").getAsInt() : 0;
                int days = event.getOption("days") != null ? event.getOption("days").getAsInt() : 0;

                if (seconds > 0 && minutes > 0 && hours > 0 && days > 0) {
                    event.reply("Please provide a end time for the giveaway").complete();
                }

                // Calculate the delay in milliseconds for the timer
                long delay = seconds * 1000L + minutes * 60000L + hours * 3600000L + days * 86400000L;

                // Calculate the total seconds for the timestamp
                long totalSeconds = seconds + (minutes * 60) + (hours * 3600) + (days * 86400);

                // Get the current time and add the total seconds to get the future timestamp
                long currentUnixTime = Instant.now().getEpochSecond();
                long futureUnixTime = currentUnixTime + totalSeconds;

                // Format the timestamp for Discord
                String time = "<t:" + futureUnixTime + ":R>";

                // send embed and add Emoji
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle(gewinn, null);
                eb.setColor(Color.MAGENTA);
                eb.setFooter("Craftex", event.getGuild().getIconUrl());
                eb.setDescription("Klicke auf den Button, um Teilzunehmen.\n\n" + //
                "**Endet**\n" + //
                time
                );
                eb.setImage("https://cdn.discordapp.com/attachments/1116302198113062963/1190988512112156755/giveaway_-_Erstellt_mit_PosterMyWall.jpg?ex=65a3cd7c&is=6591587c&hm=c7050928504b07a308840abf2062f5e60b5dd2ba2ebc63c4602734359f0872fd&");

                Button button = Button.primary("join", "🎉 Teilnehmen");

                if (event.getOption("channel") == null) {
                    MessageChannelUnion messageChannelUnion = event.getChannel();
                    if (messageChannelUnion instanceof TextChannel) {
                        channel = (TextChannel) messageChannelUnion;
                    }

                    event.reply("✅").queue(message -> {
                        message.deleteOriginal().queueAfter(1, TimeUnit.SECONDS);
                    });
                } else {
                    // get Channel Union as Text Channel
                    GuildChannelUnion channelUnion = event.getOption("channel").getAsChannel();
                    channel = (TextChannel) channelUnion;

                    event.reply("Created giveaway in channel " + channel.getAsMention()).queue(message -> {
                        message.deleteOriginal().queueAfter(3, TimeUnit.SECONDS);
                    });
                }

                if (channel != null) {
                    channel.sendMessageEmbeds(eb.build()).setActionRow(button).queue(message -> {
                        // Schedule the update of the embed after the delay
                        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                        scheduler.schedule(() -> {

                            // pick random winner
                            GiveawayHandler giveawayHandler = new GiveawayHandler();
                            String winnerID = giveawayHandler.pickWinner();
                            Member winner = message.getGuild().getMemberById(winnerID);

                            // Update the embed with the winner information
                            eb.setTitle("Giveaway vorbei: " + gewinn);
                            eb.setDescription("Klicke auf den Button, um Teilzunehmen.\n\n" + //
                            "**Winner**\n" + //
                            winner.getAsMention()
                            );
                            message.editMessageEmbeds(eb.build()).queue();
                            message.editMessageComponents().queue();

                            // Ping Member
                            message.getChannel().sendMessage(winner.getAsMention()).queue(pingMessage -> {
                                pingMessage.delete().queueAfter(1, TimeUnit.SECONDS);
                            });

                            // Shut down the scheduler to free resources
                            scheduler.shutdown();
                        }, delay, TimeUnit.MILLISECONDS);
                    }

                    );
                } else {
                    event.reply("Channel not found").setEphemeral(true).queue();
                }

            }

            // Embedbuilder
            // create
            else if (event.getName().equals("embedcreate")) {
                Member member = event.getMember();

                if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {

                    // Send "Bot is thinking..."
                    event.deferReply().setEphemeral(true).queue();

                    String title = event.getOption("title").getAsString();
                    String titlelink = event.getOption("titlelink") != null ? event.getOption("titlelink").getAsString() : null;
                    String description = event.getOption("description").getAsString();
                    Attachment image = event.getOption("image") != null ? event.getOption("image").getAsAttachment() : null;
                    String footer = event.getOption("footer") != null ? event.getOption("footer").getAsString() : null;
                    Attachment footerimage = event.getOption("footerimage") != null ? event.getOption("footerimage").getAsAttachment() : null;
                    String author = event.getOption("author") != null ? event.getOption("author").getAsString() : null;
                    Attachment authorimage = event.getOption("authorimage") != null ? event.getOption("authorimage").getAsAttachment() : null;
                    Attachment thumbnail = event.getOption("thumbnail") != null ? event.getOption("thumbnail").getAsAttachment() : null;

                    OptionMapping colorOption = event.getOption("color");
                    String colorString = colorOption != null ? colorOption.getAsString() : null;

                    Color awtColor = null;

                    try {
                        // Read the JSON file
                        //Todo make colors,json to dependency
                        String json = new String(Files.readAllBytes(Paths.get("colors.json")));

                        // Parse the JSON file
                        JsonArray colorsArray = JsonParser.parseString(json).getAsJsonArray();

                        // Create a map to store the color names and their hex values
                        Map<String, String> colorMap = new HashMap<>();

                        // Iterate over the JSON array and add each color name and hex value to the map
                        for (JsonElement colorElement : colorsArray) {
                            JsonObject colorObject = colorElement.getAsJsonObject();
                            String colorName = colorObject.get("color").getAsString();
                            String colorHex = colorObject.get("hex").getAsString();
                            colorMap.put(colorName, colorHex);
                        }

                        String colorNameToSearch = colorString;
                        String relatedHex = colorMap.get(colorNameToSearch);

                        if (relatedHex != null) {
                            awtColor = Color.decode(relatedHex);
                        } else {
                            System.out.println("Color not found in the JSON file.");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    GuildChannelUnion guildChannelUnion = event.getOption("channel") != null ? event.getOption("channel").getAsChannel() : null;
                    TextChannel channel = (TextChannel) guildChannelUnion;

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle(title, titlelink);
                    eb.setDescription(description);
                    eb.setImage(image != null ? image.getUrl() : null);
                    eb.setFooter(footer, footerimage != null ? footerimage.getUrl() : null);
                    eb.setAuthor(author, authorimage != null ? authorimage.getUrl() : null);
                    eb.setColor(awtColor);
                    eb.setThumbnail(thumbnail != null ? thumbnail.getUrl() : null);

                    if (channel != null) {
                        channel.sendMessageEmbeds(eb.build()).queue( message -> {
                            event.getHook().editOriginal("Embed created in channel " + message.getJumpUrl()).queue(reply -> {
                            reply.delete().queueAfter(5, TimeUnit.SECONDS);
                            });
                        });
                    } else {
                        event.getChannel().sendMessageEmbeds(eb.build()).queue();
                        event.getHook().editOriginal("Embed created").queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                    }
                } else {
                    event.reply("You dont have the permissions to use this Command.").setEphemeral(true).queue();
                }
                // edit
            } else if (event.getName().equals("embededit")) {
                Member member = event.getMember();

                if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {

                    // Send "Bot is thinking..."
                    event.deferReply().setEphemeral(true).queue();

                    String title = event.getOption("title") != null ? event.getOption("title").getAsString() : null;
                    String titlelink = event.getOption("titlelink") != null ? event.getOption("titlelink").getAsString() : null;
                    String description = event.getOption("description") != null ? event.getOption("description").getAsString() : null;
                    Attachment image = event.getOption("image") != null ? event.getOption("image").getAsAttachment() : null;
                    String footer = event.getOption("footer") != null ? event.getOption("footer").getAsString() : null;
                    Attachment footerimage = event.getOption("footerimage") != null ? event.getOption("footerimage").getAsAttachment() : null;
                    String author = event.getOption("author") != null ? event.getOption("author").getAsString() : null;
                    Attachment authorimage = event.getOption("authorimage") != null ? event.getOption("authorimage").getAsAttachment() : null;
                    Attachment thumbnail = event.getOption("thumbnail") != null ? event.getOption("thumbnail").getAsAttachment() : null;
                    String messageID = event.getOption("messageid").getAsString();

                    GuildChannelUnion guildChannelUnion = event.getOption("channel") != null ? event.getOption("channel").getAsChannel() : null;
                    TextChannel channel = (TextChannel) guildChannelUnion;

                    channel.retrieveMessageById(messageID).queue(message -> {

                        List<MessageEmbed> embeds = message.getEmbeds();

                        OptionMapping colorOption = event.getOption("color");
                        String colorString = colorOption != null ? colorOption.getAsString() : null;

                        Color awtColor = null;

                        if (colorString != null) {

                            try {
                                // Read the JSON file
                                String json = new String(Files.readAllBytes(Paths.get("colors.json")));

                                // Parse the JSON file
                                JsonArray colorsArray = JsonParser.parseString(json).getAsJsonArray();

                                // Create a map to store the color names and their hex values
                                Map<String, String> colorMap = new HashMap<>();

                                // Iterate over the JSON array and add each color name and hex value to the map
                                for (JsonElement colorElement : colorsArray) {
                                    JsonObject colorObject = colorElement.getAsJsonObject();
                                    String colorName = colorObject.get("color").getAsString();
                                    String colorHex = colorObject.get("hex").getAsString();
                                    colorMap.put(colorName, colorHex);
                                }

                                String colorNameToSearch = colorString;
                                String relatedHex = colorMap.get(colorNameToSearch);

                                if (relatedHex != null) {
                                    awtColor = Color.decode(relatedHex);
                                } else {
                                    System.out.println("Color not found in the JSON file.");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        // Set old values to only change the given ones

                        EmbedBuilder eb = new EmbedBuilder();
                        MessageEmbed embed = embeds.get(0);

                        String titleTextOld = embed.getTitle();
                        String titlelinkOld = embed.getUrl();

                        if (titleTextOld != null && titlelinkOld != null) {
                            eb.setTitle(titleTextOld, titlelinkOld);
                        } else if (titleTextOld != null) {
                            eb.setTitle(titleTextOld);
                        }

                        String descriptionOld = embed.getDescription();
                        if (descriptionOld != null) {
                            eb.setDescription(descriptionOld);
                        }

                        MessageEmbed.ImageInfo imageInfoOld = embed.getImage();
                        if (imageInfoOld != null) {
                            String imageOld = imageInfoOld.getUrl();
                            if (imageOld != null) {
                                eb.setImage(imageOld);
                            }
                        }

                        MessageEmbed.Footer footerOld = embed.getFooter();
                        String footerTextOld = null;
                        String footerImageOld = null;

                        if (footerOld != null) {
                            footerTextOld = footerOld.getText();
                            footerImageOld = footerOld.getIconUrl();

                            if (footerTextOld != null && footerImageOld != null) {
                                eb.setFooter(footerTextOld, footerImageOld);
                            } else if (footerTextOld != null) {
                                eb.setFooter(footerTextOld);
                            }
                        }

                        MessageEmbed.AuthorInfo authorOld = embed.getAuthor();
                        String authorNameOld = null;
                        String authorImageOld = null;

                        if (authorOld != null) {
                            authorNameOld = authorOld.getName();
                            authorImageOld = authorOld.getIconUrl();

                            if (authorNameOld != null && authorImageOld != null) {
                                eb.setAuthor(authorNameOld, null, authorImageOld);
                            } else if (authorNameOld != null) {
                                eb.setAuthor(authorNameOld, null, null);
                            }
                        }

                        java.awt.Color colorOld = embed.getColor();
                        if (colorOld != null) {
                            eb.setColor(colorOld);
                        }

                        MessageEmbed.Thumbnail thumbnailOld = embed.getThumbnail();
                        if (thumbnailOld != null) {
                            eb.setThumbnail(thumbnailOld.getUrl());
                        }

                        // Set new values

                        if (title != null && titlelink != null) {
                            eb.setTitle(title, titlelink);
                        } else if (title != null) {
                            eb.setTitle(title);
                        } else if (titlelink != null) {
                            String titleText = embed.getTitle();
                            eb.setTitle(titleText, titlelink);
                        }

                        if (description != null) {
                            eb.setDescription(description);
                        }

                        if (image != null) {
                            eb.setImage(image.getUrl());
                        }

                        if (footer != null && footerimage != null) {
                            eb.setFooter(footer, footerimage.getUrl());
                        } else if (footer != null) {
                            eb.setFooter(footer);
                        } else if (footerimage != null) {
                            String footerText = embed.getFooter().getText();
                            eb.setFooter(footerText, footerimage.getUrl());
                        }

                        if (author != null && authorimage != null) {
                            eb.setAuthor(author, authorimage.getUrl());
                        } else if (author != null) {
                            eb.setAuthor(author);
                        } else if (authorimage != null) {
                            String authorName = embed.getAuthor().getName();
                            eb.setFooter(authorName, authorimage.getUrl());
                        }

                        if (awtColor != null) {
                            eb.setColor(awtColor);
                        }

                        if (thumbnail != null) {
                            eb.setThumbnail(thumbnail.getUrl());
                        }

                        channel.editMessageEmbedsById(messageID, eb.build()).queue( embedEdit -> {
                            event.getHook().editOriginal("Embed " + embedEdit.getJumpUrl() + " edited").queue(reply -> {
                            reply.delete().queueAfter(5, TimeUnit.SECONDS);
                            });
                        });
                    }, failure -> {
                        // This code will be executed if the message could not be retrieved
                        event.reply("Message not found").setEphemeral(true).queue();
                    });
                } else {
                    event.reply("You dont have the permissions to use this Command.").setEphemeral(true).queue();
                }
            } else if (event.getName().equals("embedaddfield")) {
                Member member = event.getMember();
                try {
                    if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
                        String messageID = event.getOption("messageid").getAsString();
                        String fieldtitle = event.getOption("fieldtitle").getAsString();
                        String fielddescription = event.getOption("fielddescription").getAsString();
                        Boolean inline = event.getOption("inline").getAsBoolean();

                        MessageChannel channel = null;
                        GuildChannelUnion guildChannelUnion = event.getOption("channel") != null ? event.getOption("channel").getAsChannel() : null;

                        if (guildChannelUnion instanceof TextChannel) {
                            channel = (TextChannel) guildChannelUnion;
                        } else if (guildChannelUnion instanceof NewsChannel) {
                            channel = (NewsChannel) guildChannelUnion;
                        } else if (guildChannelUnion instanceof VoiceChannel) {
                            event.reply("Please select a Text Channel").setEphemeral(true).queue();
                        }

                        channel.retrieveMessageById(messageID).queue(message -> {

                            List<MessageEmbed> embeds = message.getEmbeds();

                            EmbedBuilder eb = new EmbedBuilder();
                            MessageEmbed embed = embeds.get(0);

                            String titleTextOld = embed.getTitle();
                            String titlelinkOld = embed.getUrl();

                            if (titleTextOld != null && titlelinkOld != null) {
                                eb.setTitle(titleTextOld, titlelinkOld);
                            } else if (titleTextOld != null) {
                                eb.setTitle(titleTextOld);
                            } else if (titlelinkOld != null) {
                                eb.setTitle(titleTextOld, titlelinkOld);
                            }

                            String descriptionOld = embed.getDescription();
                            eb.setDescription(descriptionOld);

                            String imageOld = null;

                            if (embed.getImage() != null) {
                                imageOld = embed.getImage().getUrl();
                                eb.setImage(imageOld);
                            }

                            MessageEmbed.Footer footerOld = embed.getFooter();
                            String footerTextOld = null;
                            String footerImageOld = null;

                            if (footerOld != null) {
                                footerTextOld = footerOld.getText();
                                footerImageOld = footerOld.getIconUrl();


                                if (footerTextOld != null && footerImageOld != null) {
                                    eb.setFooter(footerTextOld, footerImageOld);
                                } else if (footerTextOld != null) {
                                    eb.setFooter(footerTextOld);
                                } else if (footerImageOld != null) {
                                    eb.setFooter(footerTextOld, footerImageOld);
                                }
                            }

                            MessageEmbed.AuthorInfo AuthorOld = embed.getAuthor();
                            String authorNameOld = null;
                            String authorImageOld = null;

                            if (AuthorOld != null) {
                                authorNameOld = AuthorOld.getName();
                                authorImageOld = AuthorOld.getIconUrl();


                                if (authorNameOld != null && authorImageOld != null) {
                                    eb.setFooter(authorNameOld, authorImageOld);
                                } else if (authorNameOld != null) {
                                    eb.setFooter(authorNameOld);
                                } else if (authorImageOld != null) {
                                    eb.setFooter(authorNameOld, authorImageOld);
                                }
                            }

                            java.awt.Color colorOld = embed.getColor();
                            eb.setColor(colorOld);

                            MessageEmbed.Thumbnail thumbnailOld = embed.getThumbnail();
                            if (thumbnailOld != null) {
                                eb.setThumbnail(thumbnailOld.getUrl());
                            }

                            // get all existing fields
                            List<MessageEmbed.Field> fieldsOld = embed.getFields();
                            if (fieldsOld != null) {
                                for (MessageEmbed.Field field : fieldsOld) {
                                    eb.addField(field);
                                }
                            }

                            eb.addField(fieldtitle, fielddescription, inline);
                            message.editMessageEmbeds(eb.build()).queue(edit -> {
                                event.reply("Added Field to Embed").setEphemeral(true).queue();
                            });
                        });
                    } else {
                        event.reply("You dont have the permissions to use this Command.").setEphemeral(true).queue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (event.getName().equals("startserver")) {

                event.deferReply().queue();

                Member member = event.getMember();
                if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {

                    ProcessBuilder screencheck = new ProcessBuilder("if screen -list | grep -q \"testserver\"; then screen -S testserver -X stuff \"uar now 10 Discord Bot Start Server Command started an automatic restart$(printf '\\r')\"; fi");
                    ProcessBuilder serverstart = new ProcessBuilder("sh", "-c", "cd /home/testserver && screen -L -dmS testserver java -Xmx4G -jar spigot-1.20.2.jar --nogui");

                    try {
                        screencheck.start();

                        Thread.sleep(10000);

                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {

                        serverstart.start();
                        Thread.sleep(30000);

                        String logfile = "/home/testserver/logs/latest.log";
                        File file = new File(logfile);
                        if (file.exists()) {
                            try {
                                String content = new String(Files.readAllBytes(file.toPath()));
                                event.getHook().editOriginal("Server Konsole gestartet unter http://5.45.109.197:7868\n\n Server Konsole:\n" + "```" + content + "```").queue();
                            } catch (IOException e) {
                                event.getHook().editOriginal("Error: " + e.getMessage()).queue();
                            }
                        } else {
                            event.getHook().editOriginal("Server Konsole gestartet unter http://5.45.109.197:7868\n\n latest.log nicht gefunden").queue();
                        }
                    } catch (IOException | InterruptedException e) {
                        event.getHook().editOriginal("Error: " + e).queue();
                    }
                } else {
                    event.getHook().editOriginal("Du hast nicht die Berechtigungen, um diesen Befehl auszuführen.").queue();
                }
            }

            // Report

            else if (event.getName().equals("report")) {
                ConfigHandler configHandler = new ConfigHandler();

                String reportChannel = null;

                reportChannel = configHandler.getServerConfig(event.getGuild().getId(), "Report_Notification_Channel");

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
                RoleHandler roleHandler = new RoleHandler();

                String modRoleID = null;

                modRoleID = roleHandler.getModRole(event.getGuild());

                channel.sendMessage(event.getGuild().getRoleById(modRoleID).getAsMention()).queue();
                channel.sendMessageEmbeds(eb.build()).queue();

                eb.setTitle("Erfolgreich");
                eb.setDescription("Dein Report wurde gesendet.");
                eb.setColor(Color.GREEN);

                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                System.out.println(channel.getName());
            }


           else if (event.getName().equals("chatgpt")) {

                Member member = event.getMember();
                if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
                    event.deferReply().queue();

                    ChatGPTHandler chatGPTHandler = new ChatGPTHandler();

                    OptionMapping prompt = event.getOption("prompt");

                    assert prompt != null;

                    String message = prompt.getAsString();
                    String response = chatGPTHandler.generateText(message, event.getGuild());

                    System.out.println("ChatGPT response: " + response);

                    event.getHook().editOriginal(response).queue();
                } else {
                    EmbedGenerator embedGenerator = new EmbedGenerator();
                    embedGenerator.noPermissions(event);
                }
            }

            else if (event.getName().equals("dall-e")) {
                Member member = event.getMember();
                if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
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


    public void CommandList(SlashCommandInteractionEvent event, Consumer<List<String>> successCallback, Consumer<Throwable> failureCallback) {
        Guild server = event.getGuild();
        List<String> commandList = new ArrayList<>();

        server.retrieveCommands().queue(commands -> {
            for (Command command : commands) {
                commandList.add("/" + command.getName());
            }
            successCallback.accept(commandList);
        }, failureCallback);
    }

    public static EmbedBuilder createEmbed(String title, String description, Color color) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(color);
        return embedBuilder;
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

