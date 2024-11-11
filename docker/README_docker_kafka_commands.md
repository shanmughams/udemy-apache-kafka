
# Docker Commands for Kafka and ZooKeeper

This document provides essential Docker commands to set up and manage a Kafka environment with 3 brokers and ZooKeeper, using Docker Compose.

## Prerequisites

Ensure Docker and Docker Compose are installed on your system.

## Starting the Kafka Cluster

1. **Start the Kafka and ZooKeeper services**:
   ```bash
   cd D:\me\learn\udemy\kafka-springboot\kafka-practise\docker
   docker-compose -f docker-compose-multi-broker.yml up -d
   
   
   docker-compose up -d
   ```
   This command will start all services in detached mode (`-d`), which allows them to run in the background.

2. **Check if the services are running**:
   ```bash
   docker-compose ps
   ```
   This command displays the status of each container in the setup.

## Managing Individual Services

1. **Stop Kafka and ZooKeeper Services**:
   ```bash
   docker-compose down
   ```
   This command will stop and remove all containers created by the `docker-compose up` command.

2. **Restart the Kafka and ZooKeeper Services**:
   ```bash
   docker-compose restart
   ```

3. **Stop a Specific Broker**:
   ```bash
   docker stop <broker_container_name>
   ```
   Replace `<broker_container_name>` with the actual container name, which can be found using `docker ps`.

4. **Start a Specific Broker**:
   ```bash
   docker start <broker_container_name>
   ```

5. **Restart a Specific Broker**:
   ```bash
   docker restart <broker_container_name>
   ```

## Accessing Kafka Brokers and ZooKeeper CLI

1. **Run Kafka Console Producer**:
   Connect to a broker and start producing messages to a specific topic.
   ```bash
   docker exec -it <broker_container_name> kafka-console-producer.sh --broker-list <broker_name:port> --topic <topic_name>
   ```

2. **Run Kafka Console Consumer**:
   Connect to a broker and start consuming messages from a specific topic.
   ```bash
   docker exec -it <broker_container_name> kafka-console-consumer.sh --bootstrap-server <broker_name:port> --topic <topic_name> --from-beginning
   ```

3. **List Kafka Topics**:
   ```bash
   docker exec -it <broker_container_name> kafka-topics.sh --bootstrap-server <broker_name:port> --list
   ```

4. **Create a New Kafka Topic**:
   ```bash
   docker exec -it <broker_container_name> kafka-topics.sh --bootstrap-server <broker_name:port> --create --topic <topic_name> --partitions <number_of_partitions> --replication-factor <replication_factor>
   ```

5. **Describe a Kafka Topic**:
   ```bash
   docker exec -it <broker_container_name> kafka-topics.sh --bootstrap-server <broker_name:port> --describe --topic <topic_name>
   ```

## Checking Logs

1. **View Logs for a Specific Broker**:
   ```bash
   docker logs <broker_container_name>
   ```

2. **Follow Logs for Real-Time Updates**:
   ```bash
   docker logs -f <broker_container_name>
   ```

3. **View Logs for ZooKeeper**:
   ```bash
   docker logs <zookeeper_container_name>
   ```

## Scaling Kafka Brokers (Add More Brokers)

To add more brokers to the Kafka cluster, update your `docker-compose.yml` file with additional broker configurations, then run:

```bash
docker-compose up -d --scale kafka=<number_of_brokers>
```

For example, to scale to 5 brokers:
```bash
docker-compose up -d --scale kafka=5
```

---

## Stopping and Cleaning Up

1. **Stop All Services and Remove Containers**:
   ```bash
   docker-compose down
   ```

2. **Remove All Kafka Data (Use with Caution)**:
   This command removes all volumes associated with the Kafka setup.
   ```bash
   docker-compose down -v
   ```

This will ensure that both containers and volumes (including Kafka and ZooKeeper data) are removed, allowing for a fresh setup.

---

## Troubleshooting

- **Check for Network Issues**: If brokers are unable to communicate, ensure they’re in the same Docker network.
- **Verify Ports**: Ensure no other processes are using Kafka/ZooKeeper ports, as conflicts can cause startup issues.

---

These commands should help manage and troubleshoot your Dockerized Kafka setup effectively.
