# Kafka Spring Test Application

Тестовое приложение на Java + Spring Boot для работы с Apache Kafka.

## Описание

Это приложение демонстрирует базовую интеграцию Spring Boot с Apache Kafka. Включает:
- Kafka Producer для отправки сообщений
- Kafka Consumer для получения сообщений
- REST API для тестирования
- Docker Compose для локального запуска Kafka
- Kafka UI для визуализации

## Требования

- Java 17 или выше
- Maven 3.6+
- Docker и Docker Compose (для локального запуска Kafka)

## Структура проекта

```
kafka-spring-test/
├── src/
│   └── main/
│       ├── java/com/example/kafka/
│       │   ├── KafkaTestApplication.java    # Главный класс
│       │   ├── config/                       # Конфигурация Kafka
│       │   ├── producer/                     # Producer сервис
│       │   ├── consumer/                     # Consumer сервис
│       │   ├── controller/                   # REST контроллеры
│       │   └── model/                        # Модели данных
│       └── resources/
│           └── application.properties        # Настройки приложения
├── docker-compose.yml                        # Docker Compose для Kafka
├── pom.xml                                   # Maven конфигурация
└── README.md
```

## Быстрый старт

### 1. Запуск Kafka локально

Запустите Kafka, Zookeeper и Kafka UI используя Docker Compose:

```bash
docker-compose up -d
```

Это запустит:
- **Zookeeper** на порту `2181`
- **Kafka** на порту `9092`
- **Kafka UI** на порту `8090` (доступен по адресу http://localhost:8090)

Проверить статус контейнеров:

```bash
docker-compose ps
```

### 2. Сборка приложения

```bash
mvn clean install
```

### 3. Запуск приложения

```bash
mvn spring-boot:run
```

или

```bash
java -jar target/kafka-spring-test-1.0.0.jar
```

Приложение запустится на порту `8080`.

## REST API Endpoints

### 1. Проверка статуса приложения

```bash
curl http://localhost:8080/api/kafka/health
```

### 2. Отправка простого сообщения

```bash
curl -X POST "http://localhost:8080/api/kafka/send?message=Hello Kafka"
```

### 3. Отправка сообщения в формате JSON

```bash
curl -X POST http://localhost:8080/api/kafka/send-object \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Test message from API"
  }'
```

### 4. Получение всех полученных сообщений

```bash
curl http://localhost:8080/api/kafka/messages
```

### 5. Получение количества сообщений

```bash
curl http://localhost:8080/api/kafka/messages/count
```

### 6. Очистка полученных сообщений

```bash
curl -X DELETE http://localhost:8080/api/kafka/messages
```

## Тестирование

### Простой тест отправки и получения:

1. Отправьте сообщение:
```bash
curl -X POST "http://localhost:8080/api/kafka/send?message=Test Message 1"
```

2. Подождите 1-2 секунды (для обработки Consumer)

3. Проверьте полученные сообщения:
```bash
curl http://localhost:8080/api/kafka/messages
```

### Просмотр Kafka через UI:

Откройте браузер и перейдите на http://localhost:8090

Здесь вы можете:
- Просматривать топики
- Читать сообщения
- Мониторить состояние Kafka

## Конфигурация

Основные настройки находятся в `src/main/resources/application.properties`:

```properties
# Kafka сервер
spring.kafka.bootstrap-servers=localhost:9092

# Consumer group
spring.kafka.consumer.group-id=kafka-test-group

# Имя топика
kafka.topic.name=test-topic

# Порт приложения
server.port=8080
```

## Остановка и очистка

### Остановка приложения
Нажмите `Ctrl+C` в терминале где запущено приложение

### Остановка Kafka
```bash
docker-compose down
```

### Полная очистка (включая данные)
```bash
docker-compose down -v
```

## Логирование

Приложение выводит подробные логи:
- Отправленные сообщения (Producer)
- Полученные сообщения (Consumer)
- Ошибки и предупреждения

Логи можно увидеть в консоли при запуске приложения.

## Архитектура

### Producer
- `KafkaProducerService` - отправляет сообщения в Kafka топик
- Поддерживает отправку строк и объектов Message
- Асинхронная отправка с callback для подтверждения

### Consumer
- `KafkaConsumerService` - слушает топик и получает сообщения
- Сохраняет полученные сообщения в памяти для просмотра через API
- Использует `@KafkaListener` для автоматической обработки

### Model
- `Message` - POJO класс для сообщений с полями:
  - `id` - уникальный идентификатор
  - `content` - содержимое сообщения
  - `timestamp` - время создания

## Troubleshooting

### Ошибка подключения к Kafka
- Убедитесь что Kafka запущена: `docker-compose ps`
- Проверьте доступность порта 9092: `netstat -an | grep 9092`

### Сообщения не доходят до Consumer
- Проверьте логи приложения
- Убедитесь что Consumer group ID настроен правильно
- Проверьте через Kafka UI что сообщения попадают в топик

### Порт 8080 уже занят
- Измените порт в `application.properties`: `server.port=8081`

## Дальнейшее развитие

Возможные улучшения:
- Добавить обработку нескольких топиков
- Реализовать Dead Letter Queue для ошибочных сообщений
- Добавить метрики и мониторинг
- Создать интеграционные тесты с Embedded Kafka
- Добавить обработку транзакций

## Автор

Создано с помощью Claude Code
