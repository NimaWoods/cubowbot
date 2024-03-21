package com.cubowbot.cubow.Handler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

public class GiveawayHandler {
    public String pickWinner() {
        String winnerId = null;
        try {
            String content = new String(Files.readAllBytes(Paths.get("users.json")));
            JSONArray users = new JSONArray(content);
        
            int randomIndex = new Random().nextInt(users.length());
            JSONObject winner = users.getJSONObject(randomIndex);
        
            winnerId = winner.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            java.nio.file.Path filePath = Paths.get("users.json");
            boolean isDeleted = Files.deleteIfExists(filePath);
            if (isDeleted) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("File does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return winnerId;
    }
}
