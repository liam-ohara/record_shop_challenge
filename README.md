# Record Shop API Challenge
This demonstration API was written as part of the Northcoders Java Software Development bootcamp.

## Background
The purpose of this challenge was to demonstrate the development of a backend API and database for an imaginary record shop.
The high-level requirements of the API were to allow a user to:
* Store information about the records they have in stock
* Query this data in various ways
* Update it accordingly

### Criteria
The criteria set for the challenge were:

> * Create API endpoints with the appropriate HTTP verbs
> * Name API base URL and endpoints appropriately
> * Write production-quality code - good separation of concerns, clean and readable
> * Practise test-driven development - good coverage of unit tests
> * Include a descriptive README to document the key features of your solution, your assumptions, approaches and future thoughts.
> * Apply error and exception handling considerations in your API design
> * Include a `/health` endpoint to give the health of the application

### Minimal viable product (MVP)
The MVP for the challenge was to offer the following features: 

> * List all albums in stock
> * Get album by id
> * Add new albums into the database
> * Update album details
> * Delete albums from the database

## Programming approach
This application was written in Java 21 using:
* Object-oriented programming paradigm
* Model–view–controller (MVC) design pattern
* Test-driven development methodology 

## Installation and Setup
### Prerequisites

* Java 21
* Maven 3.6.3 or higher
* Spring Boot 3.3.4 or higher

*Optional:*
* PostgreSQL
* AWS-RDS
* PgAdmin
* Postman

### Installation
1. Clone the Repository

``git clone https://github.com/liam-ohara/record_shop_challenge.git``

2. Application.properties

Create the following .properties files under resources folder : src/main/resources/

**application.properties**

> spring.application.name=recordshop
> spring.profiles.active=h2
> springdoc.swagger-ui.path=/api/v1/swagger-ui.html
> management.endpoints.enabled-by-default=false
> management.endpoint.health.enabled=true
> management.endpoints.web.exposure.include=health

#### H2
Create the following .properties file if using H2 database (non-persistent) and set
``spring.profiles.active=h2`` in your application.properties file:

**application-h2.properties**

> spring.datasource.url=jdbc:h2:mem:recordshopdb
> spring.datasource.driverClassName=org.h2.Driver
> spring.datasource.username=sa
> spring.datasource.password=password
> spring.jpa.hibernate.ddl-auto=update
> 
> ##Enable H2 Console (optional, for debugging)
>
>spring.h2.console.enabled=true
>spring.h2.console.path=/h2-console

### PostgreSQL
Create the following .properties file if using PostgreSQL database and set
``spring.profiles.active=localDB`` in your application.properties file:

**application-localDB.properties**

>spring.datasource.url=jdbc:postgresql://localhost:5432/recordshop
>spring.datasource.username=postgres
>spring.datasource.password=postgres
>spring.jpa.database=postgresql
>spring.jpa.hibernate.ddl-auto=update

### AWS RDS
Create the following .properties file if using AWS-RDS and set ``spring.profiles.active=rds``
in your application.properties file

**application-rds.properties**
>spring.datasource.url=jdbc:postgresql://YOUR_AWS_RDS_URL/DATABASE_NAME
>spring.datasource.username=YOUR_DATABASE_NAME
>spring.datasource.password=YOUR_DATABASE_PASSWORD
>spring.jpa.database=postgresql
>spring.jpa.hibernate.ddl-auto=update

3. Once the previous two steps are completed you may run the application

### SWAGGER
You can use Postman/Swagger to test the endpoints.
To access Swagger open link in web after running the application.

http://localhost:8080/api/v1/swagger-ui/index.html

### H2-CONSOLE
You may use PgAdmin if using PostgreSQL or AWS-RDS.
To access H2 console open link in the web after running the application.

http://localhost:8080/h2-console/


## Usage and endpoints

