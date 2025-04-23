# 🧩 MongoDB Source Connector for Apache Kafka (Standalone)

This project is a custom MongoDB Source Connector** built in Java to stream data from MongoDB into a Kafka topic, using Kafka Connect in standalone mode and Docker.

---

- Tech Stack
- Java 17+
- Apache Kafka
- Kafka Connect (Standalone mode)
- MongoDB
- Maven
- Docker & Docker Compose

---

- Prerequisites

Make sure these tools are installed on your machine:

- [Java (JDK 17+)](https://adoptium.net/en-GB/temurin/releases/)
- [Maven](https://maven.apache.org/install.html)
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

Verify installs:

```bash
java -version
mvn -v
docker -v
docker compose version

---

- Project Structure

Unzip mongodb-source-project first

mongodb-source-project/
├── kafka-connect-runner/
│   ├── plugins/
│   ├── connect-standalone.properties
│   └── mongodb-source-connector.properties
├── mongodb-source-connector-scratch/
│   ├── src/main/java/com/yourcompany/mongodbsource/
│   ├── pom.xml
│   └── target/
├── Dockerfile.kafka-connect
└── docker-compose.yml

---
- If you want to rebuild the connector .jar file in the plugins folder, follow these instructions:

"cd mongodb-source-connector-scratch"
"mvn clean package"

This will generate a shaded .jar file:
"target/mongodb-source-connector-1.0-SNAPSHOT-shaded.jar"

Copy the .jar file into the plugin directory:
"cp mongodb-source-connector-scratch/target/mongodb-source-connector-1.0-SNAPSHOT-shaded.jar kafka-connect-runner/plugins/"

---

- Start Services

"docker compose up -d --build"
"docker ps" - until you see kafka-connect is listed as healthy

---

- Check Connector Status
"curl http://localhost:8083/connectors/mongodb-source-connector/status | jq"

---

- Seed MongoDB with Sample Data
"docker exec -it mongodb mongosh"
Inside the mongo shell:
"
use test
db.messages.insertOne({ text: "Hello from Kafka Connector", timestamp: new Date() })
"
Consume from kafka on a seperate terminal:
"
docker exec -it kafka kafka-console-consumer \
  --bootstrap-server kafka:9092 \
  --topic test-mongo-topic \
  --from-beginning
"
You should see your MongoDB documents in Kafka

---

- Troubleshooting

Logs:
"docker logs -f kafka-connect"

Reset Containers:
"
docker compose down -v --remove-orphans
docker compose up -d --build
"

Use the following email if any other queries arise:
Email: omar.m.waseem@gmail.com