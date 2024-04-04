package com.cubowbot.cubow.handler.discord;

import java.util.Random;

public class RandomizerHandler {

    Random random = new Random();

    public boolean randomBoolean(){
        return Math.random() < 0.5;
    }
}
