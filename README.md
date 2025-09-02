# Restaurant Voting REST API

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-green)

RESTful API for deciding where to have lunch. The project is implemented on Spring Boot using JPA, Spring Security and
Swagger.

## 🔧 Technical requirement

A voting system for deciding where to have lunch.

- 2 types of users: admin and regular users
- Admin can input a restaurant, and it's lunch menu of the day (2-5 items usually, just a dish name and price)
- Menu changes each day (admins do the updates)
- Users can vote for a restaurant they want to have lunch at today
- Only one vote counted per user
- If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind
    - If it is after 11:00 then it is too late, vote can't be changed
- Each restaurant provides a new menu each day

## ⚙️ Technologies

- **Language**: Java 21
- **Framework**: Spring Boot 3.4.5
- **Data base**: H2
- **Libraries**:
- Spring Data JPA
- Spring Security
- Lombok
- Swagger 3 (OpenAPI)
- JUnit 5
- Mockito
- Caffeine Cache
- MapStruct
- **Instruments**:
- Maven
- Git

## 📋 Environment requirements

- Java 21+
- Maven 3.9+
- Git 2.25+

## 📚 API Documentation

[Interactive Swagger UI](http://localhost:8080/)

## 🛠 Installation and launch

```bash
git clone https://github.com/c0brr/restaurant-voting.git
cd restaurant-voting
mvn package -DskipTests
java -jar target/restaurant-voting-1.0.0.jar