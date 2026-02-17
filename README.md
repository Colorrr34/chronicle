# Chronicle: Portfolio API and Frontend (Ongoing)

A blogposts manager app that allows me to manage posts that I aggregate from another RSS feed aggregator project.[Gator](https://github.com/Colorrr34/Gator)

## Description

This is a project developed to explore the shift from the explicit and lightweight SQL compile tool(sqlc) in Golang and schema-first Drizzle ORM in TypeScript to a entity-first ORM Hibernate and the Java Spring boot framework.

## Prerequisites and Dependencies

Prerequisites: JDK 21, Maven 3.9.x

Dependencies (development and production): Hibernate, Project Lombok, PostgreSQL, Flyway, Frontend Maven Plugin

Dependencies (testing): Mockito, Datafaker, AssertJ

## Setup

I am using PostgreSQL database for this project. You can setup your own database by yourself or running the following command. It will run the sql command in psql and create a database named chronicle.

```
psql -f db.sql
```

Database Migration is handled by Spring-boot with Flyway.You need to set up a .env file at the root level so

If you have maven 3.9.2 or later versions. You can just run the programme

```
mvn spring-boot:run
```

If you do not have maven 3.9.2 or later you can use the maven wrapper by running

```
./mvnw spring-boot:run
```

## Folder structure

```
├── frontend                                      # frontend react app directory
│   ├── package.json
│   ├── src
│   └── vite.config.ts
├── pom.xml
├── src
│   ├── main
│   │   ├── java                                  # project directory
│   │   └── resources
│   │       ├── application.properties
│   │       ├── db
│   │       │   └── migration
│   │       ├── static                             # frontend react build directory
│   └── test                                       # testing directory
```
