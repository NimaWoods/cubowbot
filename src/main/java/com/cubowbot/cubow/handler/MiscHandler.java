package com.cubowbot.cubow.handler;

import com.cubowbot.cubow.generator.EmbedGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.Color;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Objects;

public class MiscHandler {

    private final SlashCommandInteractionEvent event;

    public MiscHandler(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void avatar() {
        Member member = event.getOption("member").getAsMember();
        String avatarUrl = member.getEffectiveAvatarUrl();
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(member.getUser().getName() + "'s Avatar");
        eb.setColor(Color.MAGENTA);
        eb.setDescription("[png](" +avatarUrl + ") [jpg](" + avatarUrl.replace(".png", ".jpg") + ") [webp](" + avatarUrl.replace(".png", ".webp") + ")");
        eb.setImage(avatarUrl + "?size=1024");
        event.replyEmbeds(eb.build()).queue();

        //TODO Extend Embed Constructor
    }

    public void addemoji() {

        EmbedGenerator embedConstructor = new EmbedGenerator();

        Member member = event.getMember();
        if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
            OptionMapping imageOption = event.getOption("image");
            if (imageOption != null) {
                Message.Attachment attachment = imageOption.getAsAttachment();
                attachment.retrieveInputStream().thenAccept(inputStream -> {
                    Icon image = null;
                    try {
                        image = Icon.from(inputStream);
                        String name = event.getOption("name").getAsString();

                        event.getGuild().createEmoji(name, image).queue( emoji -> {
                                    event.reply(emoji.getAsMention()).queue();
                                },
                                error -> embedConstructor.failure(event, "Could not add emoji to Server!"+ "\n" + error.getMessage() + "\n Please note:\\n\" +\n" +
                                                "\"File type: The image must be in JPEG, PNG, or GIF format\\n\" +\n" +
                                                "\"\\n\" +\n" +
                                                "\"File size: The image must be under 256KB in size\\n\" +\n" +
                                                "\"\\n\" +\n" +
                                                "\"Dimensions: For optimal resolution, you can upload custom emojis in sizes up to 128×128 pixels, but they will be resized to 32×32 pixels when uploaded to Discord\\n\" +\n"
                                        , true)
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).exceptionally(throwable -> {
                    // Handle any exceptions here
                    throwable.printStackTrace();
                    return null;
                });
            }
        } else {
            embedConstructor.noPermissions(event);
        }
    }

    public void removeemoji() {
        EmbedGenerator embedConstructor = new EmbedGenerator();

        String emoji = event.getOption("emoji").getAsString();
        String emojiID = emoji.replaceAll("<:.*:(\\d+)>", "$1");
        Objects.requireNonNull(event.getGuild()).getEmojiById(emojiID).delete().queue(
                success -> embedConstructor.success(event, "Successfully removed emoji from Server!", true),
                error -> embedConstructor.failure(event, "Could not remove emoji from Server!"+ "\n" + error.getMessage(), true)
        );
    }

    public void serverstats() {
        Guild guild = event.getGuild();

        String serverName = guild.getName();
        String serverId = guild.getId();
        Integer memberCount = guild.getMemberCount();
        TextChannel rulesChannel = guild.getRulesChannel();
        OffsetDateTime timeCreated = guild.getTimeCreated();

        String banner = guild.getBannerUrl();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Server Stats");
        eb.setDescription("**Server:** " + serverName +  //
                "\n**Server Id:** " + serverId + //
                "\n**Members:** " + memberCount + //
                "\n**Rules Channel** "+ rulesChannel + //
                "**Created:** " + timeCreated);
        eb.setImage(banner);
        eb.setColor(Color.MAGENTA);
        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }

}
