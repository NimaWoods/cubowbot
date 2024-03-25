package com.cubowbot.cubow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InviteController {
    @GetMapping("/invite")
    public String invite() {
        return "redirect:https://discord.com/oauth2/authorize?client_id=1217485873508253839";
    }
}