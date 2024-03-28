package com.cubowbot.cubow.listener;

import com.cubowbot.cubow.handler.ConfigHandler;
import com.cubowbot.cubow.handler.ModalsHandler;
import com.cubowbot.cubow.handler.TicketHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class SubcommandListener {

    ConfigHandler configHandler = new ConfigHandler();

    public void options(SlashCommandInteractionEvent event) {
        configHandler.saveOption(event);

        /*switch (event.getSubcommandName()) {
            case "rules":
                configHandler.saveOption(event);
            case "rule_channel":
                configHandler.saveOption(event);
            case "welcome_channel":
                configHandler.saveOption(event);
            case "welcome_message":
                configHandler.saveOption(event);

            case "goodbye_channel":
                configHandler.saveOption(event);
            case "goodbye_message":
                configHandler.saveOption(event);

            case "website":
                configHandler.saveOption(event);
            case "discord":
                configHandler.saveOption(event);
            case "twitter":
                configHandler.saveOption(event);
            case "instagram":
                configHandler.saveOption(event);
            case "facebook":
                configHandler.saveOption(event);
            case "youtube":
                configHandler.saveOption(event);
            case "twitch":
                configHandler.saveOption(event);

            case "mute_role":
                configHandler.saveOption(event);
            case "join_autorole":
                configHandler.saveOption(event);
            case "moderation_roles":
                configHandler.saveOption(event);

            case "chatgpt_permissions":
                configHandler.saveOption(event);
            case "chatgpt_api_token":
                configHandler.saveOption(event);
        }*/
    }

    public void ticketoptions(SlashCommandInteractionEvent event) {
        switch (event.getSubcommandName()) {
            case "ticket_channel":
                configHandler.saveOption(event);
            case "ticket_category":
                configHandler.saveOption(event);
            case "transcript_channel":
                configHandler.saveOption(event);
            case "ticket_title":
                configHandler.saveOption(event);
            case "ticket_description":
                configHandler.saveOption(event);
            case "ticket_name":
                configHandler.saveOption(event);
            case "ticket_q_and_a":
                configHandler.saveOption(event);

            case "mute_role":
                configHandler.saveOption(event);
            case "join_autorole":
                configHandler.saveOption(event);
            case "moderation_roles":
                configHandler.saveOption(event);
        }
    }

    public void notificationoptions(SlashCommandInteractionEvent event) {
        switch (event.getSubcommandName()) {
            case "moderation_notification_channel":
                configHandler.saveOption(event);
            case "report_notification_channel":
                configHandler.saveOption(event);
            case "live_notification_channel":
                configHandler.saveOption(event);
            case "offline_notification_channel":
                configHandler.saveOption(event);
        }
    }

    public void Ticket(SlashCommandInteractionEvent event) {
        TicketHandler ticketHandler = new TicketHandler();
        ModalsHandler modals = new ModalsHandler();
        switch (event.getName()) {
            default:
                modals.generateTicket(null, event);
                break;
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
                ticketHandler.closeConfirm(event);
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
    }
}
