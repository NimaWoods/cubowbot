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

		System.out.println("Starting Bot...");

		ConfigHandler configHandler = new ConfigHandler();

		System.out.println("Retrieving Configs...");
		configHandler.checkConfigs();

		System.out.println("Retrieving Token...");
		String token = null;
		try {
			token = configHandler.loadToken();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		System.out.println("Building JDA...");
		JDABuilder builder = JDABuilder.createDefault(token);

		// Load Handler
		EventListener eventListener = new EventListener();
		ButtonHandler buttonHandler = new ButtonHandler();
		SlashCommandListener slashCommandListener = new SlashCommandListener();
		AutoCompleteHandler autoCompleteHandler = new AutoCompleteHandler();
		ImageHandler imageHandler = new ImageHandler();
		ModalListener modalListener = new ModalListener();
		ContextMenuListener contextMenuListener = new ContextMenuListener();


		// Load Event Listener
		System.out.println("\nLoading Event Listener...");

		System.out.print("slashCommandListener");
		builder.addEventListeners(slashCommandListener);
		System.out.print(" -> LOADED\n");

		System.out.print("eventListener");
		builder.addEventListeners(eventListener);
		System.out.print(" -> LOADED\n");

		System.out.print("buttonHandler");
		builder.addEventListeners(buttonHandler);
		System.out.print(" -> LOADED\n");

		System.out.print("autoCompleteHandler");
		builder.addEventListeners(autoCompleteHandler);
		System.out.print(" -> LOADED\n");

		System.out.print("modalListener");
		builder.addEventListeners(modalListener);
		System.out.print(" -> LOADED\n");

		System.out.print("contextMenuListener");
		builder.addEventListeners(contextMenuListener);
		System.out.print(" -> LOADED\n");

		System.out.println(" ");

		builder.setStatus(OnlineStatus.ONLINE);

		builder.setChunkingFilter(ChunkingFilter.ALL);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

		EnumSet<CacheFlag> enumSet = EnumSet.of(CacheFlag.ONLINE_STATUS, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.VOICE_STATE);

		builder.enableCache(enumSet);

		bot = builder.build();

		try {
			bot.awaitReady();
			System.out.println("Bot Ready!");
			System.out.println(" ");
		} catch (Exception e) {
			System.out.println("Error_ " + e);
			System.exit(0);
		}

		// Setup Bot
		imageHandler.downloadWelcomeImage(bot);

		BotHandler botHandler = new BotHandler();
		botHandler.initializeActivity(bot);

		List<Guild> guilds = bot.getGuilds();
		System.out.println("Bot is currently active in " + guilds.size() + " guilds.");

		botHandler.start();

		// Load Global Commands
		CommandList commandList = new CommandList();
		commandList.loadCommands(bot);

		System.out.println("Starting Server...");

		// Start Spring Server
		SpringApplication.run(CubowApplication.class, args);

	}

	public JDA getJDA() {
		return bot;
	}
}
