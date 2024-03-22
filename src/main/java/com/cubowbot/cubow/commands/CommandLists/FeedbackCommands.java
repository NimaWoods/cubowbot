package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class FeedbackCommands {

    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                new CommandDataImpl("bug", "report a bug for Cubow")
                        .addOption(OptionType.STRING, "title", "Bug title", true)
                        .addOption(OptionType.STRING, "description", "Bug description", true)
                        .addOption(OptionType.ATTACHMENT, "file", "Attachment", false)
                        .addOption(OptionType.STRING, "link", "Server invite Link for testing", false),

                new CommandDataImpl("suggest", "suggest a feature for Cubow")
                        .addOption(OptionType.STRING, "title", "Bug title", true)
                        .addOption(OptionType.STRING, "description", "Bug description", true)
                        .addOption(OptionType.ATTACHMENT, "file", "Attachment", false)
                        .addOption(OptionType.STRING, "link", "Server invite Link for testing", false)
        );

        return commandList;
    }

}
