

     _________  ________  ________   ________  ________          ________  ________      ___    ___ _____ ______   _______   ________   _________        ________  ________  ___
    |\___   ___\\   __  \|\   ___  \|\   ___ \|\   __  \        |\   __  \|\   __  \    |\  \  /  /|\   _ \  _   \|\  ___ \ |\   ___  \|\___   ___\     |\   __  \|\   __  \|\  \
    \|___ \  \_\ \  \|\  \ \  \\ \  \ \  \_|\ \ \  \|\  \       \ \  \|\  \ \  \|\  \   \ \  \/  / | \  \\\__\ \  \ \   __/|\ \  \\ \  \|___ \  \_|     \ \  \|\  \ \  \|\  \ \  \
         \ \  \ \ \   __  \ \  \\ \  \ \  \ \\ \ \   __  \       \ \   ____\ \   __  \   \ \    / / \ \  \\|__| \  \ \  \_|/_\ \  \\ \  \   \ \  \       \ \   __  \ \   ____\ \  \
          \ \  \ \ \  \ \  \ \  \\ \  \ \  \_\\ \ \  \ \  \       \ \  \___|\ \  \ \  \   \/  /  /   \ \  \    \ \  \ \  \_|\ \ \  \\ \  \   \ \  \       \ \  \ \  \ \  \___|\ \  \
           \ \__\ \ \__\ \__\ \__\\ \__\ \_______\ \__\ \__\       \ \__\    \ \__\ \__\__/  / /      \ \__\    \ \__\ \_______\ \__\\ \__\   \ \__\       \ \__\ \__\ \__\    \ \__\
            \|__|  \|__|\|__|\|__| \|__|\|_______|\|__|\|__|        \|__|     \|__|\|__|\___/ /        \|__|     \|__|\|_______|\|__| \|__|    \|__|        \|__|\|__|\|__|     \|__|  
                                                                                       \|___|/



## Process Flow
#### This was created with visual paradigm online

# [~ SIMULATE (CPS) Core Payment System Demo ~](https://github.com/bruc3balo/TandaCPSDemo)
There is a demo of the core payment system functionality to simulate the B2C transaction to the payment api integration.


![Transaction Process Flow](docs/sequence_diagram.png)

### [Open Swagger documentation](http://localhost:6793/payment-api/swagger-ui/index.html)
#### Click above to open open ai documentation

# MariaDB
##### Deploy maria db individually
```bash
docker compose up -f mariadb/maria-db-docker-compose.yaml -d
```

## Schema
This was generated by intellij

![SQL database schema](docs/database_schema.png)


# Kafka
The kafka group.id is **gw_payment**.

The topics are **gw_request** and **gw_response**
```bash
docker compose up -f kafka/kafka-docker-compose.yaml -d
```

# Deploying

Code changes will be picked up by building a new image or passing **--build** service to **docker compose up**

Necessary health checks have been put in place to ensure container uptime and ease of debugging

### Step 1: Create Docker Network
```bash
docker network create tanda_network
```

### Step 2: Deploy Whole Infrastructure
#### For initial build to create images i.e. No images build
```bash
docker compose -f payment-docker-compose.yaml up --build -d
```
#### Start all containers without building image i.e. After successful initial build
```bash
docker compose -f payment-docker-compose.yaml up -d
```

### Step 3: Watch Logs from all containers
```bash
docker compose -f payment-docker-compose.yaml logs -f
```

### Step 4: Clean up

#### Stop all containers without deleting volume
```bash
docker compose -f payment-docker-compose.yaml down
```

#### Prune The Whole Infrastructure

```bash
docker compose -f payment-docker-compose.yaml down -v   
```






# Environment Variables
### N/B: It's bad practice to push secrets to a repository. This is just to make testing easy

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


# Testing

#### A) Testing the api while building
##### DOCKER FILE

```bash
mvn surefire:test
```

#### B) Testing the api while running
##### Shell
```bash
curl -f http://localhost:6793/payment-api/ping
```

##### Host
```bash
docker exec tanda_payment_api curl -f http://localhost:6793/payment-api/ping
```


#### C) Testing mariadb
##### Shell
```bash
healthcheck.sh --connect --innodb_initialized
```

##### Host
```bash
docker exec payment_mariadb healthcheck.sh --connect --innodb_initialized
```

#### D) Testing kafka
##### Shell
```bash
kafka-topics --bootstrap-server=kafka:9092 --list
```

##### Host
```bash
docker exec payment_kafka kafka-topics --bootstrap-server=kafka:9092 --list
```

#### E) Testing zookeeper
##### Shell
```bash
nc -z localhost 2181
```
##### Host

```bash
docker exec payment_zookeeper nc -z localhost 2181
```