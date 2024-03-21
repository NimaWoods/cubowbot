package com.cubowbot.cubow.Handler;

import io.github.artemnefedov.javaai.exception.JavaAIException;
import io.github.artemnefedov.javaai.service.JavaAI;
import net.dv8tion.jda.api.entities.Guild;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static io.github.artemnefedov.javaai.service.JavaAI.javaAiBuilder;

public class ChatGPTHandler {
    public String generateText(String message, Guild server) {


        String url = "https://api.openai.com/v1/chat/completions";
        String model = "gpt-4";
        String apikey = ConfigHandler.getServerConfig(server.getId(), "ChatGPT_API_Token");

        try {
            return makeRequest(url, apikey, model, message, server);
        } catch (IOException e) {
            if (e.getMessage().contains("HTTP response code: 400")) {

                // Switch to a different model and retry
                String fallbackModel = "gpt-3.5-turbo-0125";
                System.out.println("Rate limit exceeded. Switching to model: " + fallbackModel);

                try {
                    return makeRequest(url, apikey, fallbackModel, message, server);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                try {
                    throw e;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private String makeRequest(String url, String apikey, String model, String message, Guild server) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + apikey);
        con.setRequestProperty("Content-Type", "application/json");

        String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
        con.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(body);
        writer.flush();
        writer.close();

        // Get the response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // returns the extracted contents of the response.
        return extractContentFromResponse(response.toString().replace("\\n", "\n"));
    }

    public String generateImage(String prompt, Guild server) {

        ConfigHandler configHandler = new ConfigHandler();

        String token = configHandler.getServerConfig(server.getId(), "ChatGPT_API_Token");

        try {
            JavaAI javaAI = javaAiBuilder(token);
            String imgURl = javaAI.generateImage(prompt);

            return imgURl;

        } catch (JavaAIException e) {

            String error = e.toString();

            if (error.contains("content_policy_violation")) {
                return "Content Policy Violation: Your request was rejected as a result of the Dall-E safety system. Your prompt may contain text that is not allowed by the Dall-E safety system.";
            } else {
                throw new RuntimeException(error);
            }
        }
    }

    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content")+11;
        int endMarker = response.indexOf("\"", startMarker);
        return response.substring(startMarker, endMarker);
    }
}
