package com.cubowbot.cubow.commands.CommandLists;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Arrays;
import java.util.List;

public class EmbedCommands {
    public List<CommandData> loadList() {

        List<CommandData> commandList = Arrays.asList(

                new CommandDataImpl("embedcreate", "Creates an embed")
                        .addOption(OptionType.STRING, "title", "Embed Titel", true)
                        .addOption(OptionType.STRING, "description", "Embed Beschreibung", true)
                        .addOption(OptionType.STRING, "color", "Embed color", true, true)
                        .addOption(OptionType.STRING, "titlelink", "Embed Titel Link")
                        .addOption(OptionType.ATTACHMENT, "image", "Embed Image")
                        .addOption(OptionType.STRING, "footer", "Embed Footer Text")
                        .addOption(OptionType.ATTACHMENT, "footerimage", "Embed Footer Image")
                        .addOption(OptionType.STRING, "author", "Embed Author Text")
                        .addOption(OptionType.ATTACHMENT, "authorimage", "Embed Author Image")
                        .addOption(OptionType.CHANNEL, "channel", "Channel to send the Embed in")
                        .addOption(OptionType.ATTACHMENT, "thumbnail", "Embed Thumbnail"),

                new CommandDataImpl("embededit", "Edits embed")
                        .addOption(OptionType.CHANNEL, "channel", "Channel of Embed", true)
                        .addOption(OptionType.STRING, "messageid", "ID of the Embed Message", true)
                        .addOption(OptionType.STRING, "title", "Embed Titel")
                        .addOption(OptionType.STRING, "description", "Embed Beschreibung")
                        .addOption(OptionType.STRING, "color", "Embed color", false, true)
                        .addOption(OptionType.STRING, "titlelink", "Embed Titel Link")
                        .addOption(OptionType.ATTACHMENT, "image", "Embed Image")
                        .addOption(OptionType.STRING, "footer", "Embed Footer Text")
                        .addOption(OptionType.ATTACHMENT, "footerimage", "Embed Footer Image")
                        .addOption(OptionType.STRING, "author", "Embed Author Text")
                        .addOption(OptionType.ATTACHMENT, "authorimage", "Embed Author Image")
                        .addOption(OptionType.ATTACHMENT, "thumbnail", "Embed Thumbnail"),

                new CommandDataImpl("embedaddfield", "Adds a Field to an Embed")
                        .addOption(OptionType.CHANNEL, "channel", "Channel of Embed", true)
                        .addOption(OptionType.STRING, "messageid", "ID of the Embed Message", true)
                        .addOption(OptionType.STRING, "fieldtitle", "Field Titel", true)
                        .addOption(OptionType.STRING, "fielddescription", "Field Description", true)
                        .addOption(OptionType.BOOLEAN, "inline", "Inline", true)

        );

        return commandList;
    }
}
