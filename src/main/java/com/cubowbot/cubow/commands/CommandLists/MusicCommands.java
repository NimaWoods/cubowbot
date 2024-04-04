package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class MusicCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = List.of(

                new CommandDataImpl("play", "Play a Song")
                        .addOption(OptionType.STRING, "link", "The song to play (youtube link/song name)", true),


                new CommandDataImpl("join", "Joins a Voice Channel"),
                new CommandDataImpl("leave", "Leaves a Voice Channel")

        );

        return commandList;
    }
}