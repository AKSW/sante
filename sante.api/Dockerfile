#STAGE 1
FROM maven:3.8.5-openjdk-17-slim

RUN mkdir -p /sante

WORKDIR /sante

COPY ../.. .

WORKDIR /sante/sante.api

ENTRYPOINT mvn spring-boot:run -Dspring-boot.run.arguments="--index.path=/index --server.port=7070"

# Make port 7070 available to the world outside this container
EXPOSE 7070