package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigCommands {

    public List<CommandData> loadList() {
        List<CommandData> commandList = new ArrayList<>(Arrays.asList(
                new CommandDataImpl("options", "Set every general Option for your server")
                        .addSubcommands(Arrays.asList(
                                new SubcommandData("rules", "Rules Embed to send in the rule channel")
                                        .addOption(OptionType.STRING, "rules", "\\n for new line"),
                                new SubcommandData("rule_channel", "Rules Embed to send in the rule channel")
                                        .addOption(OptionType.CHANNEL, "rule_channel", "\\n for new line"),

                                new SubcommandData("welcome_channel", "Welcome Channel")
                                        .addOption(OptionType.CHANNEL, "welcomechannel", "test"),
                                new SubcommandData("welcome_message", "Welcome Message")
                                        .addOption(OptionType.STRING, "welcomemessage", "Welcome Message"),

                                new SubcommandData("goodbye_channel", "Goodbye Channel")
                                        .addOption(OptionType.CHANNEL, "goodbyechannel", "Goodbye Channel"),
                                new SubcommandData("goodbye_message", "Goodbye Message")
                                        .addOption(OptionType.STRING, "goodbyemessage", "Goodbye Message"),

                                new SubcommandData("website", "Your Website Link")
                                        .addOption(OptionType.STRING, "website", "Your Website Link"),
                                new SubcommandData("discord", "Your Discord Link")
                                        .addOption(OptionType.STRING, "discord", "Your Discord Link"),
                                new SubcommandData("twitter", "Your Twitter Link")
                                        .addOption(OptionType.STRING, "twitter", "Your Twitter Link"),
                                new SubcommandData("instagram", "Your Instagram Link")
                                        .addOption(OptionType.STRING, "instagram", "Your Instagram Link"),
                                new SubcommandData("facebook", "Your Facebook Link")
                                        .addOption(OptionType.STRING, "facebook", "Your Facebook Link"),
                                new SubcommandData("youtube", "Your Youtube Link")
                                        .addOption(OptionType.STRING, "youtube", "Your Youtube Link"),
                                new SubcommandData("twitch", "Your Twitch Link")
                                        .addOption(OptionType.STRING, "twitch", "Your Twitch Link"),

                                new SubcommandData("mute_role", "Mute Role")
                                        .addOption(OptionType.ROLE, "muterole", "Mute Role"),
                                new SubcommandData("join_autorole", "Autorole")
                                        .addOption(OptionType.ROLE, "joinautorole", "Autorole"),
                                new SubcommandData("moderation_roles", "Mod Role")
                                        .addOption(OptionType.ROLE, "moderationroles", "Mod Role"),

                                new SubcommandData("chatgpt_permissions", "ALL/ADMIN/OWNER")
                                    .addOption(OptionType.STRING, "chatgpt_permissions", "ALL/ADMIN/OWNER"),
                                new SubcommandData("chatgpt_api_token", "ChatGPT API Token")
                                        .addOption(OptionType.STRING, "chatgpt_api_token", "ALL/ADMIN/OWNER")
                        )),

                new CommandDataImpl("ticketoptions", "Set every ticket Option for your server")
                        .addSubcommands(Arrays.asList(
                                new SubcommandData("ticket_channel", "Channel for the Ticket Panel")
                                        .addOption(OptionType.CHANNEL, "ticket_channel_id", "Channel for the Ticket Panel"),
                                new SubcommandData("ticket_category", "Category where Tickets get created")
                                        .addOption(OptionType.NUMBER, "ticket_channel_id", "Channel for the Ticket Panel"),
                                new SubcommandData("transcript_channel", "Transkript Channel")
                                        .addOption(OptionType.CHANNEL, "transcript_channel", "Transkript Channel"),
                                new SubcommandData("ticket_title", "Ticket Titel")
                                        .addOption(OptionType.STRING, "ticket_title", "Ticket Titel"),
                                new SubcommandData("ticket_description", "Ticket Description")
                                        .addOption(OptionType.STRING, "ticket_description", "Ticket Description"),
                                new SubcommandData("ticket_name", "Ticket Channel Name")
                                        .addOption(OptionType.STRING, "ticket_name", "Ticket Channel Name"),
                                new SubcommandData("ticket_q_and_a", "Q&A to send to ChatGPT")
                                        .addOption(OptionType.STRING, "ticket_q_and_a", "Q&A to send to ChatGPT"),

                                new SubcommandData("ticket_view_role_permission", "List of roles who can view Tickets")
                                        .addOption(OptionType.STRING, "ticket_view_role_permission", "List of roles who can view Tickets"),
                                new SubcommandData("ticket_send_role_permission", "List of roles who can send messages in Tickets")
                                        .addOption(OptionType.STRING, "ticket_send_role_permission", "List of roles who can send messages in Tickets"),
                                new SubcommandData("ticket_mention_role", "List of roles who get mentioned on new Ticket")
                                        .addOption(OptionType.STRING, "ticket_mention_role", "List of roles who get mentioned on new Ticket")
                        )),

                new CommandDataImpl("notificationoptions", "Set every notification Option for your server")
                        .addSubcommands(Arrays.asList(
                                new SubcommandData("moderation_notification_channel", "Moderation Notification Channel")
                                        .addOption(OptionType.CHANNEL, "moderation_notification_channel", "Moderation Notification Channel"),
                                new SubcommandData("report_notification_channel", "Report Notification Channel")
                                        .addOption(OptionType.CHANNEL, "report_notification_channel", "Report Notification Channel"),
                                new SubcommandData("live_notification_channel", "Live Notification Channel (Twitch)")
                                        .addOption(OptionType.CHANNEL, "live_notification_channel", "Live Notification Channel (Twitch)"),
                                new SubcommandData("offline_notification_channel", "Offline Notification Channel (Twitch)")
                                        .addOption(OptionType.CHANNEL, "offline_notification_channel", "Offline Notification Channel (Twitch)")
                        ))
        ));

        return commandList;
    }
}