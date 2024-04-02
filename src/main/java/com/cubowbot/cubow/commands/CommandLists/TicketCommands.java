package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;

public class TicketCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                new CommandDataImpl("ticket", "Create and Manage Tickets")
                        .addSubcommands(new SubcommandData("create", "Create a Ticket"))

                        .addSubcommands(new SubcommandData("add", "Adds a User to a Ticket")
                            .addOption(OptionType.USER, "user", "User to add", true))

                        .addSubcommands(new SubcommandData("remove", "Removes a User from a Ticket")
                            .addOption(OptionType.USER, "user", "Member to transfer to", true))

                        .addSubcommands(new SubcommandData("claim", "Assigns a single staff member to a ticket"))

                        .addSubcommands(new SubcommandData("unclaim", "[Moderator] Unassigns a single staff member to a ticket"))

                        .addSubcommands(new SubcommandData("close", "Close the current Ticket"))

                        .addSubcommands(new SubcommandData("closerequest", "Asking the user to confirm the ticket is able to close"))

                        .addSubcommands(new SubcommandData("reopen", "Reopens a Ticket that was previously closed"))

                        .addSubcommands(new SubcommandData("sendpanel", "Resends the Ticket panel"))

                        .addSubcommands(new SubcommandData("transfer", "[Moderator] Transfers a claimed ticket to another staff"))
        );

        return commandList;
    }
}
