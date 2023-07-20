# MicroProfile generated Application

## Introduction

MicroProfile Starter has generated this MicroProfile application for you.

The generation of the executable jar file can be performed by issuing the following command
```shell script
./gradlew microBundle
```


This will create an executable jar file **micro-movie-microbundle.jar** within the _build/libs_ gradle folder. This can be started by executing the following command
```shell script
./gradlew microStart
```


## Creating a docker image

The application can be packaged using:
```shell script
./gradlew microBundle
```

Then, build the image with:
```shell script
docker build -f ./docker/Dockerfile.micro -t micro/micro-movie .
```

## Running the application

Then run the container using:
```shell script
docker run -i --rm -p 8083:8083 micro/micro-movie .
```

> **_NOTE:_**  Other services like Kafka or the database have to be running and properly configured in order for the application to work.
> **_NOTE:_**  When running multiple services the port numbers may have to be adjusted.



