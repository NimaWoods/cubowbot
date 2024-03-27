package com.cubowbot.cubow.handler;

import com.cubowbot.cubow.CubowApplication;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseHandler.class);
    private MongoDatabase mongoDatabase;

    public DataBaseHandler(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    public DataBaseHandler() {
        String uri = "mongodb://5.45.109.197:27017/cubowbot";
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .build();
        logger.info("Connecting to Database");
        logger.info("Testing connection...");
        MongoClient mongoClient = MongoClients.create(settings);
        this.mongoDatabase = mongoClient.getDatabase("cubowbot");
    }


    public void saveDocument(String collectionName, Document document) {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            collection.insertOne(document);
            logger.info("Document inserted successfully into collection: {}", collectionName);
        } catch (Exception e) {
            logger.error("Error while saving document: {}", e.getMessage());
        }
    }

    public List<Document> getAllDocuments(String collectionName) {
        List<Document> documents = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            documents = collection.find().into(new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error while retrieving documents from collection {}: {}", collectionName, e.getMessage());
        }
        return documents;
    }

    public void updateDocument(String collectionName, String documentID, String fieldName, String fieldValue) {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            Document query = new Document("_id", documentID);
            Document update = new Document("$set", new Document(fieldName, fieldValue));
            collection.updateOne(query, update);
            logger.info("Document updated successfully in collection: {}", collectionName);
        } catch (Exception e) {
            logger.error("Error while updating document: {}", e.getMessage());
        }
    }

    public void deleteDocument(String collectionName, String documentID) {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            Document query = new Document("_id", documentID);
            collection.deleteOne(query);
            logger.info("Document deleted successfully from collection: {}", collectionName);
        } catch (Exception e) {
            logger.error("Error while deleting document: {}", e.getMessage());
        }
    }


    // ------------------------------------------------------------------------------------------
    // Beta Member
    public boolean saveBetaMember(Member member) {
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection("betamember");
            logger.info("Connected to Collection");

            Document document = new Document("userID", member.getId())
                    .append("username", member.getNickname())
                    .append("joinedAt", LocalDateTime.now());

            collection.insertOne(document);
            logger.info("Document inserted successfully.");

            return true;
        } catch (Exception e) {
            logger.error("Error while saving beta member: {}", e.getMessage());

            return false;
        }
    }

    public List<Document> getAllBetaMembers() {
        List<Document> betaMembers = new ArrayList<>();
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection("betamember");

            betaMembers = collection.find().into(new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error while retrieving beta members: {}", e.getMessage());
        }
        return betaMembers;
    }
}