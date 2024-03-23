package com.cubowbot.cubow.listener;

import com.cubowbot.cubow.handler.*;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        System.out.println("\nExecuting Slash Command " + event.getName()
                + " for user " + event.getMember().getEffectiveName()
                + " on server " + event.getGuild().getName());

        // Load Handler
        TextResponseHandler textResponseHandler = new TextResponseHandler(event);
        ModerationHandler moderationHandler = new ModerationHandler(event);
        MiscHandler miscHandler = new MiscHandler(event);
        CommandHandler commandHandler = new CommandHandler(event);
        TicketHandler ticketHandler = new TicketHandler(event);
        GiveawayHandler giveawayHandler = new GiveawayHandler(event);
        EmbedHandler embedHandler = new EmbedHandler(event);
        AICommandHandler aiCommandHandler = new AICommandHandler(event);
        FeedbackHandler feedbackHandler = new FeedbackHandler(event);

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

        // Feedback Commands
        switch (event.getName()) {
            case "bug":
                feedbackHandler.bug();
                break;
            case "suggest":
                feedbackHandler.suggest();
                break;

        }
    }
}

