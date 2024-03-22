package com.cubowbot.cubow.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.entities.Message;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonHandler {
    public void saveListToJson(List<Message> messages, String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(messages, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Message> loadFromJson(String filename) {
        try (FileReader fileReader = new FileReader(filename)) {
            Gson gson = new Gson();
            Type messageType = new TypeToken<List<Message>>() {}.getType();
            return gson.fromJson(fileReader, messageType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
