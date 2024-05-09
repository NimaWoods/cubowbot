package com.cubowbot.cubow.handler.discord;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BetaHandler {
    private static final Logger logger = LoggerFactory.getLogger(BetaHandler.class);

    public void sendBetaMessage() {
        CubowApplication cubowApplication = new CubowApplication();
        JDA bot = cubowApplication.getJDA();

        Guild cubowServer = bot.getGuildById("1217994812108832880");

        TextChannel channel = cubowServer.getTextChannelById("1221229660021588129");

        String title = "Cubow Beta";
        String description = "Sichere dir jetzt Zugang zum Cubow Beta-Programm. " +
                "\n\nCubow kann nur aktiviert werden, wenn ein Benutzer mit Administratorrechten auf dem Server am Beta-Programm teilnimmt.";

        // Create the embed message
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(Color.MAGENTA);

        if (channel != null) {
            try {
                String channelID = channel.getLatestMessageId();
                // Edit the latest message embed with the updated values of embedBuilder

                Message message = channel.retrieveMessageById(channel.getLatestMessageId()).complete();

                MessageEmbed embed = message.getEmbeds().getFirst();

                // Only Edit if not equal
                if(!Objects.equals(embed.getTitle(), title) || !Objects.equals(embed.getDescription(), description)) {
                    channel.deleteMessageById(message.getId()).queue();
                    channel.sendMessageEmbeds(embedBuilder.build())
                            .setActionRow(Button.primary("betajoin", "Join Beta"))
                            .addActionRow(Button.link(bot.getInviteUrl(Permission.ADMINISTRATOR), "Invite"))
                            .queue();
                }
            } catch (Exception e) {

                channel.sendMessageEmbeds(embedBuilder.build())
                        .setActionRow(Button.primary("betajoin", "Join Beta"),
                                Button.link(bot.getInviteUrl(Permission.ADMINISTRATOR), "Invite"))
                        .queue();
            }
        } else {
            logger.info("Channel not found!");
        }
    }

    public void memberJoinedBeta(ButtonInteractionEvent event ) {
        // TODO add with Database
       /* CubowApplication cubowApplication = new CubowApplication();
        JDA bot = cubowApplication.getJDA();

        event.deferReply().setEphemeral(true).queue();
        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        EmbedBuilder eb = new EmbedBuilder();

        List<Document> betaMembers = dataBaseHandler.getAllBetaMembers();
        List<String> betaMemberIds = betaMembers.stream()
                .map(doc -> doc.getString("userID"))
                .collect(Collectors.toList());

        if(!betaMemberIds.contains(event.getMember().getId())) {
            if (betaMembers.stream().count() < 20) {
                Boolean sucess = dataBaseHandler.saveBetaMember(event.getMember());

                if (sucess) {
                    eb.setTitle("Joined Beta");
                    eb.setDescription("Du wurdest ins Beta Programm aufgenommen und kannst Cubow jetzt einladen!");
                    eb.setColor(Color.GREEN);

                    event.getHook().editOriginalEmbeds(eb.build())
                            .setActionRow(Button.link(bot.getInviteUrl(Permission.ADMINISTRATOR), "Invite"))
                            .queue();
                } else {
                    eb.setTitle("Fehler");
                    eb.setDescription("Etwas ist schief gelaufen");
                    eb.setColor(Color.RED);

                    event.getHook().editOriginalEmbeds(eb.build())
                            .setActionRow(Button.primary("ticket", "Ticket erstellen"))
                            .queue();
                }
            } else {
                eb.setTitle("Alle Plätze sind belegt");
                eb.setDescription("Leider schaffen unsere Server diesen Ansturm nicht und wir müssen upgraden. Wir benachrichtigen dich aber sobald wieder" +
                        "neue Plätze da sind");
                eb.setImage("https://i.imgur.com/9jD9bSF.jpg");
                eb.setColor(Color.RED);

                event.getHook().editOriginalEmbeds(eb.build())
                        .queue();
            }
        } else {
            eb.setTitle("Bereits registriert");
            eb.setDescription("Du bist bereits registriert");
            eb.setColor(Color.RED);

            event.getHook().editOriginalEmbeds(eb.build())
                    .setActionRow(Button.link("https://cubow.nimawoods.de/invite", "Cubow einladen"))
                    .queue();
        } */
    }

    public void joinedServer(GuildJoinEvent event) {
        /* DataBaseHandler dataBaseHandler = new DataBaseHandler();
        EmbedBuilder eb = new EmbedBuilder();

        Guild guild = event.getGuild();

        List<Document> betaMembers = dataBaseHandler.getAllBetaMembers();
        List<String> betaMemberIds = betaMembers.stream()
                .map(doc -> doc.getString("userID"))
                .collect(Collectors.toList());

        List<Member> adminMembers = guild.getMembers().stream()
                .filter(member -> member.getRoles().stream()
                        .anyMatch(role -> role.getName().equalsIgnoreCase("admin")))
                .collect(Collectors.toList());

        boolean isAdminBetaMember = adminMembers.stream()
                .anyMatch(member -> betaMemberIds.contains(member.getId()));

        if (isAdminBetaMember) {
            logger.info("Cubow joined server " + event.getGuild().getName());
        } else {
            TextChannel textChannel = (TextChannel) guild.getDefaultChannel();

            eb.setTitle("Cubow Bot");
            eb.setDescription("Jemand hat gerade versucht, Cubow einzuladen, obwohl keiner der Administratoren dieses Servers am Cubow-Betaprogramm teilnimmt." +
                    "Derzeit verfügen wir über eine begrenzte Bandbreite und müssen das Interesse an Cubow abschätzen. Daher gewähren wir derzeit nur Zugang zur Beta." +
                    "\n\n" +
                    "Wenn du Interesse an Betaplätzen oder allgemeinen Updates zu Cubow hast, kannst du unserem offiziellen Discord-Server beitreten. " +
                    "Dort kannst du Cubow ausprobieren oder der Beta beitreten.");
            eb.setColor(Color.MAGENTA);

            textChannel.sendMessageEmbeds(eb.build())
                    .setActionRow(Button.link("https://discord.gg/xHYD4Bm5x6", "Cubow Discord"))
                    .queue();

            guild.leave().queue();
        }*/
    }

}
