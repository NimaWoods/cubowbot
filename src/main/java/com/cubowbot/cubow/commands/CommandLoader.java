package com.cubowbot.cubow.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public class CommandLoader {

    public void loadCommands(List<CommandData> commandList, JDA bot) {

        Guild Testserver = bot.getGuildById("1217994812108832880");

        System.out.println("Registrierung von Befehlen:");
        for (CommandData command : commandList) {
            System.out.print(command.getName() + ", ");
        }

        System.out.println("\nWaiting for Commands to be registered...");

        bot.updateCommands().addCommands(commandList).queue(
                success -> System.out.println("Registered all commands on Bot"),
                failure -> System.out.println("Failed to register commands..." + failure)
        );

        // Testserver
        Testserver.updateCommands().addCommands().queue(
                success -> System.out.println("Registered all commands on Testserver"),
                failure -> System.out.println("Failed to register commands on Testserver..." + failure)
        );
    }

    public void cleanupCommands(JDA bot, List<CommandData> commandList) {
        /*CompletableFuture<List<Command>> futureCommands = bot.retrieveCommands().submit();

        futureCommands.thenAccept(existingCommands -> {
            Set<String> existingCommandNames = existingCommands.stream()
                    .map(Command::getName)
                    .collect(Collectors.toSet());

            // Identify commands that are not in the local command list
            List<Command> commandsToRemove = existingCommands.stream()
                    .filter(cmd -> !commandList.contains(cmd.getName()))
                    .collect(Collectors.toList());

            for (Command cmd : commandsToRemove) {
                bot.deleteCommandById(cmd.getId()).queue(
                        success -> System.out.println("Successfully removed command: " + cmd.getName()),
                        error -> System.out.println("Failed to remove command: " + cmd.getName() + " Error: " + error.getMessage())
                );
            }
        }).exceptionally(ex -> {
            System.out.println("Failed to retrieve and clean up commands: " + ex.getMessage());
            return null;
        });
        System.out.println(" ");*/

        //TODO Repair auto remove
    }
}
