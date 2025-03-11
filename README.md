# TedTalks task

## To install

- Java 21
- Docker

## How to run
There is docker-compose file which includes both the database and the application. Both should start working by running `docker-compose up`.

Then the application will be available at `http://localhost:8080`.

Please note that basic security is used in the service. The one needs to use username=`user` and password=`password`. The following header should be used:
```
Authorization: Basic dXNlcjpwYXNzd29yZA==
```

## How to test

1. Run the docker-compose
2. Import data using `http://localhost:8080/v1/tedtalks/upload` endpoint
```bash
curl --location 'http://localhost:8080/v1/tedtalks/upload' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--form 'file=@"/path_to/iO_Data.csv"'
```
3. Available endpoints:
```
POST http://localhost:8080/v1/tedtalks - Create additional TedTalks

GET http://localhost:8080/v1/tedtalks?title=?&author=? - Gets TedTalks based on title and author parameters. Parameters are optional.

GET http://localhost:8080/v1/tedtalks/top-influencers?topNumber=5 - Gets top N influencers based on formula in the service.
```
4. Another way to test the endpoints is to use provided (in the project root) Postman collection - `TedTalks service.postman_collection.json`

## Decisions
### Database
PostgreSQL was chosen as a database:
- Due to provided level of requirement, any DB can be used. For example MongoDB, PostgreSQL and other. For this task implementation I chose PostgreSQL due to familiarity and ease of setup. PostgreSQL is well-suited for a variety of applications and provides robust support for complex queries and transactions.

### Influencers formula
The following formula is used
```
viewsFactor * 0.5 + likesFactor * 0.3 + rate * 100
```
`viewsFactor` and `likesFactor` are based on log10 to make big numbers less dominant.

The formula is very discussable, but I tried to make it fair for all authors.

## Considerations
- Metrics and Monitoring
    - Integrating a monitoring solution like Prometheus would be beneficial for tracking system performance and health metrics.
    - Integrating a logging service (e.g., ELK stack or Loki) and structured logging (e.g., logback with JSON format) would allow for better monitoring.
- Security and Authorization
    - Implementing security is crucial, but the approach depends on the broader system architecture.
    - Endpoints are available for public discovery and modern API security approaches can be used. For example Spring Security with Basic Auth was implemented.
    - Database access should be changed from username+password to some modern way. It can be IAM authentication (if cloud environment), TLS/SSL, token-based and other.
- Errors handling improvements
    - Improve input data validation.
    - Better service error handling should be implemented across all endpoints to provide meaningful responses and improve fault tolerance.
- More tests should be written
    - Additional tests should be written to improve code coverage.
    - Currently, an H2 database is used for testing, which allows for efficient test execution.
    - For optimal coverage consider implementing other testing types. For example integration, regression, e2e, performance and other.
- API documentation
    - There are many ways to describe endpoints abd the documentation depends on if the service will be public or internal. 
- Optimizations to database and back-ups
  - For example add partitioning or unique constraints
- Optimizations to /v1/tedtalks/top-influencers endpoint
  - Precompute in memory when new tedTalk is saved and async flush to the table. But there is a cons to this approach. The data may be not correct for some time.
  - This ensures faster response times.