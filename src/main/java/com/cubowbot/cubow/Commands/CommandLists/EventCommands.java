package com.cubowbot.cubow.Commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Arrays;
import java.util.List;

public class EventCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                // TODO new CommandDataImpl("createevent", "Creates an Discord event")
                //.addOption(OptionType.STRING, "eventname", "Event Name", true)
                //.addOption(OptionType.STRING, "eventbeschreibung","Event Beschreibung", true)

        );

        return commandList;
    }
}
