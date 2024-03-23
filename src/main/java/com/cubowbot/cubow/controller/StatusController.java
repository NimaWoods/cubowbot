package com.cubowbot.cubow.controller;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.Route;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StatusController {

    @GetMapping("/status")
    public String index(Model model) {

        CubowApplication cubowApplication = new CubowApplication();
        JDA bot = cubowApplication.getJDA();

        List<Guild> guilds = bot.getGuilds();

        model.addAttribute("logo", bot.getSelfUser().getAvatarUrl());

        model.addAttribute("botName", bot.getSelfUser().getName());
        model.addAttribute("botStatus", bot.getStatus().name());
        model.addAttribute("botGuilds", guilds.size());

        return "status";
    }
}
