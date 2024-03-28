package com.cubowbot.cubow.listener;

import com.cubowbot.cubow.CubowApplication;
import com.cubowbot.cubow.handler.*;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlashCommandListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SlashCommandListener.class);

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        logger.info("\nExecuting Slash Command \"" + event.getName()
                + "\" for subcommand \"" + event.getSubcommandName()
                + "\" for user \"" + event.getMember().getEffectiveName()
                + "\" on server \"" + event.getGuild().getName() + "\"");

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
        SubcommandListener subcommandListener = new SubcommandListener();

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

        // setting
        switch (event.getName()) {
            case "options":
                subcommandListener.options(event);
            case "ticketoptions":
                subcommandListener.ticketoptions(event);
            case "notificationoptions":
                subcommandListener.notificationoptions(event);
        }

        if (event.getName().equals("option")) {
            subcommandListener.options(event);
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
            case "coinflip":
                miscHandler.coinflip();
        }

        // Help Commands Handler
        switch (event.getName()) {
            case "commands", "help":
                commandHandler.help();
                break;
        }

        // Ticket Commands Handler
        if(event.getName().equals("ticket")) {
            subcommandListener.Ticket(event);
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

        // Test Commands
        // Deactivate if not in use
        if(true) {
            switch (event.getName()) {
                case "sendwelcometest":
                    WelcomeHandler welcomeHandler = new WelcomeHandler();
                    welcomeHandler.sendWelcomeTest(event);
            }
        }
    }
}

