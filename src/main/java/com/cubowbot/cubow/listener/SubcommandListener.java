package com.cubowbot.cubow.listener;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SubcommandListener {
    public void options(SlashCommandInteractionEvent event) {
        switch (event.getSubcommandName()) {
            case "test":
                System.out.println("TESSSSSSSSSST");
        }
    }
}
