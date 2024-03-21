package com.cubowbot.cubow.Handler;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cubowbot.cubow.Handler.TicketHandler;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.json.JSONArray;
import org.json.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Widget.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ButtonHandler extends ListenerAdapter{
    
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if (event.getComponentId().equals("ticket")) {

            ModalsHandler modals = new ModalsHandler();

            // Generate Modal
            modals.generateTicket(event, null);

        } else if (event.getComponentId().equals("closeTicket")) {
            String member = event.getMember().getAsMention();
            event.reply("Ticket wird von " + member + " geschlossen").queue();

            Channel channel = event.getChannel();
            channel.delete().queueAfter(10, TimeUnit.SECONDS);
        }

        else if (event.getComponentId().equals("closerequestyes")) {
                EmbedBuilder builder = new EmbedBuilder();

                builder.setDescription("Ticket wird geschlossen.");
                event.editMessageEmbeds(builder.build())
                        .setComponents()
                        .queue();


                event.getChannel().delete().queueAfter(3, TimeUnit.SECONDS);
        }

        else if (event.getComponentId().equals("closerequestno")) {
            System.out.println("Test");
            EmbedBuilder eb = new EmbedBuilder();

            MessageEmbed embed = event.getMessage().getEmbeds().get(0);
            eb.setTitle(embed.getTitle());
            eb.setDescription(embed.getDescription());
            eb.setColor(embed.getColor());

            eb.addField("Das Ticket wird nicht geschlossen.", "Bitte gebe uns weitere Informationen, wie wir dir helfen können.", false);
            event.editMessageEmbeds(eb.build())
                    .setComponents()
                    .queue();

            //event.getMessage().delete().queueAfter(10, TimeUnit.SECONDS);

        } else if (event.getComponentId().equals("claim")) {
            
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

                                            Button close = Button.primary("closeTicket", "Ticket schließen");
                                            Button unclaim = Button.primary("unclaim", "Unclaim Ticket");

                                            List<Button> newButtons = Arrays.asList(close, unclaim);
                                            event.getInteraction().getMessage().editMessageComponents().setActionRow(newButtons).queue();
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
                            event.getHook().editOriginal("Ticket already claimed").queue();

                            Button close = Button.primary("closeTicket", "Ticket schließen");
                            Button unclaim = Button.primary("unclaim", "Ticket claimed by " + member.getEffectiveName());

                            List<Button> newButtons = Arrays.asList(close, unclaim);
                            event.getInteraction().getMessage().editMessageComponents().setActionRow(newButtons).queue();
                        }
                    } catch (Exception e) {
                            event.getHook().editOriginal("An error occurred: " + e.getMessage()).queue();
                    }
                } else {
                    event.getHook().editOriginal("Du hast nicht die Berechtigungen, diesen Befehl auszuführen.").queue();
                }
        } else if (event.getComponentId().equals("unclaim")) {
            
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
                                            System.out.println("Ticket unclaimed by" + member.getAsMention());

                                            Button close = Button.primary("closeTicket", "Ticket schließen");
                                            Button claim = Button.primary("claim", "Claim Ticket");

                                            List<Button> newButtons = Arrays.asList(close, claim);
                                            event.getInteraction().getMessage().editMessageComponents().setActionRow(newButtons).queue();
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

                            Button close = Button.primary("closeTicket", "Ticket schließen");
                            Button unclaim = Button.primary("claim", "Claim Ticket");

                            List<Button> newButtons = Arrays.asList(close, unclaim);
                            event.getInteraction().getMessage().editMessageComponents().setActionRow(newButtons).queue();
                        }
                    } catch (Exception e) {
                            event.getHook().editOriginal("An error occurred: " + e.getMessage()).queue();
                    }
                } else {
                    event.getHook().editOriginal("Du hast nicht die Berechtigungen, diesen Befehl auszuführen.").queue();
                }
        } else if (event.getComponentId().equals("join")) {
            User user = event.getUser();
            String userId = user.getId();
            String userName = user.getName();

            // Create a JSONObject for the user
            JSONObject userJson = new JSONObject();
            userJson.put("id", userId);
            userJson.put("name", userName);

            // Read the existing file content and convert it to a JSONArray
            JSONArray usersArray;
            try {
                String content = new String(Files.readAllBytes(Paths.get("users.json")));
                usersArray = new JSONArray(content);
            } catch (IOException e) {
                // If there's an error reading the file or it doesn't exist, start a new JSONArray
                usersArray = new JSONArray();
            }

            // Add the new user to the JSONArray
            usersArray.put(userJson);

            // Convert the updated JSONArray to String
            String jsonString = usersArray.toString();

            // Write the updated JSON array back to the file
            try (FileWriter file = new FileWriter("users.json")) {
                file.write(jsonString);
                file.flush();
                event.reply("Du nimmst am Giveaway Teil!").setEphemeral(true).queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(event.getComponentId().equals("whitelistCheck")) {

        }
    }  
}
