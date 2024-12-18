package com.cubowbot.cubow.listener;

import com.cubowbot.cubow.handler.discord.*;
import com.cubowbot.cubow.handler.discord.music.MusicHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlashCommandListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SlashCommandListener.class);

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        logger.info("\nExecuting Slash Command \"" + event.getName()
                + "\" with subcommand \"" + event.getSubcommandName()
                + "\" for user \"" + event.getMember().getEffectiveName()
                + "\" on server \"" + event.getGuild().getName() + "\"");

        // Load Handler
        TextResponseHandler textResponseHandler = new TextResponseHandler(event);
        ModerationHandler moderationHandler = new ModerationHandler(event);
        MiscHandler miscHandler = new MiscHandler(event);
        CommandHandler commandHandler = new CommandHandler(event);
        GiveawayHandler giveawayHandler = new GiveawayHandler(event);
        EmbedHandler embedHandler = new EmbedHandler(event);
        AICommandHandler aiCommandHandler = new AICommandHandler(event);
        FeedbackHandler feedbackHandler = new FeedbackHandler(event);
        SubcommandListener subcommandListener = new SubcommandListener();
        MusicHandler musicHandler = new MusicHandler(event);
        ConfigHandler configHandler = new ConfigHandler();

        ModalsHandler modals = new ModalsHandler();

        // Text Response
        switch (event.getName()) {
            case "website":
                textResponseHandler.getWebsite();
                break;
            case "ping":
                textResponseHandler.sendPing();
                break;

            // Moderation
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

            // settings
            /* case "options":
                configHandler.saveOption(event);
                break;
            case "ticketoptions":
                configHandler.saveOption(event);
                break;
            case "notificationoptions":
                configHandler.saveOption(event);
                break; */

            //misc
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
                break;

            // Help Commands Handler
            case "commands":
                commandHandler.help();
                break;

            case "help":
                commandHandler.help();
                break;

            // Ticket Commands Handler
            case "ticket":
                subcommandListener.Ticket(event);
                break;

            // giveaway
            case "creategiveaway":
                giveawayHandler.creategiveaway();
                break;

            // embeds
            case "embedcreate":
                embedHandler.embedcreate();
                break;
            case "embededit":
                embedHandler.embededit();
                break;
            case "embedaddfield":
                embedHandler.embedaddfield();
                break;

            // AI Handler
            case "chatgpt":
                aiCommandHandler.chatgpt();
                break;
            case "dall-e":
                aiCommandHandler.dalle();
                break;

            // Feedback Commands
            case "bug":
                feedbackHandler.bug();
                break;
            case "suggest":
                feedbackHandler.suggest();
                break;

            // Music
            case "play":
                musicHandler.play();
                break;

            // Voice Channel
            case "join":
                musicHandler.join();
            case "leave":
                musicHandler.leave();
        }
    }
}

