package com.cubowbot.cubow.Commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class BotCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                new CommandDataImpl("bug", "Reports an Bug")
                        .addOption(OptionType.STRING, "Beschreibung", "Erkläre den Fehler so genau wie möglich")
        );

        return commandList;
    }
}
