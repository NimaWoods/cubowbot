package com.cubowbot.cubow.commands;

import com.cubowbot.cubow.CubowApplication;
import com.cubowbot.cubow.commands.CommandLists.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CommandList {
    private static final Logger logger = LoggerFactory.getLogger(CommandList.class);
    public void loadCommands(JDA bot) {

        CommandLoader commandLoader = new CommandLoader();
        TestserverCommandLoader testserverCommandLoader = new TestserverCommandLoader();

        EmbedCommands embedCommands = new EmbedCommands();
        EventCommands eventCommands = new EventCommands();
        GiveawayCommands giveawayCommands = new GiveawayCommands();
        KICommands kiCommands = new KICommands();
        LockdownCommands lockdownCommands = new LockdownCommands();
        MiscCommands miscCommands = new MiscCommands();
        ModerationCommands moderationCommands = new ModerationCommands();
        MusicCommands musicCommands = new MusicCommands();
        ReportCommands reportCommands = new ReportCommands();
        TextCommands textCommands = new TextCommands();
        TicketCommands ticketCommands = new TicketCommands();
        FeedbackCommands feedbackCommands = new FeedbackCommands();

        // Context Menu
        ContextCommands contextCommands = new ContextCommands();


        // Initialize the commandList
        List<CommandData> commandList = new ArrayList<>();

        // Combine all command lists into the commandList
        commandList.addAll(embedCommands.loadList());
        commandList.addAll(eventCommands.loadList());
        commandList.addAll(giveawayCommands.loadList());
        commandList.addAll(kiCommands.loadList());
        commandList.addAll(lockdownCommands.loadList());
        commandList.addAll(miscCommands.loadList());
        commandList.addAll(moderationCommands.loadList());
        commandList.addAll(musicCommands.loadList());
        commandList.addAll(reportCommands.loadList());
        commandList.addAll(textCommands.loadList());
        commandList.addAll(ticketCommands.loadList());
        commandList.addAll(contextCommands.loadList());
        commandList.addAll(feedbackCommands.loadList());

        logger.info(" ");

        // Load all commands
        commandLoader.loadCommands(commandList, bot);
        testserverCommandLoader.loadCommands();
    }
}
