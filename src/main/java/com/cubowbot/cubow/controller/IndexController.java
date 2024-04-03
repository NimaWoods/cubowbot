package com.cubowbot.cubow.controller;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {

        CubowApplication cubowApplication = new CubowApplication();
        JDA bot = cubowApplication.getJDA();

        long guilds = bot.getGuilds().stream().count();
        long totalUsers = bot.getGuilds().stream()
                .mapToLong(guild -> guild.getMemberCount())
                .sum();

        long count = 0;
        for (Command command : bot.retrieveCommands().complete()) {
            count++;
        }


        String logoURL = bot.getSelfUser().getAvatarUrl();
        logoURL = logoURL + "?size=512";

        model.addAttribute("logo", logoURL);
        model.addAttribute("guilds", guilds);
        model.addAttribute("commands", count);
        model.addAttribute("user", totalUsers);

        return "index";
    }
}
