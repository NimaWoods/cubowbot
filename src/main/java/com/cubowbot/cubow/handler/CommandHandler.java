package com.cubowbot.cubow.handler;

import com.cubowbot.cubow.generator.EmbedGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class CommandHandler {

    private final SlashCommandInteractionEvent event;

    public CommandHandler(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void help() {
        EmbedGenerator embedConstructor = new EmbedGenerator();

        CommandList(event, commandList -> {
                    // Sort the commandList array
                    String[] sortedCommands = commandList.toArray(new String[0]);
                    Arrays.sort(sortedCommands);

                    // Create a StringBuilder to hold the sorted commands
                    StringBuilder commandListString = new StringBuilder();

                    // Append sorted commands to the StringBuilder
                    for (String command : sortedCommands) {
                        commandListString.append(command).append("\n");
                    }
                    if (!commandListString.isEmpty()) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Command List", null);
                        eb.setColor(Color.GREEN);
                        eb.setDescription(commandListString.toString());
                        event.replyEmbeds(eb.build()).queue();
                        //TODO turnable Pages
                    } else {
                        embedConstructor.failure(event, "No commands found!", true);
                    }
                }, failure -> embedConstructor.failure(event, "Failed: " + failure, true)
        );
    }

    public void CommandList(SlashCommandInteractionEvent event, Consumer<List<String>> successCallback, Consumer<Throwable> failureCallback) {
        Guild server = event.getGuild();
        List<String> commandList = new ArrayList<>();

        server.retrieveCommands().queue(commands -> {
            for (Command command : commands) {
                commandList.add("/" + command.getName());
            }
            successCallback.accept(commandList);
        }, failureCallback);
    }

}
