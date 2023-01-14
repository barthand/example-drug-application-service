# Example drug-application service

## Project Requirements

* JDK 17
* Maven (or better use included Maven wrapper via `./mvnw` wrapper script)
* Docker (required for tests using [Testcontainers](https://www.testcontainers.org/)) and docker-compose (required to setup local MongoDB)

## Getting started

Make sure `./mvnw` script has executable bit set:

```
chmod +x ./mvnw
```

## Build the project

In order to just build the package (without tests):

```
./mvnw clean package -DskipTests
```

(or drop `-DskipTests` if you want to run tests before package is built)

## Test the project

> **Note**
> Some tests use [Testcontainers](https://www.testcontainers.org/) and as such require valid Docker environment.

In order to run tests (this includes integration tests):

```
./mvnw clean test
```

## Provision database solution (MongoDB)

Setup local MongoDB using provided docker-compose:

```
docker-compose -f local-dev/docker-compose.yml -p drug-application-service up -d
```

If you want to make sure you're working on clean database, then first issue:

```
docker-compose -f local-dev/docker-compose.yml -p drug-application-service down 
```

## Run the project

> **Note**
> See previous section to provision database first.

```
./mvnw spring-boot:run
```

## General information

### High-level code architecture

Architecture is moderately following a Hexagon (or Onion, or Ports & Adapters) architecture, described in more details here: 

https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/
 
### Known issues

* within application/domain layer there are some dependencies to Spring's codebase (e.g. `Pageable` or `Page<T>`) that could be avoided to decouple domain completely from the framework (see comments in `DrugApplicationUseCases` class) 
* Some parts of the code use records, some other parts use `@Data` classes (provided via Lombok). This is just for experimentation purposes, and should be unified and consistently used in the target project. It's worth to remember though, not everything supports `java.lang.Record` yet (e.g. see issue in `com.barthand.example.drugapplication.openfda.client.OpenFDADrugsClientResponse`, namely lack of support of `@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)` for records - see [issue here](https://github.com/FasterXML/jackson-databind/issues/2992))
* Some parts of the code are tested in combination of unit tests and integration tests (with stub of external API in place), other parts rely on integration tests (APIs for storing/reading applications). Again, the approach should be discussed within the Team and consistently followed in real project.

### Future enhancements

* Responses might be enriched with HATEOAS (e.g. via `spring-boot-starter-hateoas`) to improve readability and discoverability of APIs
* Tracing capabilities could be added (e.g. via `spring-cloud-sleuth` dependency), so that each operation is assigned a Trace and Span ID which come very handy especially in distributed systems

## OpenFDA Drugs Integration

### Questions

* do we need to expose this data using a separate representation than the one provided by OpenFDA Drugs API?
* should we limit further results when using both filters? or search by either of them?

### Future enhancements

* there's not much done here in regard to resiliency of this external integration. Feign has default timeout of 10s (connect timeout) and 60s (read timeout). As an example, Circuit Breaker could be added here to make sure our system remains available in case of 3rd party system going down.

### Alternatives

* whole integration could be done in a way that we act as a reverse proxy with simplified API (just two filters), but returning the same response as OpenFDA provides (would allow to get rid of mapping between differently structured objects, and then ask 3rd party API with filters in place and subsequently just pass through JSON response as a string to the client).
* Download whole OpenFDADrugs dataset (<8 MB) periodically, store as a read model and serve data from it

### Risks

#### OpenFDA SLA, requests quota, paging limits, etc.

After https://open.fda.gov/apis/authentication/:
> With no API key: 240 requests per minute, per IP address. 1,000 requests per day, per IP address.
> 
> With an API key: 240 requests per minute, per key. 120,000 requests per day, per key.

## Applications stored within this system

### Questions 

* Should we somehow validate the data provided?
* Should we validate provided data against OpenFDA APIs?

### Future enhancements

* Instead of exposing aggregate state, we could (and at some point should) decouple aggregate from its representation on the API. There were no specific requirements that would require that at this point though.
* There is no `MongoTransactionManager` bean registered, which means there are no Mongo Transactions are (so far) used (even though docker-compose Mongo is configured for that). The reason is that Mongo even in old versions provides atomic writes to a single document out-of-the-box and in context of given requirements, we operate only on single documents during write operations, making transactions "redundant" .
* There is no optimistic locking applied to aggregate being stored, which means there is a possibility to overwrite changes done to aggregate in between `load()` and `save()` (although current code doesn't suffer from it).
