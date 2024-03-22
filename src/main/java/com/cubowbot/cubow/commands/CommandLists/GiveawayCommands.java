package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class GiveawayCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                new CommandDataImpl("creategiveaway", "creates a new giveaway")
                        .addOption(OptionType.STRING, "gewinn", "Giveaway Gewinn", true)
                        .addOption(OptionType.INTEGER, "winner", "Amount of Winner", true)
                        .addOption(OptionType.INTEGER, "seconds", "Zeit in Sekunden", true)
                        .addOption(OptionType.INTEGER, "minutes", "Zeit in Sekunden", true)
                        .addOption(OptionType.INTEGER, "hours", "Zeit in Sekunden", true)
                        .addOption(OptionType.INTEGER, "days", "Zeit in Sekunden", true)
                        .addOption(OptionType.CHANNEL, "channel", "Channel where to send the giveaway")

                //TODO new CommandDataImpl("endgiveaway", "ends a giveaway),

                //TODO new CommandDataImpl("rerollgiveaway", "creates a new giveaway")

        );

        return commandList;
    }
}
