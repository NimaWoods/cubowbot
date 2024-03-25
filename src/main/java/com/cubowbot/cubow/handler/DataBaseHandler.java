package com.cubowbot.cubow.handler;

import com.cubowbot.cubow.CubowApplication;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class DataBaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(CubowApplication.class);

    public void connect() {
        String uri = "mongodb://localhost:27017/cubowbot";

        MongoClient mongoClient = null;

        try {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(uri))
                    .build();
            logger.info("Connected to Database");
            mongoClient = MongoClients.create(settings);

            MongoDatabase mongoDatabase = mongoClient.getDatabase("cubowbot");

            MongoCollection<Document> collection = mongoDatabase.getCollection("data");
            logger.info("Connected to Collection");

            logger.info("Creating Documents with Basedata...");
            Document document = new Document("userID", "303571400164245504");
            document.append("joinedAt", LocalDateTime.now());

            collection.insertOne(document);

            mongoClient.close();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}