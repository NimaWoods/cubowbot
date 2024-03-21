package com.cubowbot.cubow.Listener;

import com.cubowbot.cubow.Handler.ChatGPTHandler;
import com.cubowbot.cubow.Handler.ConfigHandler;
import com.cubowbot.cubow.Handler.TicketHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.interaction.command.GenericContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.managers.channel.ChannelManager;
import net.dv8tion.jda.api.requests.restaction.ThreadChannelAction;

import java.awt.*;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ContextMenuListener extends ListenerAdapter {
    @Override
    public void onMessageContextInteraction(MessageContextInteractionEvent event) {

        System.out.println(event.getName() + " " + event.getType() + " " + event.getMember().getEffectiveName());

            if (event.getName().equals("Ticket erstellen")) {

                event.deferReply().setEphemeral(true).queue();

                Message target = event.getTarget();
                Member member = event.getMember();
                Guild server = event.getGuild();

                String description = target.getContentDisplay();

                TicketHandler ticketHandler = new TicketHandler();

                TextChannel textChannel = ticketHandler.createTicket(server, member, description);

                event.reply("Dein Ticket wurde unter " + textChannel.getAsMention() + " erstellt").setEphemeral(true).queue(
                        message -> message.deleteOriginal().queueAfter(10, TimeUnit.SECONDS)
                );



            } else if (event.getName().equals("Überprüfe auf Scam")) {

                Message target = event.getTarget();
                String context = target.getContentRaw();
                System.out.println(context);

                event.deferReply()
                        .setEphemeral(true)
                        .queue();

                ChatGPTHandler chatGPTHandler = new ChatGPTHandler();

                String answer = null;

                answer = chatGPTHandler.generateText("Überprüfe ob diese Nachricht Scam sein könnte, wir sind auf einem Minecraft Discord Server der zu einem Minecraft Server gehört: " + context, event.getGuild());

                EmbedBuilder eb = new EmbedBuilder();

                eb.setTitle("Überprüfe auf Scam");
                eb.setDescription(answer);
                eb.setColor(Color.MAGENTA);

                event.getHook().editOriginalEmbeds(eb.build()).queue();

            } else {
                System.out.println("Command" + event.getName() + "not found");
            }
        }

}
