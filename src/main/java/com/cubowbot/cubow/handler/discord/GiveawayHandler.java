package com.cubowbot.cubow.handler.discord;

import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GiveawayHandler {

    private final SlashCommandInteractionEvent event;
    private static final Logger logger = LoggerFactory.getLogger(GiveawayHandler.class);

    public GiveawayHandler(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void creategiveaway() {
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
        long totalSeconds = seconds + (minutes * 60L) + (hours * 3600L) + (days * 86400L);

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
        eb.setImage("https://t3.ftcdn.net/jpg/03/64/44/62/360_F_364446273_s5hEzzJtTDsi2vyOmWa51G4VsIRT6Ckp.jpg");

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
                            String winnerID = pickWinner();
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

    // ----------------------------------------------

    public String pickWinner() {
        String winnerId = null;
        try {
            String content = new String(Files.readAllBytes(Paths.get("users.json")));
            JSONArray users = new JSONArray(content);
        
            int randomIndex = new Random().nextInt(users.length());
            JSONObject winner = users.getJSONObject(randomIndex);
        
            winnerId = winner.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            java.nio.file.Path filePath = Paths.get("users.json");
            boolean isDeleted = Files.deleteIfExists(filePath);
            if (isDeleted) {
                logger.info("File deleted successfully");
            } else {
                logger.info("File does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return winnerId;
    }
}
