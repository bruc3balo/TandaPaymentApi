### [Swagger](http://localhost:6793/payment-api/swagger-ui/index.html)
Open Api Swagger documentation

# MariaDB
```bash
docker compose up -f mariadb/maria-db-docker-compose.yaml -d
```

## Schema

![SQL database schema](docs/database_schema.png)


# Kafka
```bash
docker compose up -f kafka/kafka-docker-compose.yaml -d
```

# Deploying

Code changes will be picked up by building a new image or passing --build service to **docker compose up**

### Start Whole Infrastructure
```bash
docker compose -f payment-docker-compose.yaml down up --build -d
```

### Stop The Whole Infrastructure
```bash
docker compose -f payment-docker-compose.yaml down -v 
```

### Start without building
```bash
docker compose -f payment-docker-compose.yaml up -d
```

### Watch Logs
```bash
docker compose -f payment-docker-compose.yaml logs -f
```

# Process
![Transaction Process Flow](docs/sequence_diagram.png)
