# Retina Care - Backend

This repository contains the source code for Retina Care's main API. Written in Java with the Spring framework, it follows best practices in developing a robust and functional REST API.

# Features

- TODO

## Setting Up Locally

1. Git clone the repository into your local computer.

    ```shell
       git clone https://github.com/RetinaCare/spring-backend.git backend
    ```

2. Once opened up in your editor of choice, [IntelliJ IDEA](https://www.jetbrains.com/idea/) is recommended, install the required dependencies. Maven is our build chain of choice.
3. The project uses [Docker](https://docker.com) and [Docker compose](https://docs.docker.com/compose/) to build the source code. We run the Redis and Postgres containers here as well for local testing.
4. Set the necessary environment variables, information on what variables are required is listed [here](#environment-variables).
5. The recommended way to run the server is via Docker compose, we've prepared a script, which can be found in `/scripts/build-compose` to build and spin up the containers.

    ```shell
       chmod +x ./scripts/build-compose.sh
       # the --compose flag is optional, 
       # without it, are single Jar will be built 
       ./scripts/build-compose.sh --compose
    ```

## Environment Variables

| Key           | Description                    | Default Value |
|---------------|--------------------------------|---------------|
| `DB_USERNAME` | PostgreSQL Database Username   | postgres      |
| `DB_PASSWORD` | PostgreSQL Database Password   |               |
| `DB_URL`      | PostgreSQL JDBC Connection URL |               |

## Dependencies

- TODO