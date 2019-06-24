# Coding assignment for back-end engineer

* Prerequisites
  * Basic java knowledge, along with experience in OOP, API design, testing frameworks, and understanding of basic algorithms and data structures is a necessary prerequisite for taking this test.
* Assignment
  * We ask you to implement an application which represents a store for car charging session entities. It will hold all records in memory and provide REST API.
  * Each entity of the store represents unique charging session that can be in progress or finished. Entity can have the following fields:

  * UUID id;
  * String stationId;
  * LocalDateTime startedAt;
  * LocalDateTime stoppedAt;
  * LocalDateTime updatedAt;
  * StatusEnum status;

## Endpoints to implement

* POST /chargingSessions 

Submit a new charging session for the station

  * Computational complexity (upper bound)
O(log(n)) 

  * Request body
{
"stationId":
"ABC-12345"
}

{

  * Response body
"id":
"d9bb7458-d5d9-4de7-87f7-7f39edd51d18",
"stationId": "ABC-12345",
"updatedAt": "2019-05-06T19:00:20.529"
}


* PUT /chargingSessions/{id} Stop charging session 

  * Computational complexity (upper bound)
O(log(n)) 

  * Request body
{
"id":
"d9bb7458-d5d9-4de7-87f7-7f39edd51d18",
"stationId": "ABC-12345",
"updatedAt":
"2019-05-06T21:15:01.198",
"status": "FINISHED"
}
* GET /chargingSessions Retrieve all charging sessions 

  * Computational complexity (upper bound)
O(n) 

  * Response body
[{
"id":
"d9bb7458-d5d9-4de7-87f7-7f39edd51d18",
"stationId": "ABC-12345",
"updatedAt":
"2019-05-06T19:00:20.529",
"status": "IN_PROGRESS"
},
{
"id":
"69acaf1b-743f-47df-9339-abe50998b201",
"stationId": "ABC-12346",
"updatedAt": "2019-05-06T19:01:35.245"
,
"status": "FINISHED"
}]

* GET /chargingSessions/summary

Retrieve a summary of submitted charging sessions including:
totalCount – total number of charging session updates for the last minute
startedCount – total number of started charging sessions for the last minute
stoppedCount – total number of stopped charging sessions for the last minute


  * Computational complexity (upper bound)
O(log(n)) 

  * Response body
{
"totalCount: 5,
"startedCount": 1
"stoppedCount": 4
}

* Requirements
  * Implementation should be done in Java 8
  * Application is thread-safe
  * Application is covered with tests (classes and endpoints)
  * Application is using in-memory data structures (not to be confused with in-memory databases)
  * Computational complexity meets our requirements (see the table). Limits are only applicable to the data structure which holds charging session objects (serialization, object mappings and other parts of application logic are out of consideration and may have arbitrary complexity)
  * Documentation of the implemented functionality and instructions how to run are present (consider adding javadocs and README file).
  * We expect it to be run with a single command
  * Out of scope
  * Space complexity
  * Data store limit considerations (we make an assumption that total amount of charging sessions will never exceed 2 power 30 )

## Tech Stack

  * Java 8
  * Spring Boot 2.1.5
  * JUnit 5
  * Lombok


## Running the application

To build application: 

```bash
gradlew clean build
```

To run application: 

```bash
gradlew clean bootRun
```

To start application: 

```bash
http://localhost:9001/
```

## Testing


```bash
gradlew clean test
```

### Documentation

Swagger2 documentation is available.

http://localhost:9001/swagger-ui.html#