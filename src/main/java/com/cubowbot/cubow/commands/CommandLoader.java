package com.cubowbot.cubow.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public class CommandLoader {

    public void loadCommands(List<CommandData> commandList, JDA bot) {

        System.out.println("Registrierung von Befehlen:");
        for (CommandData command : commandList) {
            System.out.print(command.getName() + ", ");
        }

        System.out.println("\nWaiting for Commands to be registered...");

        bot.updateCommands().addCommands(commandList).queue(
                success -> System.out.println("Registered all commands on Bot"),
                failure -> System.out.println("Failed to register commands..." + failure)
        );
    }
}
