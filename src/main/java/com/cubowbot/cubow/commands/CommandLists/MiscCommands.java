package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class MiscCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                new CommandDataImpl("avatar", "Gives you a members avatar in different formats")
                        .addOption(OptionType.USER, "member", "Name of Member", true),

                new CommandDataImpl("addemoji", "Adds an Emoji to the Server")
                        .addOption(OptionType.STRING, "name", "Emoji Name", true)
                        .addOption(OptionType.ATTACHMENT, "image", "Emoji", true),

                new CommandDataImpl("removeemoji", "Remove an Emoji from the Server")
                        .addOption(OptionType.STRING, "emoji", "Please provide the Emoji")

        );

        return commandList;
    }
}
