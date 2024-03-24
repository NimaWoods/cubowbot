package com.cubowbot.cubow;

import com.cubowbot.cubow.commands.CommandList;
import com.cubowbot.cubow.handler.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class CubowApplication {

	private static JDA bot;
	private static final Logger logger = LoggerFactory.getLogger(CubowApplication.class);

	public static void main(String[] args) {


		// Start Spring Server
		logger.info("Starting Server...");
		SpringApplication.run(CubowApplication.class, args);
		logger.info("\nServer started\n");


		logger.info("Connecting to MongoDB Database...");
		DataBaseHandler dataBaseHandler = new DataBaseHandler();
		dataBaseHandler.connect();

		// Connecting Discord Bot
		logger.info("Connecting Bot JDA...");

		ConfigHandler configHandler = new ConfigHandler();

		logger.info("Checking Configs...");
		configHandler.checkConfigs();

		BotHandler botHandler = new BotHandler();
		BetaHandler betaHandler = new BetaHandler();
		JDABuilder builder = botHandler.build();

		bot = builder.build();

		try {
			bot.awaitReady();
			logger.info("Bot Online!");
			System.out.println(" ");
		} catch (Exception e) {
			logger.info("Error_ " + e);
			System.exit(0);
		}

		// Setup Bot

		ImageHandler imageHandler = new ImageHandler();
		imageHandler.downloadWelcomeImage(bot);

		botHandler.initializeActivity(bot);

		List<Guild> guilds = bot.getGuilds();
		logger.info("Bot is currently active in " + guilds.size() + " guilds.");

		botHandler.start();

		// Load Global Commands
		CommandList commandList = new CommandList();
		commandList.loadCommands();

		logger.info("Sending Beta Message...");
		betaHandler.sendBetaMessage();

	}

	public JDA getJDA() {
		return bot;
	}
}
