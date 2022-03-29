FROM openjdk:11
EXPOSE 3010
ADD target/demo-0.0.1-SNAPSHOT.jar post-service.jar
ENTRYPOINT ["java","-jar","post-service.jar"]