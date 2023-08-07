#STAGE 1
FROM maven:3.8.5-openjdk-17-slim

RUN mkdir -p /sante

WORKDIR /sante

COPY . .

RUN mvn clean install

WORKDIR /sante/sante.main/target

CMD ["java", "-jar", "sante.main-0.0.21-SNAPSHOT-jar-with-dependencies.jar", "index", "-endpoint", "${endpoint}", "-path", "../../foaf_kg"]

VOLUME /sante/foaf_kg