**BASE_URL: /api/v1/**

* **Health endpoint: http://localhost:8080/actuator/health**


* **Album: /api/v1/album**


* GET: getAllAlbums <br />
  http://localhost:8080/api/v1/album

```
[
    {
        "albumId": 1,
        "name": "All Around My Hat",
        "artist": {
            "artistId": 1,
            "name": "Steeleye Span"
        },
        "publisher": {
            "publisherId": 1,
            "name": "Air Studios"
        },
        "releaseDate": "1977-10-01",
        "genre": "FOLK"
    }
]
```

* GET: getAlbumById <br />
  http://localhost:8080/api/v1/album/1

```
[
    {
        "albumId": 1,
        "name": "All Around My Hat",
        "artist": {
            "artistId": 1,
            "name": "Steeleye Span"
        },
        "publisher": {
            "publisherId": 1,
            "name": "Air Studios"
        },
        "releaseDate": "1977-10-01",
        "genre": "FOLK"
    }
]
```

* GET: getAlbumsByArtistName <br />
  http://localhost:8080/api/v1/album/artist/Steeleye%20Span

```
[
    {
        "albumId": 1,
        "name": "All Around My Hat",
        "artist": {
            "artistId": 1,
            "name": "Steeleye Span"
        },
        "publisher": {
            "publisherId": 1,
            "name": "Air Studios"
        },
        "releaseDate": "1977-10-01",
        "genre": "FOLK"
    }
]
```

* GET: getAlbumsByAlbumName <br />
  http://localhost:8080/api/v1/album/albumname/All%20Around%20My%20Hat

```
[
    {
        "albumId": 1,
        "name": "All Around My Hat",
        "artist": {
            "artistId": 1,
            "name": "Steeleye Span"
        },
        "publisher": {
            "publisherId": 1,
            "name": "Air Studios"
        },
        "releaseDate": "1977-10-01",
        "genre": "FOLK"
    }
]
```

* GET: getAlbumsByReleaseYear <br />
  http://localhost:8080/api/v1/album/releaseyear/1977

```
[
    {
        "albumId": 1,
        "name": "All Around My Hat",
        "artist": {
            "artistId": 1,
            "name": "Steeleye Span"
        },
        "publisher": {
            "publisherId": 1,
            "name": "Air Studios"
        },
        "releaseDate": "1977-10-01",
        "genre": "FOLK"
    }
]
```

* GET: getAlbumsByGenre <br />
  http://localhost:8080/api/v1/album/genre/folk

```
[
    {
        "albumId": 1,
        "name": "All Around My Hat",
        "artist": {
            "artistId": 1,
            "name": "Steeleye Span"
        },
        "publisher": {
            "publisherId": 1,
            "name": "Air Studios"
        },
        "releaseDate": "1977-10-01",
        "genre": "FOLK"
    }
]
```

* POST: insertAlbum <br />
  http://localhost:8080/api/v1/album

  JSON format for request body:

```
{
    "name":"All Around My Hat",
    "artist":{"name":"Steeleye Span"},
    "publisher":{"name":"Air Studios"},
    "releaseDate":[1977,10,1],
    "genre":"FOLK"
}
```

* DELETE: deleteAlbum <br />
  http://localhost:8080/api/v1/album/1
  
  Returns JSON of the album deleted

```
[
    {
        "albumId": 1,
        "name": "All Around My Hat",
        "artist": {
            "artistId": 1,
            "name": "Steeleye Span"
        },
        "publisher": {
            "publisherId": 1,
            "name": "Air Studios"
        },
        "releaseDate": "1977-10-01",
        "genre": "FOLK"
    }
]
```

* PUT: replaceAlbum <br />
  http://localhost:8080/api/v1/album/1

  Replaces the album with given id, with the album in the request body <br /> <br />
  JSON format for request body:
```
{
    "name":"Computerwelt",
    "artist":{"name":"Kraftwerk"},
    "publisher":{"name":"Kling Klang"},
    "releaseDate":[1981,2,11],
    "genre":"ELECTRONIC"
}
```  

* PATCH: updateAlbum <br />
  http://localhost:8080/api/v1/album/1

  Updates the album with given id, with details provided in the request body <br /> <br />
  JSON format for request body:

```
{   
    "artist": {"name":"KraftWerk"}
}    
```