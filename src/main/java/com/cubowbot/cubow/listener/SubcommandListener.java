package com.cubowbot.cubow.listener;

import com.cubowbot.cubow.handler.ConfigHandler;
import com.cubowbot.cubow.handler.ModalsHandler;
import com.cubowbot.cubow.handler.TicketHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class SubcommandListener {

    public void Ticket(SlashCommandInteractionEvent event) {

        TicketHandler ticketHandler = new TicketHandler(event);
        ModalsHandler modals = new ModalsHandler();

        switch (event.getSubcommandName()) {
            case "sendpanel":
                System.out.println("sendpanel");
                ticketHandler.sendDashboard(event.getGuild());
                break;
            case "add":
                System.out.println("add");
                ticketHandler.add();
                break;
            case "remove":
                System.out.println("remove");
                ticketHandler.remove();
                break;
            case "close":
                System.out.println("close");
                ticketHandler.closeConfirm(event);
                break;
            case "claim":
                System.out.println("claim");
                ticketHandler.claim();
                break;
            case "transfer":
                System.out.println("transfer");
                ticketHandler.transfer();
                break;
            case "unclaim":
                System.out.println("unclaim");
                ticketHandler.unclaim();
                break;
            case "closerequest":
                System.out.println("closerequest");
                ticketHandler.closerequest();
                break;
            default:
                System.out.println("default");
                modals.generateTicket(null, event);
                break;
        }
    }
}
