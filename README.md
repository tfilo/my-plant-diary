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
- Angular (TODO)

#### Requirements for build (other versions may work too)

- maven 3.6.3               https://maven.apache.org/download.cgi
- AdoptOpenJDK 11 J9        https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=openj9
- postgres:13.1-alpine      https://hub.docker.com/_/postgres
- djfarrelly/maildev:1.1.0  https://hub.docker.com/r/djfarrelly/maildev

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

#### Authentication

- `POST: /api/authenticate` - return generated Token

#### Plant - linked to autenticated USER

- `POST: /api/plant` - create Plant
- `PUT: /api/plant` - update Plant
- `GET: /api/plant/{id}` - get Plant by id
- `GET: /api/plant` - get all Plants
- `GET: /api/plant/by-location` - get all Plants by location, if locationId param not specified than will return plants without specified location
- `DELETE: /api/plant/{id}` - delete Plant by id
- `GET: /api/plant/type` - get all PlantTypes

#### Event - linked to specific PLANT of autenticated USER

- `POST: /api/event` - create Event
- `PUT: /api/event` - update Event
- `GET: /api/event/{id}` - get Event by id
- `GET: /api/event/all/{plantId}` - get all Events by plantId
- `DELETE: /api/event/{id}` - delete Event by id
- `GET: /api/event/type` - get all EventTypes

#### Location - linked to autenticated USER

- `POST: /api/location` - create Location
- `PUT: /api/location` - update Location
- `GET: /api/location/{id}` - get Location by id
- `GET: /api/location` - get all Locations
- `DELETE: /api/location/{id}` - delete Location by id

#### Schedule - linked to specific PLANT of autenticated USER

- `POST: /api/schedule` - create Schedule
- `PUT: /api/schedule` - update Schedule
- `GET: /api/schedule/{id}` - get Schedule by id
- `GET: /api/schedule` - get all Schedules
- `DELETE: /api/schedule/{id}` - delete Schedule by id

#### Photo - upload photos to Plant

- `POST: /api/photo` - upload Photo
- `PUT: /api/photo` - update Photo description
- `GET: /api/photo/{id}` - get Photo by id
- `GET: /api/photo/all/{plantId}` - get all Photo Thumbnails
- `DELETE: /api/photo/{id}` - delete Photo by id

#### Manage users

- `POST: /api/user/register` - register new user
- `PUT: /api/user/activate` - activate new user using activationToken from email
- `PUT: /api/user` - update user
- `GET: /api/user` - get logged user
- `DELETE: /api/user` - delete logged user by username and password (just for verification that user is sure what account is deleting)
