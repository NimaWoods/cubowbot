package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class TicketCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                new CommandDataImpl("add", "Adds a User to a Ticket")
                        .addOption(OptionType.USER, "user", "User to add", true),


                new CommandDataImpl("remove", "Removes a User from a Ticket")
                        .addOption(OptionType.USER, "user", "User to remove", true),

                new CommandDataImpl("claim", "[Moderator] Assigns a single staff member to a ticket"),

                new CommandDataImpl("unclaim", "[Moderator] Unassigns a single staff member to a ticket"),

                new CommandDataImpl("close", "Close the current Ticket"),

                new CommandDataImpl("closerequest", "[Moderator] Asking the user to confirm the ticket is able to close"),

                new CommandDataImpl("ticket", "Opens a new Ticket"),

                // TODO new CommandDataImpl("reopen", "Reopens a Ticket that was previously closed"),

                new CommandDataImpl("sendpanel", "Resends the Ticket panel"),

                new CommandDataImpl("transfer", "[Moderator] Transfers a claimed ticket to another staff")
                        .addOption(OptionType.USER, "user", "Member to transfer to", true)

        );

        return commandList;
    }
}
