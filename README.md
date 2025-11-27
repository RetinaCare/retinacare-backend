# Retina Care :: Backend

This repository contains the source code for Retina Care's main API. Written in Java with the Spring framework, it follows best practices in developing a robust and functional REST API.

## Table of Contents

- [Features](#features)
- [System Architecture](#system-architecture)
- [Up and Running](#up-and-running)
- [Environment Variables](#environment-variables)
- [Usage](#usage)
  - [Authentication](#1-authentication--auth)
- [Scripts](#scripts)
- [Dependencies](#dependencies)

## Features

- Authentication: sign-up, sign-in, forget-password using secure stateless JWTs.
- Access token refresh and token rotation.
- Automated expired token deletion using cron jobs.
- Risk progression metrics via external microservice, called internally.

## System Architecture

| Relevant High Level Components  |
| - |
| ![arch.jpg](/docs/images/arch.jpg) |

At the core of the system, is our backend written in Java Spring. It exposes the endpoints for clients to authenticate and get diabetic retinopathy predictions. It also serves as the middleman between the web client and our internal ML models and prediction microservice.

#### Primary Database

  We use Postgres as our primary database of choice due to the simple reason that it is battle-tested, mature, and FOSS.
  
#### Server Strategy

  We do not use any managed PaaS (Platform-as-a-Service). Instead, our database and necessary services are hosted on a Virtual Private Server. This gives us a greater handle over   deployments and cost. After evaluating our options, we landed on a Debian 13 VPS running on a Digital Ocean droplet, Coolify for self-hosted deployments and Traefik as our routing proxy.
  
#### Object Storage

The machine learning model is stored on Digital Ocean spaces, which is an S3 compatible storage unit. This is where the [prediction service](https://github.com/RetinaCare/prediction-service) loads from before listening to incoming requests.

#### Continuous Integration & Deployments

We follow a CI/CD model, this ensures that the newest changes are automatically built and pushed without manual intervention. It also allows us to pin-point errors whenever a workflow run fails. The workflow is triggered on push events to the main branch, then the code is checked-out, built using Docker, and published to the Docker Container Registry. From there, we have a Coolify webhook that is triggered immedately - which starts a deployment event on our VPS.

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

## Scripts

A couple of handy Unix bash scripts, found in `/scripts`, are used for quickly executing common tasks. We provide utilities for cleaning (nuclear) and migrating the database schema, as well as others for building and running the required docker containers.

Details of their use can be easily understood by reading their content.

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
