FROM openjdk:8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} post-service.jar
EXPOSE 3010
ENTRYPOINT ["java","-jar","/post-service.jar"]