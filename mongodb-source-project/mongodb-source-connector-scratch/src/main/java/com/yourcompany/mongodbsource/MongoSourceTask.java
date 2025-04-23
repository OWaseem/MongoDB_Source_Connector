package com.yourcompany.mongodbsource;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.bson.Document;

import java.util.*;

public class MongoSourceTask extends SourceTask {

    private MongoClient mongoClient;
    private MongoCollection<Document> collection;
    private String topic;

    @Override
    public String version() {
        return "1.0";
    }

    @Override
    public void start(Map<String, String> props) {
        String mongoUri = props.get("mongodb.uri");
        String databaseName = props.get("mongodb.database");
        String collectionName = props.get("mongodb.collection");
        topic = props.get("topic");

        // Create MongoDB connection using the new driver
        ConnectionString connString = new ConnectionString(mongoUri);
        mongoClient = MongoClients.create(connString);
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        collection = database.getCollection(collectionName);
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        List<SourceRecord> records = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                // Convert Document to String (can customize)
                String value = doc.toJson();

                SourceRecord record = new SourceRecord(
                        Collections.singletonMap("source", "mongodb"),
                        Collections.singletonMap("offset", doc.getObjectId("_id").toHexString()),
                        topic,
                        Schema.STRING_SCHEMA,
                        value
                );

                records.add(record);
            }
        }

        Thread.sleep(5000); // Poll interval — adjust as needed
        return records;
    }

    @Override
    public void stop() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
