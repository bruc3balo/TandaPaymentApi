# Process Flow
![Transaction Process Flow](docs/sequence_diagram.png)

### [Swagger](http://localhost:6793/payment-api/swagger-ui/index.html)
Open Api Swagger documentation

# MariaDB
```bash
docker compose up -f mariadb/maria-db-docker-compose.yaml -d
```

## Schema

![SQL database schema](docs/database_schema.png)


# Kafka
The kafka group.id is **gw_payment**

```bash
docker compose up -f kafka/kafka-docker-compose.yaml -d
```

# Deploying

Code changes will be picked up by building a new image or passing --build service to **docker compose up**

### Start Whole Infrastructure (For First Time Builds)
```bash
docker compose -f payment-docker-compose.yaml up --build -d
```

### Stop The Whole Infrastructure
```bash
docker compose -f payment-docker-compose.yaml down -v   
```

### Start without building image (After Successful Initial Build)
```bash
docker compose -f payment-docker-compose.yaml up -d
```

### Stop without deleting volume
```bash
docker compose -f payment-docker-compose.yaml down
```

### Watch Logs
```bash
docker compose -f payment-docker-compose.yaml logs -f
```

# Environment Variables
The following environment variables need to be provided in order for the application to run smoothly. 

[Application Environment variables](.env)
```.dotenv
spring.datasource.protocol=
spring.datasource.host=
spring.datasource.username=
spring.datasource.password=
spring.datasource.database=
spring.datasource.port=
spring.datasource.driver-class-name=
spring.jpa.hibernate.ddl-auto=
daraja.auth.url=
daraja.b2c.url=
daraja.result.url=
daraja.key=
daraja.secret=
initiator.name=
security.credential=
business.short-code=
kafka.servers=
```

[MariaDB Environment variables](mariadb/.env)
```dotenv
MYSQL_ROOT_PASSWORD=
MYSQL_DATABASE=
MYSQL_USER=
MYSQL_PASSWORD=
```

[Kafka Environment variables](kafka/.env)
```dotenv
# Kafka
KAFKA_ZOOKEEPER_CONNECT=
KAFKA_ADVERTISED_LISTENERS=
KAFKA_LISTENERS=
KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=
KAFKA_INTER_BROKER_LISTENER_NAME=
KAFKA_AUTO_CREATE_TOPICS_ENABLE=
KAFKA_NUM_PARTITIONS=
KAFKA_DEFAULT_REPLICATION_FACTOR=
KAFKA_BROKER_ID=
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=

# Zookeeper
ZOOKEEPER_CLIENT_PORT=
ZOOKEEPER_TICK_TIME=
ZOOKEEPER_SYNC_LIMIT=
```