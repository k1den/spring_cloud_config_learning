# Spring Cloud Config с Kafka и PostgreSQL

Проект демонстрирует интеграцию Spring Cloud Config Server и Client с сохранением данных в PostgreSQL и отправкой сообщений в Kafka.

### 1. Настройка базы данных

```sql
-- Создание базы данных
CREATE DATABASE mydb;

-- Создание sequence для генерации ID
CREATE SEQUENCE message_sequence START 1;
```

### 2. Работа с контейнерами

```
-- Запуск контейнеров
docker-compose up -d

-- Остановка контейнеров
docker-compose down -v
```

### 3. Работа с кафкой

```
-- Создание топика
docker exec -it kafka bash
kafka-topics --create \
  --topic user-data-topic \
  --bootstrap-server localhost:9092 \
  --partitions 3 \
  --replication-factor 1

-- Просмотр сообщений
docker run --rm -it --network host confluentinc/cp-kafka:7.5.0 \
  kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic user-data-topic \
  --from-beginning
```

### Порядок запуска

ВАЖНО!!! Запускайте сервисы в строгом порядке:
1. Config Server
2. Config Client
3. Поднимите контейнер с Kafka и Zookeeper

### Тестирование

Выполнить GET запрос:
```
http://localhost:8081/api/config
```

Выполнить POST запрос через Postman по адресу(данные вводите через Body->raw, формат JSON):
```
http://localhost:8081/api/data
```

Пример ввода данных:
```json
{
  "name": "Максим",
  "message": "Привет, я Максим",
  "email": "maksim@example.com"
}
```
