package com.cubowbot.cubow.handler;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class TextChannelHandler {
    public List<Message> getAllMessages(TextChannel channel) {

        List<Message> messages = new ArrayList<>();
        long lastMessageId = Long.MAX_VALUE;

        while (true) {
            List<Message> retrievedMessages = channel.getHistoryFromBeginning(100).complete().getRetrievedHistory();

            if (retrievedMessages.isEmpty()) {
                break;
            }

            lastMessageId = retrievedMessages.get(retrievedMessages.size() - 1).getIdLong();

            messages.addAll(retrievedMessages);
        }

        return messages;
    }
}
