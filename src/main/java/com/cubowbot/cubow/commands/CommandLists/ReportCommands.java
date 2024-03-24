package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class ReportCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = List.of(

                new CommandDataImpl("report", "Report a member")
                        .addOption(OptionType.USER, "user", "User to report", true)
                        .addOption(OptionType.STRING, "reason", "Reason", true)

        );

        return commandList;
    }
}
