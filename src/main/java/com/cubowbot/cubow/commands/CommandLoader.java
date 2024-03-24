package com.cubowbot.cubow.commands;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommandLoader {
    private static final Logger logger = LoggerFactory.getLogger(CommandLoader.class);

    public void loadCommands(List<CommandData> commandList, JDA bot) {

        logger.info("Registrierung von Befehlen:");
        for (CommandData command : commandList) {
            logger.info(command.getName());
        }

        logger.info("Waiting for Commands to be registered...");

        bot.updateCommands().addCommands(commandList).queue(
                success -> logger.info("Registered all commands on Bot"),
                failure -> logger.info("Failed to register commands..." + failure)
        );
    }
}
