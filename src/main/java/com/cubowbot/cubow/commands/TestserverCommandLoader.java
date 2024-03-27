package com.cubowbot.cubow.commands;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class TestserverCommandLoader {
    private static final Logger logger = LoggerFactory.getLogger(TestserverCommandLoader.class);

    public void loadCommands() {
        CubowApplication cubowApplication = new CubowApplication();
        JDA bot = cubowApplication.getJDA();

        Guild Testserver = bot.getGuildById("1217994812108832880");

        List<CommandData> commandList = List.of(

            //new CommandDataImpl("sendwelcometest", "Test");
        );

        // Testserver
        // Activate while Debugging by changing false to true
        if (commandList != null) {
            Testserver.updateCommands().addCommands(commandList).queue(
                    success -> logger.info("Registered all commands on Testserver " + Testserver.getName()),
                    failure -> logger.info("Failed to register commands on Testserver..." + failure)
            );
        } else {
            Testserver.updateCommands().addCommands().queue(
                    success -> logger.info("Removed all Test commands from Testserver " + Testserver.getName() + " because TeserverCommandList is null "),
                    failure -> logger.info("Failed to remove commands from Testserver..." + failure));
        }
    }
}
