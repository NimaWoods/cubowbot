package com.cubowbot.cubow.handler.discord;

import com.cubowbot.cubow.handler.discord.music.TrackScheduler;
import com.cubowbot.cubow.listener.*;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumSet;

public class BotHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(BotHandler.class);

    ConfigHandler configHandler = new ConfigHandler();

    public JDABuilder build() {
        logger.info("Building JDA...");

        logger.info("Retrieving Token...");
        String token = null;

        configHandler.checkConfigs();
        try {
            token = configHandler.loadToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JDABuilder builder = JDABuilder.createDefault(token);

        // Load Handler
        EventListener eventListener = new EventListener();
        ButtonListener buttonListener = new ButtonListener();
        SlashCommandListener slashCommandListener = new SlashCommandListener();
        AutoCompleteHandler autoCompleteHandler = new AutoCompleteHandler();
        ModalListener modalListener = new ModalListener();
        ContextMenuListener contextMenuListener = new ContextMenuListener();

        // Load Event Listener
        logger.info("\nLoading Event Listener...");

        builder.addEventListeners(slashCommandListener);
        logger.info("slashCommandListener");

        builder.addEventListeners(eventListener);
        logger.info("eventListener");

        builder.addEventListeners(buttonListener);
        logger.info("buttonHandler");

        builder.addEventListeners(autoCompleteHandler);
        logger.info("autoCompleteHandler");

        builder.addEventListeners(modalListener);
        logger.info("modalListener");

        builder.addEventListeners(contextMenuListener);
        logger.info("contextMenuListener");

        System.out.println(" ");

        builder.setStatus(OnlineStatus.ONLINE);

        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT);

        EnumSet<CacheFlag> enumSet = EnumSet.of(CacheFlag.ONLINE_STATUS, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.VOICE_STATE);

        builder.enableCache(enumSet);

        return builder;
    }

    public void initializeActivity(JDA bot) {

        // Load Activity Text
        String activityText = "cubow.nimawoods.de";

        Activity.ActivityType activityType = null;
        activityType = getStatusType();

        // Set Activity
        bot.getPresence().setActivity(Activity.of(activityType, activityText));
    }

    private Activity.ActivityType getStatusType() {

        String activityType = "watching";

        return switch (activityType.toLowerCase()) {
            case "watching" -> Activity.ActivityType.WATCHING;
            case "listening" -> Activity.ActivityType.LISTENING;
            case "streaming" -> Activity.ActivityType.STREAMING;
            default -> Activity.ActivityType.PLAYING;
        };
    }

    public class RestartBot {

        public static void main(String[] args) {
            try {
                logger.info("Initiating restart...");
                Runtime.getRuntime().exec("./start_bot.sh");
                System.exit(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
