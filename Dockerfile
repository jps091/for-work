FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ./build/libs/*SNAPSHOT.jar project.jar
COPY ./src/main/resources/scouter.agent.jar scouter.agent.jar
COPY ./src/main/resources/scouter.conf scouter.conf
ENTRYPOINT ["java", "-javaagent:/app/scouter.agent.jar", "-Dscouter.config=/app/scouter.conf", "-Dobj_name=forwork-scouter", "-jar", "/app/project.jar"]