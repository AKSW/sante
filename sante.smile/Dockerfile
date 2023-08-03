#STAGE 1
FROM maven:3.8.5-openjdk-17-slim AS build

RUN mkdir -p /sante

WORKDIR /sante

COPY ../.. .

RUN mvn clean install

#STAGE 2
FROM gcr.io/distroless/java17-debian11

COPY --from=build /sante/sante.smile/target /sante/sante.smile

WORKDIR /sante/sante.smile

ENTRYPOINT ["java","-jar","-Dsante.index.path=/index","sante.smile-2.5.3.war"]

# Make port 7070 available to the world outside this container
EXPOSE 7070