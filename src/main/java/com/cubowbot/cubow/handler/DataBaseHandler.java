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
import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(CubowApplication.class);
    private MongoClient mongoClient;

    public DataBaseHandler() {
        String uri = "mongodb://localhost:27017/cubowbot";
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .build();
        logger.info("Connecting to Database");
        mongoClient = MongoClients.create(settings);
    }

    // Test on Startup
    public void testConnection() {
        logger.info("Testing connection...");
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("cubowbot");
        } catch (Exception e) {
            logger.error("Database connection failed: " + e.toString());
        }
    }

    // Beta Member
    public void saveBetaMember(String userID) {
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("cubowbot");
            MongoCollection<Document> collection = mongoDatabase.getCollection("data");
            logger.info("Connected to Collection");

            logger.info("Creating Document with Basedata...");
            Document document = new Document("userID", userID)
                    .append("joinedAt", LocalDateTime.now());

            collection.insertOne(document);
            logger.info("Document inserted successfully.");
        } catch (Exception e) {
            logger.error("Error while saving beta member: {}", e.getMessage());
        }
    }

    public List<Document> getAllBetaMembers() {
        List<Document> betaMembers = new ArrayList<>();
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("cubowbot");
            MongoCollection<Document> collection = mongoDatabase.getCollection("data");

            betaMembers = collection.find().into(new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error while retrieving beta members: {}", e.getMessage());
        }
        return betaMembers;
    }

    public void updateBetaMember(String userID, String newStatus) {
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("cubowbot");
            MongoCollection<Document> collection = mongoDatabase.getCollection("data");

            Document query = new Document("userID", userID);
            Document update = new Document("$set", new Document("status", newStatus));

            collection.updateOne(query, update);
            logger.info("Beta member updated successfully.");
        } catch (Exception e) {
            logger.error("Error while updating beta member: {}", e.getMessage());
        }
    }

    public void deleteBetaMember(String userID) {
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("cubowbot");
            MongoCollection<Document> collection = mongoDatabase.getCollection("data");

            Document query = new Document("userID", userID);

            collection.deleteOne(query);
            logger.info("Beta member deleted successfully.");
        } catch (Exception e) {
            logger.error("Error while deleting beta member: {}", e.getMessage());
        }
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.info("Database connection closed.");
        }
    }
}