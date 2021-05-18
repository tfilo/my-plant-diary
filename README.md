# Plant diary

This is personal project for Indoor/Outdoor gardening. App allows you to track gardening history and help to remember
when to water or fertile plants. Every user has list of own plants. Plant has list of Events. Every event is single
action done with plant. You can track watering, fertilizing, transplanting, propagation etc. When your plan dies it can
be deleted, but it will be kept in history section. This can simplify creating new plant of the same kind in future. For
example if you had tomatoes last season, you can find it in history and create new plant by copying it.

## Key features

- Tracking plant care (Watering, Fertilizing, etc.)
- Reminding planned events (Watering, Fertilizing, etc.)
- Tracking historical records
- Recreating new plants from historical records
- Group plants by location
- Group plants by type (Cactus, Succulent, Houseplant, Vegetable, etc.)

## Technical information

#### Technologies used

Backend
- SpringBoot
- JPA
- Postgres

Tests
- Integration Tests
- Random Beans
- Testcontainers

Frontend
- Angular (TODO - work in progress)

#### Requirements for build (other versions may work too)

- maven 3.8.1               https://maven.apache.org/download.cgi
- AdoptOpenJDK 16 J9        https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=openj9
- postgres:13.2-alpine      https://hub.docker.com/_/postgres
- maildev/maildev:1.1.0  https://hub.docker.com/r/djfarrelly/maildev

#### Build

- `mvn clean install`

#### Run - in development environment

- `./run-postgre-server.sh`
- `./run-plant-diary.sh`
- `./run-mail-server.sh`

## Endpoins

### Example

- **Static content** `curl http://localhost:8080`
- **Get
  Token:** `curl --header "Content-Type: application/json" --request POST --data '{"username":"USERNAME","password":"PASSWORD"}' http://localhost:8080/api/authenticate`
- **Example protected endpoint:** `curl GET -H "Authorization: Bearer TOKEN" http://localhost:8080/api/plant/1`

### All endpoints - API can change any time, it is under development

#### OpenAPI specification

- http://localhost:8080/v3/api-docs
- http://localhost:8080/v3/api-docs.yaml
- http://localhost:8080/swagger-ui.html

#### How to update angular generated services

To generate up to date OpenApi angular services, you must first run app and download api-docs.yaml file.

- `cd my-plant-diary`
- `docker-compose up -d`
- `wget -q -O ../api-docs.yaml http://localhost:8080/v3/api-docs.yaml`
- `docker-compose down`
- `cd ../`
- `mvn clean install`