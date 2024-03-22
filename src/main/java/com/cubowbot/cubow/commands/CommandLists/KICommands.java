package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class KICommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                new CommandDataImpl("chatgpt", "Ask ChatGPT anything")
                        .addOption(OptionType.STRING, "prompt", "prompt", true),

                new CommandDataImpl("dall-e", "Generate an Image with Dall-E")
                        .addOption(OptionType.STRING, "prompt", "prompt", true)

        );

        return commandList;
    }
}
