# Running the whole application

The whole application consisting off all six microservices can be run in two ways

## Running the public version

You can start the complete system with the Docker-Compose File **public.yaml**:
```shell script
docker-compose -f public.yaml up
```
This way all images are loaded from public repositories and started using docker containers

To stop the system run:
```shell script
docker-compose -f public.yaml down
```

## Running the private version

You can created the docker images yourself using the **README.md** files in each subproject. If you did not change the images names you can start the complete system with the Docker-Compose File **private.yaml**:
```shell script
docker-compose -f private.yaml up
```
This way only the images for external services are loaded from public repositories and started using docker containers

To stop the system run:
```shell script
docker-compose -f private.yaml down
```

## Testing the application

You can import the **payaramicro-quarkus.postman_collection.json** into Postman to get already configured calls for all the available endpoints

> **_NOTE:_**  Depending on the version you use and adjustments you made the ports of the services may differ


> **_NOTE:_**  Endpoints that are available and not stated in the Postman Collection:

    Jaeger-UI: http://localhost:16686/search

    Quarkus:
        Health: /q/health & /q/health-ui
        Metrics: /q/metrics
        OpenAPI: /q/openapi % /q/swagger-ui

    Payara Micro:
        Health: /health
        Metrics: /metrics
        OpenAPI /openapi