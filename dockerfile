FROM amazoncorretto:17
WORKDIR /app
COPY build/libs/user-app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]