# Credit-Service

## О проекте

**Credit-Service** — это сервис для управления кредитными заявками и клиентами, реализованный на Spring Boot с поддержкой асинхронной обработки через Apache Kafka.

Сервис позволяет:
- Регистрировать и обновлять клиентов
- Создавать и отслеживать кредитные заявки
- Асинхронно обрабатывать статусы кредитов через Kafka
- Использовать Dead Letter Queue для отказоустойчивости

---

## Технологический стек
- **Java 17**
- **Spring Boot 3** (Web, Data JPA, Validation)
- **PostgreSQL**
- **Apache Kafka** (Spring for Apache Kafka)
- **Lombok**
- **Docker Compose** (для локального запуска инфраструктуры)
---

## Структура проекта

- `src/main/java/com/credit/credit/`
  - `controller/` 
  - `service/` 
  - `entity/` 
  - `repository/` 
  - `config/` 
  - `component/`
  - `dto/` 
  - `enums/`, `exception/`, `mapper/`, `util/`
- `src/main/resources/application.yml` 

---

## Пример конфигурации application.yml

```yaml
spring:
  application:
    name: credit
  datasource:
    url: jdbc:postgresql://localhost:5432/credit
    username: postgres
    password: 123456
    driverClassName: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  kafka:
    admin:
      fail-fast: false
      properties:
        offsets.topic.replication.factor: 1
        transaction.state.log.replication.factor: 1
        transaction.state.log.min.isr: 1
    consumer:
      bootstrap-servers: localhost:9092
      group-id: credit-group
      topics:
        new-credits: new-credits
        credits: credits
      dlq-topic: credits-dlq
    producer:
      bootstrap-servers: localhost:9094
      topics:
        credits: credits
        new-credits: new-credits
server:
  error:
    include-stacktrace: never
    include-message: always
    include-binding-errors: never
```

---

---

## Контакты

- Автор: Ivakov Andrey
