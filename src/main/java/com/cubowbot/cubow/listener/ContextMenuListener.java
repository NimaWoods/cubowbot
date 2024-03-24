package com.cubowbot.cubow.listener;

import com.cubowbot.cubow.CubowApplication;
import com.cubowbot.cubow.handler.ChatGPTHandler;
import com.cubowbot.cubow.handler.TicketHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class ContextMenuListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ContextMenuListener.class);
    @Override
    public void onMessageContextInteraction(MessageContextInteractionEvent event) {

        logger.info(event.getName() + " " + event.getType() + " " + event.getMember().getEffectiveName());

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
                logger.info(context);

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
                logger.info("Command" + event.getName() + "not found");
            }
        }

}
