package com.cubowbot.cubow.listener;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cubowbot.cubow.handler.*;
import net.dv8tion.jda.api.entities.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.Widget.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import com.cubowbot.cubow.generator.EmbedGenerator;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        System.out.println("\nExecuting Slash Command " + event.getName()
                + " for user " + event.getMember().getEffectiveName()
                + " on server " + event.getGuild().getName());

        // Load Handler as Objects
        TextResponseHandler textResponseHandler = new TextResponseHandler(event);
        ModerationHandler moderationHandler = new ModerationHandler(event);
        MiscHandler miscHandler = new MiscHandler(event);
        CommandHandler commandHandler = new CommandHandler(event);
        TicketHandler ticketHandler = new TicketHandler(event);
        GiveawayHandler giveawayHandler = new GiveawayHandler(event);
        EmbedHandler embedHandler = new EmbedHandler(event);
        AiCommandHandler aiCommandHandler = new AiCommandHandler(event);

        ModalsHandler modals = new ModalsHandler();

        // Text Response
        switch (event.getName()) {
            case "website":
                textResponseHandler.getWebsite();
                break;
            case "ping":
                textResponseHandler.sendPing();
                break;
        }

        // Moderation
        switch (event.getName()) {
            case "ban":
                moderationHandler.ban();
                break;
            case "unban":
                moderationHandler.unban();
                break;
            case "timeout":
                moderationHandler.timeout();
                break;
            case "removetimeout":
                moderationHandler.removeTimeout();
                break;
            case "kick":
                moderationHandler.kick();
                break;
            case "report":
                moderationHandler.report();
                break;
        }

        //misc
        switch (event.getName()) {
            case "avatar":
                miscHandler.avatar();
                break;
            case "addemoji":
                miscHandler.addemoji();
                break;
            case "removeemoji":
                miscHandler.removeemoji();
                break;
            case "serverstats":
                miscHandler.serverstats();
                break;
        }

        // Help Commands Handler
        switch (event.getName()) {
            case "commands", "help":
                commandHandler.help();
                break;
        }

        // Ticket Commands Handler
        switch (event.getName()) {
            case "sendpanel":
                ticketHandler.sendDashboard(event.getGuild());
                break;
            case "add":
                ticketHandler.add();
                break;
            case "remove":
                ticketHandler.remove();
                break;
            case "ticket":
                modals.generateTicket(null, event);
                break;
            case "close":
                ticketHandler.close();
                break;
            case "claim":
                ticketHandler.claim();
                break;
            case "transfer":
                ticketHandler.transfer();
                break;
            case "unclaim":
                ticketHandler.unclaim();
                break;
            case "closerequest":
                ticketHandler.closerequest();
                break;
        }

        // giveaway
        switch(event.getName()) {
            case "creategiveaway":
                giveawayHandler.creategiveaway();
                break;
        }

        // embeds,
        switch(event.getName()) {
            case "embedcreate":
                embedHandler.embedcreate();
                break;
            case "embededit":
                embedHandler.embededit();
                break;
            case "embedaddfield":
                embedHandler.embedaddfield();
                break;

        }

        // AI Handler
        switch (event.getName()) {
            case "chatgpt":
                aiCommandHandler.chatgpt();
                break;
            case "dall-e":
                aiCommandHandler.dalle();
                break;
        }
    }
}

