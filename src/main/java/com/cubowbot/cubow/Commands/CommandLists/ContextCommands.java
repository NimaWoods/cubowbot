package com.cubowbot.cubow.Commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Arrays;
import java.util.List;

public class ContextCommands {
    public List<CommandData> loadList() {

        return Arrays.asList(

                Commands.message("Ticket erstellen"),
                Commands.message("Überprüfe auf Scam")

        );
    }
}
