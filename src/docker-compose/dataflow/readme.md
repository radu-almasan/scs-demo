# Dataflow with Docker Compose

## Get

```shell
curl https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose.yml > docker-compose.yml
curl https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose-kafka.yml > docker-compose-kafka.yml
curl https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose-mariadb.yml > docker-compose-mariadb.yml
curl https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose/docker-compose-prometheus.yml > docker-compose-prometheus.yml
```

## Start

```shell
export DATAFLOW_VERSION=2.11.4-jdk17
export DATAFLOW_VERSION_SIMPLE=2.11.4 # Prometheus
export SKIPPER_VERSION=2.11.4-jdk17
export HOST_MOUNT_PATH=~/.m2 
export DOCKER_MOUNT_PATH=/root/.m2
docker compose -f docker-compose.yml -f docker-compose-kafka.yml -f docker-compose-mariadb.yml -f docker-compose-prometheus.yml up
```

## Play

Listen to the `output` topic:

```shell
docker exec -i dataflow-kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic output
```

Write a simple message to the `input` topic:

```shell
echo 1 | docker exec -i dataflow-kafka kafka-console-producer --bootstrap-server localhost:9092 --topic input $1
```

## References

[Installing by Using Docker Compose](https://dataflow.spring.io/docs/installation/local/docker/)
