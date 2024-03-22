package com.cubowbot.cubow.handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class ModalsHandler {
    public void generateTicket(ButtonInteractionEvent buttonEvent, SlashCommandInteractionEvent slashEvent) {

        TextInput description = TextInput.create("ticket-description", "description", TextInputStyle.PARAGRAPH)
                .setMinLength(10)
                .setMaxLength(1024)
                .setRequired(true)
                .setPlaceholder("Wie können wir dir helfen?")
                .build();

        Modal modal = Modal.create("ticketModal", "Ticket erstellen")
                .addActionRows(ActionRow.of(description))
                .build();

        if (buttonEvent != null) {
            ButtonInteractionEvent event = buttonEvent;
            event.replyModal(modal).queue();

        } else if (slashEvent != null ) {
            SlashCommandInteractionEvent event = slashEvent;
            event.replyModal(modal).queue();

        } else {
            throw new IllegalArgumentException("ERROR: Event is null");
        }
    }
}