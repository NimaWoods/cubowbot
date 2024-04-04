package com.cubowbot.cubow.handler.discord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class AutoCompleteHandler extends ListenerAdapter{

    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
            if (event.getName().equals("unban") && event.getFocusedOption().getName().equals("user")) {
                event.getGuild().retrieveBanList().queue(bans -> {
                    List<Command.Choice> choices = bans.stream()
                        .map(ban -> new Command.Choice(ban.getUser().getName(), ban.getUser().getId()))
                        .collect(Collectors.toList());
            
                    event.replyChoices(choices).queue(null, error -> {
                        System.err.println("Ein Fehler ist aufgetreten: " + error.getMessage());
                    });
                }, error -> {
                    System.err.println("Ein Fehler ist aufgetreten: " + error.getMessage());
                });
            }

            else if (event.getName().equals("embedcreate") && event.getFocusedOption().getName().equals("color")) {
                try {
                    // Read the JSON file from the resources directory
                    InputStream is = getClass().getClassLoader().getResourceAsStream("colors.json");
                    if (is == null) {
                        throw new FileNotFoundException("colors.json not found in resources directory");
                    }
                    String json = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));

                    // Parse the JSON file
                    JsonArray colorsArray = com.google.gson.JsonParser.parseString(json).getAsJsonArray();

                    // Create a list to store the color names
                    List<String> colorNames = new ArrayList<>();

                    // Iterate over the JSON array and add each color name to the list
                    for (JsonElement colorElement : colorsArray) {
                        JsonObject colorObject = colorElement.getAsJsonObject();
                        String colorName = colorObject.get("color").getAsString();
                        colorNames.add(colorName);
                    }

                    // Create an OptionData object for the color option
                    OptionData colorOption = new OptionData(OptionType.STRING, "color", "Embed color")
                            .setRequired(false)
                            .setAutoComplete(true);

                    if (event.getName().equals("embedcreate") && event.getFocusedOption().getName().equals("color")) {
                        // Get the user's input
                        String userInput = event.getFocusedOption().getValue();

                        // Filter the color names based on the user's input
                        List<Command.Choice> options = colorNames.stream()
                                .filter(colorName -> colorName.startsWith(userInput))
                                .map(colorName -> new Command.Choice(colorName, colorName))
                                .collect(Collectors.toList());

                        // Reply with the filtered color names
                        event.replyChoices(options).queue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (event.getName().equals("embededit") && event.getFocusedOption().getName().equals("color")) {
                try {
                    // Read the JSON file
                    String json = new String(Files.readAllBytes(Paths.get("colors.json")));

                    // Parse the JSON file
                    JsonArray colorsArray = com.google.gson.JsonParser.parseString(json).getAsJsonArray();

                    // Create a list to store the color names
                    List<String> colorNames = new ArrayList<>();

                    // Iterate over the JSON array and add each color name to the list
                    for (JsonElement colorElement : colorsArray) {
                        JsonObject colorObject = colorElement.getAsJsonObject();
                        String colorName = colorObject.get("color").getAsString();
                        colorNames.add(colorName);
                    }

                    // Create an OptionData object for the color option
                    OptionData colorOption = new OptionData(OptionType.STRING, "color", "Embed color")
                        .setRequired(false)
                        .setAutoComplete(true);

                        if (event.getName().equals("embededit") && event.getFocusedOption().getName().equals("color")) {
                            // Get the user's input
                            String userInput = event.getFocusedOption().getValue();
                    
                            // Filter the color names based on the user's input
                            List<Command.Choice> options = colorNames.stream()
                                .filter(colorName -> colorName.startsWith(userInput))
                                .map(colorName -> new Command.Choice(colorName, colorName))
                                .collect(Collectors.toList());
                    
                            // Reply with the filtered color names
                            event.replyChoices(options).queue();
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
}
