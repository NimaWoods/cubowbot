package com.cubowbot.cubow.Commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class LockdownCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                new CommandDataImpl("lockdownchannel", "Lockdown Channel")
                        .addOption(OptionType.INTEGER, "minuten", "Zeit in Minuten"),
                new CommandDataImpl("lockdownserver", "Lockdown Channel")
                        .addOption(OptionType.INTEGER, "minuten", "Zeit in Minuten")

        );

        return commandList;
    }
}
