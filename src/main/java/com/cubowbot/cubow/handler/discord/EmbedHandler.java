package com.cubowbot.cubow.handler.discord;

import com.cubowbot.cubow.CubowApplication;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Widget.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EmbedHandler {
    private static final Logger logger = LoggerFactory.getLogger(EmbedHandler.class);

    private final SlashCommandInteractionEvent event;

    public EmbedHandler(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void embedcreate() {
        Member member = event.getMember();

        String moderatorRole = ConfigHandler.getServerConfig(event.getGuild().getId(),"Moderation_Roles");

        if (member.getRoles().stream().anyMatch(role -> role.getId().equals(moderatorRole))) {

            // Send "Bot is thinking..."
            event.deferReply().setEphemeral(true).queue();

            String title = event.getOption("title").getAsString();
            String titlelink = event.getOption("titlelink") != null ? event.getOption("titlelink").getAsString() : null;
            String description = event.getOption("description").getAsString();
            Attachment image = event.getOption("image") != null ? event.getOption("image").getAsAttachment() : null;
            String footer = event.getOption("footer") != null ? event.getOption("footer").getAsString() : null;
            Attachment footerimage = event.getOption("footerimage") != null ? event.getOption("footerimage").getAsAttachment() : null;
            String author = event.getOption("author") != null ? event.getOption("author").getAsString() : null;
            Attachment authorimage = event.getOption("authorimage") != null ? event.getOption("authorimage").getAsAttachment() : null;
            Attachment thumbnail = event.getOption("thumbnail") != null ? event.getOption("thumbnail").getAsAttachment() : null;

            OptionMapping colorOption = event.getOption("color");
            String colorString = colorOption != null ? colorOption.getAsString() : null;

            Color awtColor = null;

            try {
                // Read the JSON file
                //Todo make colors,json to dependency
                String json = new String(Files.readAllBytes(Paths.get("colors.json")));

                // Parse the JSON file
                JsonArray colorsArray = JsonParser.parseString(json).getAsJsonArray();

                // Create a map to store the color names and their hex values
                Map<String, String> colorMap = new HashMap<>();

                // Iterate over the JSON array and add each color name and hex value to the map
                for (JsonElement colorElement : colorsArray) {
                    JsonObject colorObject = colorElement.getAsJsonObject();
                    String colorName = colorObject.get("color").getAsString();
                    String colorHex = colorObject.get("hex").getAsString();
                    colorMap.put(colorName, colorHex);
                }

                String colorNameToSearch = colorString;
                String relatedHex = colorMap.get(colorNameToSearch);

                if (relatedHex != null) {
                    awtColor = Color.decode(relatedHex);
                } else {
                    logger.info("Color not found in the JSON file.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            GuildChannelUnion guildChannelUnion = event.getOption("channel") != null ? event.getOption("channel").getAsChannel() : null;
            TextChannel channel = (TextChannel) guildChannelUnion;

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(title, titlelink);
            eb.setDescription(description);
            eb.setImage(image != null ? image.getUrl() : null);
            eb.setFooter(footer, footerimage != null ? footerimage.getUrl() : null);
            eb.setAuthor(author, authorimage != null ? authorimage.getUrl() : null);
            eb.setColor(awtColor);
            eb.setThumbnail(thumbnail != null ? thumbnail.getUrl() : null);

            if (channel != null) {
                channel.sendMessageEmbeds(eb.build()).queue( message -> {
                    event.getHook().editOriginal("Embed created in channel " + message.getJumpUrl()).queue(reply -> {
                        reply.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                });
            } else {
                event.getChannel().sendMessageEmbeds(eb.build()).queue();
                event.getHook().editOriginal("Embed created").queue(message -> {
                    message.delete().queueAfter(5, TimeUnit.SECONDS);
                });
            }
        } else {
            event.reply("You dont have the permissions to use this Command.").setEphemeral(true).queue();
        }
    }

    public void embededit() {
        Member member = event.getMember();

        if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {

            // Send "Bot is thinking..."
            event.deferReply().setEphemeral(true).queue();

            String title = event.getOption("title") != null ? event.getOption("title").getAsString() : null;
            String titlelink = event.getOption("titlelink") != null ? event.getOption("titlelink").getAsString() : null;
            String description = event.getOption("description") != null ? event.getOption("description").getAsString() : null;
            Attachment image = event.getOption("image") != null ? event.getOption("image").getAsAttachment() : null;
            String footer = event.getOption("footer") != null ? event.getOption("footer").getAsString() : null;
            Attachment footerimage = event.getOption("footerimage") != null ? event.getOption("footerimage").getAsAttachment() : null;
            String author = event.getOption("author") != null ? event.getOption("author").getAsString() : null;
            Attachment authorimage = event.getOption("authorimage") != null ? event.getOption("authorimage").getAsAttachment() : null;
            Attachment thumbnail = event.getOption("thumbnail") != null ? event.getOption("thumbnail").getAsAttachment() : null;
            String messageID = event.getOption("messageid").getAsString();

            GuildChannelUnion guildChannelUnion = event.getOption("channel") != null ? event.getOption("channel").getAsChannel() : null;
            TextChannel channel = (TextChannel) guildChannelUnion;

            channel.retrieveMessageById(messageID).queue(message -> {

                List<MessageEmbed> embeds = message.getEmbeds();

                OptionMapping colorOption = event.getOption("color");
                String colorString = colorOption != null ? colorOption.getAsString() : null;

                Color awtColor = null;

                if (colorString != null) {

                    try {
                        // Read the JSON file
                        String json = new String(Files.readAllBytes(Paths.get("colors.json")));

                        // Parse the JSON file
                        JsonArray colorsArray = JsonParser.parseString(json).getAsJsonArray();

                        // Create a map to store the color names and their hex values
                        Map<String, String> colorMap = new HashMap<>();

                        // Iterate over the JSON array and add each color name and hex value to the map
                        for (JsonElement colorElement : colorsArray) {
                            JsonObject colorObject = colorElement.getAsJsonObject();
                            String colorName = colorObject.get("color").getAsString();
                            String colorHex = colorObject.get("hex").getAsString();
                            colorMap.put(colorName, colorHex);
                        }

                        String colorNameToSearch = colorString;
                        String relatedHex = colorMap.get(colorNameToSearch);

                        if (relatedHex != null) {
                            awtColor = Color.decode(relatedHex);
                        } else {
                            logger.info("Color not found in the JSON file.");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                // Set old values to only change the given ones

                EmbedBuilder eb = new EmbedBuilder();
                MessageEmbed embed = embeds.get(0);

                String titleTextOld = embed.getTitle();
                String titlelinkOld = embed.getUrl();

                if (titleTextOld != null && titlelinkOld != null) {
                    eb.setTitle(titleTextOld, titlelinkOld);
                } else if (titleTextOld != null) {
                    eb.setTitle(titleTextOld);
                }

                String descriptionOld = embed.getDescription();
                if (descriptionOld != null) {
                    eb.setDescription(descriptionOld);
                }

                MessageEmbed.ImageInfo imageInfoOld = embed.getImage();
                if (imageInfoOld != null) {
                    String imageOld = imageInfoOld.getUrl();
                    if (imageOld != null) {
                        eb.setImage(imageOld);
                    }
                }

                MessageEmbed.Footer footerOld = embed.getFooter();
                String footerTextOld = null;
                String footerImageOld = null;

                if (footerOld != null) {
                    footerTextOld = footerOld.getText();
                    footerImageOld = footerOld.getIconUrl();

                    if (footerTextOld != null && footerImageOld != null) {
                        eb.setFooter(footerTextOld, footerImageOld);
                    } else if (footerTextOld != null) {
                        eb.setFooter(footerTextOld);
                    }
                }

                MessageEmbed.AuthorInfo authorOld = embed.getAuthor();
                String authorNameOld = null;
                String authorImageOld = null;

                if (authorOld != null) {
                    authorNameOld = authorOld.getName();
                    authorImageOld = authorOld.getIconUrl();

                    if (authorNameOld != null && authorImageOld != null) {
                        eb.setAuthor(authorNameOld, null, authorImageOld);
                    } else if (authorNameOld != null) {
                        eb.setAuthor(authorNameOld, null, null);
                    }
                }

                java.awt.Color colorOld = embed.getColor();
                if (colorOld != null) {
                    eb.setColor(colorOld);
                }

                MessageEmbed.Thumbnail thumbnailOld = embed.getThumbnail();
                if (thumbnailOld != null) {
                    eb.setThumbnail(thumbnailOld.getUrl());
                }

                // Set new values

                if (title != null && titlelink != null) {
                    eb.setTitle(title, titlelink);
                } else if (title != null) {
                    eb.setTitle(title);
                } else if (titlelink != null) {
                    String titleText = embed.getTitle();
                    eb.setTitle(titleText, titlelink);
                }

                if (description != null) {
                    eb.setDescription(description);
                }

                if (image != null) {
                    eb.setImage(image.getUrl());
                }

                if (footer != null && footerimage != null) {
                    eb.setFooter(footer, footerimage.getUrl());
                } else if (footer != null) {
                    eb.setFooter(footer);
                } else if (footerimage != null) {
                    String footerText = embed.getFooter().getText();
                    eb.setFooter(footerText, footerimage.getUrl());
                }

                if (author != null && authorimage != null) {
                    eb.setAuthor(author, authorimage.getUrl());
                } else if (author != null) {
                    eb.setAuthor(author);
                } else if (authorimage != null) {
                    String authorName = embed.getAuthor().getName();
                    eb.setFooter(authorName, authorimage.getUrl());
                }

                if (awtColor != null) {
                    eb.setColor(awtColor);
                }

                if (thumbnail != null) {
                    eb.setThumbnail(thumbnail.getUrl());
                }

                channel.editMessageEmbedsById(messageID, eb.build()).queue( embedEdit -> {
                    event.getHook().editOriginal("Embed " + embedEdit.getJumpUrl() + " edited").queue(reply -> {
                        reply.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                });
            }, failure -> {
                // This code will be executed if the message could not be retrieved
                event.reply("Message not found").setEphemeral(true).queue();
            });
        } else {
            event.reply("You dont have the permissions to use this Command.").setEphemeral(true).queue();
        }
    }

    public void embedaddfield() {
        Member member = event.getMember();
        try {
            if (member.getRoles().stream().anyMatch(role -> role.getId().equals("1079231987090469016"))) {
                String messageID = event.getOption("messageid").getAsString();
                String fieldtitle = event.getOption("fieldtitle").getAsString();
                String fielddescription = event.getOption("fielddescription").getAsString();
                Boolean inline = event.getOption("inline").getAsBoolean();

                MessageChannel channel = null;
                GuildChannelUnion guildChannelUnion = event.getOption("channel") != null ? event.getOption("channel").getAsChannel() : null;

                if (guildChannelUnion instanceof TextChannel) {
                    channel = (TextChannel) guildChannelUnion;
                } else if (guildChannelUnion instanceof NewsChannel) {
                    channel = (NewsChannel) guildChannelUnion;
                } else if (guildChannelUnion instanceof VoiceChannel) {
                    event.reply("Please select a Text Channel").setEphemeral(true).queue();
                }

                channel.retrieveMessageById(messageID).queue(message -> {

                    List<MessageEmbed> embeds = message.getEmbeds();

                    EmbedBuilder eb = new EmbedBuilder();
                    MessageEmbed embed = embeds.get(0);

                    String titleTextOld = embed.getTitle();
                    String titlelinkOld = embed.getUrl();

                    if (titleTextOld != null && titlelinkOld != null) {
                        eb.setTitle(titleTextOld, titlelinkOld);
                    } else if (titleTextOld != null) {
                        eb.setTitle(titleTextOld);
                    } else if (titlelinkOld != null) {
                        eb.setTitle(titleTextOld, titlelinkOld);
                    }

                    String descriptionOld = embed.getDescription();
                    eb.setDescription(descriptionOld);

                    String imageOld = null;

                    if (embed.getImage() != null) {
                        imageOld = embed.getImage().getUrl();
                        eb.setImage(imageOld);
                    }

                    MessageEmbed.Footer footerOld = embed.getFooter();
                    String footerTextOld = null;
                    String footerImageOld = null;

                    if (footerOld != null) {
                        footerTextOld = footerOld.getText();
                        footerImageOld = footerOld.getIconUrl();


                        if (footerTextOld != null && footerImageOld != null) {
                            eb.setFooter(footerTextOld, footerImageOld);
                        } else if (footerTextOld != null) {
                            eb.setFooter(footerTextOld);
                        } else if (footerImageOld != null) {
                            eb.setFooter(footerTextOld, footerImageOld);
                        }
                    }

                    MessageEmbed.AuthorInfo AuthorOld = embed.getAuthor();
                    String authorNameOld = null;
                    String authorImageOld = null;

                    if (AuthorOld != null) {
                        authorNameOld = AuthorOld.getName();
                        authorImageOld = AuthorOld.getIconUrl();


                        if (authorNameOld != null && authorImageOld != null) {
                            eb.setFooter(authorNameOld, authorImageOld);
                        } else if (authorNameOld != null) {
                            eb.setFooter(authorNameOld);
                        } else if (authorImageOld != null) {
                            eb.setFooter(authorNameOld, authorImageOld);
                        }
                    }

                    java.awt.Color colorOld = embed.getColor();
                    eb.setColor(colorOld);

                    MessageEmbed.Thumbnail thumbnailOld = embed.getThumbnail();
                    if (thumbnailOld != null) {
                        eb.setThumbnail(thumbnailOld.getUrl());
                    }

                    // get all existing fields
                    List<MessageEmbed.Field> fieldsOld = embed.getFields();
                    if (fieldsOld != null) {
                        for (MessageEmbed.Field field : fieldsOld) {
                            eb.addField(field);
                        }
                    }

                    eb.addField(fieldtitle, fielddescription, inline);
                    message.editMessageEmbeds(eb.build()).queue(edit -> {
                        event.reply("Added Field to Embed").setEphemeral(true).queue();
                    });
                });
            } else {
                event.reply("You dont have the permissions to use this Command.").setEphemeral(true).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
