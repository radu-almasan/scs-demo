# Spring Cloud Streams Demo

## Build

```shell
docker compose -p scs -f src/docker-compose/app.yml up -d 
```

## Play

Listen to the `output` topic:

```shell
docker exec -i scs-kafka-1 kafka-console-consumer --bootstrap-server localhost:9092 --topic output
```

Write a simple message to the `input` topic:

```shell
echo 1 | docker exec -i scs-kafka-1 kafka-console-producer --bootstrap-server localhost:9092 --topic input $1
```