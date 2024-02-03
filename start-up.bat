echo "Getting kafka image from confluentinc/cp-kafka"

docker pull confluentinc/cp-kafka

echo "Starting kafka with zookeeper"

docker compose -f kafka-stack-docker-compose/zk-single-kafka-single.yml up