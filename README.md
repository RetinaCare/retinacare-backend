# Retina Care :: Backend

This repository contains the source code for Retina Care's main API. Written in Java with the Spring framework, it follows best practices in developing a robust and functional REST API.

## Table of Contents

- [Features](#features)
- [Up and Running](#up-and-running)
- [Environment Variables](#environment-variables)
- [Usage](#usage)
  - [Authentication](#1-authentication--auth)
- [Dependencies](#dependencies)

## Features

- Authentication: sign-up, sign-in, forget-password using secure stateless JWTs.

## Up and Running

1. Clone the repository.

    ```shell
   git clone https://github.com/RetinaCare/spring-backend.git backend
    ```

2. Once open in your editor, [IntelliJ IDEA](https://www.jetbrains.com/idea/) is recommended, install the required dependencies. Maven is our build tool of choice.
3. The project uses [Docker](https://docker.com) and [Docker Compose](https://docs.docker.com/compose/) to build the source code. We also run Redis and Postgres containers here for local testing.
4. Set the necessary environment variables, see more [here](#environment-variables).
5. The recommended way to run the server is via Docker compose, we've prepared a script, which can be found in `/scripts/build-compose` to build and spin up the containers.

    ```shell
   chmod +x ./scripts/build-compose.sh

   # The --compose flag is optional. 
   # A single jar is built if not specified. 
   ./scripts/build-compose.sh --compose
    ```

## Environment Variables

On Unix systems, you can set environment variables by exporting them. Required env variables are listed below:

1. `DB_USERNAME`:  PostgreSQL database username.
2. `DB_PASSWORD`: PostgreSQL database password.
3. `DB_URL`: PostgreSQL JDBC connection URL.
4. `POSTGRES_DB`: Database name (for container).
5. `POSTGRES_USER`: Database user (for container).
6. `POSTGRES_PASSWORD`: Database password (for container).
7. `JWT_SECRET`: Jwt HMAC SHA 256.

You can copy the `.env.example` file into `.env` and change as required. Then you can use this command to load them at once into context:

```shell
chmod +x ./scripts/load-env.sh
source ./scripts/load-env.sh
```

## Usage

Documentation of the available API endpoints is done with Swagger, and can be accessed on `/api/v1/swagger`. Note, that this is version 1 of the API, so all available endpoints must be prefixed with `/api/v1/`.

### 1. Authentication :: `/auth`

We provide endpoints for email/password based signing-in and sign-ups. As well as OAuth2 for signing in with Google.

Upon successful sign-ups, you will be provided with a refresh and access token. The access token is used to access protected endpoints, such as prediction. Due to its sensitive nature, it is short-lived and only valid for **30 minutes**. The refresh token is used to renew this access token. Its longevity is much higher than the access token. It is valid for only **7 days**, after which the user must sign in again to renew both tokens.


## Dependencies

- [Spring Boot](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot/)
- [Spring Validation](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation/)
- [Spring Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web/)
- [Spring Devtools](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools/)
- [Spring Security](https://mvnrepository.com/artifact/org.springframework.security/spring-security-core)
- [PostgreSQL JDBC Driver](https://mvnrepository.com/artifact/org.postgresql/postgresql/)
- [Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok/)
- [Flyway](https://mvnrepository.com/artifact/org.flywaydb/flyway-core/)
- [Spring Doc OpenAPI](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui/)
