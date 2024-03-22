package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class TextCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                new CommandDataImpl("website", "Craftex Website"),
                new CommandDataImpl("ping", "Ping Pong"),
                new CommandDataImpl("help", "Shows you a list of commands"),
                new CommandDataImpl("commands", "Shows you a list of commands"),
                new CommandDataImpl("serverstats", "Shows you statistics about the server")

                );

        return commandList;
    }
}
