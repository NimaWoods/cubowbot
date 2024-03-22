package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class ModerationCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                new CommandDataImpl("ban", "Ban a member")
                        .addOption(OptionType.USER, "user", "User to ban", true),

                new CommandDataImpl("tempban", "Bans someone for the specified time")
                        .addOption(OptionType.USER, "user", "User to ban", true)
                        .addOption(OptionType.INTEGER, "hours", "Time in hours", true)
                        .addOption(OptionType.INTEGER, "minutes", "Time in minutes", true)
                        .addOption(OptionType.INTEGER, "seconds", "Time in seconds", true),

                new CommandDataImpl("kick", "Kick a member")
                        .addOption(OptionType.USER, "user", "User to ban", true),

                new CommandDataImpl("unban", "Unban a member")
                        .addOption(OptionType.STRING, "user", "User to unban", true, true),

                //TODO new CommandDataImpl("mute", "Mute a member")
                //.addOption(OptionType.USER, "user", "User to mute", true)
                //.addOption(OptionType.INTEGER, "duration", "The duration of the mute in seconds.", false),

                //TODO new CommandDataImpl("unmute", "Unmute a member")
                //.addOption(OptionType.USER, "user", "User to mute", true),

                new CommandDataImpl("timeout", "Timeout a member")
                        .addOption(OptionType.USER, "user", "User to Timeout", true)
                        .addOption(OptionType.INTEGER, "hours", "Time in hours", true)
                        .addOption(OptionType.INTEGER, "minutes", "Time in minutes", true)
                        .addOption(OptionType.INTEGER, "seconds", "Time in seconds", true),


                new CommandDataImpl("removetimeout", "Remove timeout from a member")
                        .addOption(OptionType.USER, "user", "User to remove timeout from", true)

                );

        return commandList;
    }
}
