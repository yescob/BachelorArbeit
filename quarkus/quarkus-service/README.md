# quarkus-service Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8082/q/dev/.

## Creating a docker image

The application can be packaged using:
```shell script
./gradlew build
```

Then, build the image with:
```shell script
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/quarkus-service-jvm .
```

## Running the application

Then run the container using:
```shell script
docker run -i --rm -p 8082:8082 quarkus/quarkus-service-jvm
```

> **_NOTE:_**  Other services have to be running and properly configured in the `application.properties` in order for the application to work.
> **_NOTE:_**  When running multiple services the port numbers may have to be adjusted.
