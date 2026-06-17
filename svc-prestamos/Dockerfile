FROM maven:3.9-eclipse-temurin-21
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
EXPOSE 8092
CMD ["java", "-jar", "target/svc-prestamos-0.0.1-SNAPSHOT.jar"]