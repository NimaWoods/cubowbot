package com.cubowbot.cubow.Listener;

import com.cubowbot.cubow.Handler.ChatGPTHandler;
import com.cubowbot.cubow.Handler.ConfigHandler;
import com.cubowbot.cubow.Handler.TicketHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.ThreadChannelAction;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ModalListener extends ListenerAdapter {
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("ticketModal")) {

            event.deferReply().setEphemeral(true).queue();

            Member member = event.getMember();
            Guild server = event.getGuild();
            String description = event.getValue("ticket-description").getAsString();

            TicketHandler ticketHandler = new TicketHandler();
            TextChannel textChannel = ticketHandler.createTicket(server, member, description);

            event.getHook().editOriginal("Dein Ticket wurde unter " + textChannel.getAsMention() + " erstellt").queue();
        }
    }
}
