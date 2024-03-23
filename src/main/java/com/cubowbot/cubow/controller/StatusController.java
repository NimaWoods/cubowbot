package com.cubowbot.cubow.controller;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusController {

    @GetMapping("/status")
    public String index(Model model) {

        CubowApplication cubowApplication = new CubowApplication();
        JDA bot = cubowApplication.getJDA();

        model.addAttribute("logo", bot.getSelfUser().getAvatarUrl());

        model.addAttribute("botName", bot.getSelfUser().getName());
        model.addAttribute("botStatus", bot.getStatus().name());

        return "status";
    }
}
