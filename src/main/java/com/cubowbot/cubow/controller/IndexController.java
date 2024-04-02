package com.cubowbot.cubow.controller;

import com.cubowbot.cubow.CubowApplication;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {

        CubowApplication cubowApplication = new CubowApplication();
        JDA bot = cubowApplication.getJDA();

        if(bot.getStatus().name().equals("CONNECTED")) {
            model.addAttribute("botStatus", "Online");
        } else {
            model.addAttribute("botStatus", bot.getStatus().name());
        }

        return "comingsoon";
    }
}
