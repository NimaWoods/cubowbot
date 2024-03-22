package com.cubowbot.cubow;

import com.cubowbot.cubow.commands.CommandList;
import com.cubowbot.cubow.handler.*;
import com.cubowbot.cubow.listener.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

@SpringBootApplication
public class CubowApplication {

	private static JDA bot;

	public static void main(String[] args) throws Exception {

		System.out.println("Starting Server...");

		// Start Spring Server
		SpringApplication.run(CubowApplication.class, args);

		System.out.println("\nServer started\n");

		System.out.println("Starting Bot...");

		ConfigHandler configHandler = new ConfigHandler();

		System.out.println("Checking Configs...");

		BotHandler botHandler = new BotHandler();
		JDABuilder builder = botHandler.build();

		bot = builder.build();

		try {
			bot.awaitReady();
			System.out.println("Bot Online!");
			System.out.println(" ");
		} catch (Exception e) {
			System.out.println("Error_ " + e);
			System.exit(0);
		}

		// Setup Bot

		ImageHandler imageHandler = new ImageHandler();
		imageHandler.downloadWelcomeImage(bot);

		botHandler.initializeActivity(bot);

		List<Guild> guilds = bot.getGuilds();
		System.out.println("Bot is currently active in " + guilds.size() + " guilds.");

		botHandler.start();

		// Load Global Commands
		CommandList commandList = new CommandList();
		commandList.loadCommands(bot);

		System.out.println("Done! Bot Ready");

	}

	public JDA getJDA() {
		return bot;
	}
}
