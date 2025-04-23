package com.yourcompany.mongodbsource;

import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.apache.kafka.common.config.ConfigDef;

import java.util.*;

public class MongoSourceConnector extends SourceConnector {

    private Map<String, String> configProps;

    @Override
    public void start(Map<String, String> props) {
        // Save configuration properties for use in taskConfigs()
        this.configProps = new HashMap<>(props);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return MongoSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        // Create a list of identical configs for each task
        List<Map<String, String>> taskConfigs = new ArrayList<>();
        for (int i = 0; i < maxTasks; i++) {
            taskConfigs.add(new HashMap<>(configProps));
        }
        return taskConfigs;
    }

    @Override
    public void stop() {
        // Optional cleanup logic can be added here
    }

    @Override
    public ConfigDef config() {
        // Define required connector configuration fields
        return new ConfigDef()
            .define("mongodb.uri", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "MongoDB URI")
            .define("mongodb.database", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "MongoDB Database")
            .define("mongodb.collection", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "MongoDB Collection")
            .define("topic", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Kafka Topic to publish data to");
    }

    @Override
    public String version() {
        return "1.0";
    }
}
