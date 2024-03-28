package com.cubowbot.cubow.listener;

import com.cubowbot.cubow.handler.ConfigHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SubcommandListener {
    public void options(SlashCommandInteractionEvent event) {

        String subCommandName = event.getSubcommandName();

        switch (subCommandName) {
            case "rules":
                ConfigHandler configHandler = new ConfigHandler();
                configHandler.saveOption(event);
            case "rule_channel":
                System.out.println("TESSSSSSSSSST");
            case "welcome_channel":
                System.out.println("TESSSSSSSSSST");
            case "welcome_message":
                System.out.println("TESSSSSSSSSST");

            case "goodbye_channel":
                System.out.println("TESSSSSSSSSST");
            case "goodbye_message":
                System.out.println("TESSSSSSSSSST");

            case "website":
                System.out.println("TESSSSSSSSSST");
            case "discord":
                System.out.println("TESSSSSSSSSST");
            case "twitter":
                System.out.println("TESSSSSSSSSST");
            case "instagram":
                System.out.println("TESSSSSSSSSST");
            case "facebook":
                System.out.println("TESSSSSSSSSST");
            case "youtube":
                System.out.println("TESSSSSSSSSST");
            case "twitch":
                System.out.println("TESSSSSSSSSST");

            case "mute_role":
                System.out.println("TESSSSSSSSSST");
            case "join_autorole":
                System.out.println("TESSSSSSSSSST");
            case "moderation_roles":
                System.out.println("TESSSSSSSSSST");

            case "chatgpt_permissions":
                System.out.println("TESSSSSSSSSST");
            case "chatgpt_api_token":
                System.out.println("TESSSSSSSSSST");
        }
    }

    public void ticketoptions(SlashCommandInteractionEvent event) {
        switch (event.getSubcommandName()) {
            case "ticket_channel":
                System.out.println("TESSSSSSSSSST");
            case "ticket_category":
                System.out.println("TESSSSSSSSSST");
            case "transcript_channel":
                System.out.println("TESSSSSSSSSST");
            case "ticket_title":
                System.out.println("TESSSSSSSSSST");
            case "ticket_description":
                System.out.println("TESSSSSSSSSST");
            case "ticket_name":
                System.out.println("TESSSSSSSSSST");
            case "ticket_q_and_a":
                System.out.println("TESSSSSSSSSST");

            case "mute_role":
                System.out.println("TESSSSSSSSSST");
            case "join_autorole":
                System.out.println("TESSSSSSSSSST");
            case "moderation_roles":
                System.out.println("TESSSSSSSSSST");
        }
    }

    public void notificationoptions(SlashCommandInteractionEvent event) {
        switch (event.getSubcommandName()) {
            case "moderation_notification_channel":
                System.out.println("TESSSSSSSSSST");
            case "report_notification_channel":
                System.out.println("TESSSSSSSSSST");
            case "live_notification_channel":
                System.out.println("TESSSSSSSSSST");
            case "offline_notification_channel":
                System.out.println("TESSSSSSSSSST");
        }
    }
}
