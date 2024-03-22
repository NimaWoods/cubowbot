package com.cubowbot.cubow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketViewController {
    @GetMapping("/ticket")
    public String ticket(Model model) {

        List<String> texts = new ArrayList<>();

        String name = "AAAA";
        String text = "TEXT";

        texts.add("<p>" + name + "</p><br/><p>" + text + "</p>");
        texts.add("<p>" + name + "</p><br/><p>" + text + "</p>");

        model.addAttribute("texts", texts);

        return "ticket";
    }
}
