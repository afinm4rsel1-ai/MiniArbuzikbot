# --- Этап 1: Сборка проекта с помощью Gradle ---
FROM gradle:8.5.0-jdk17-alpine AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN ./gradlew shadowJar --no-daemon

# --- Этап 2: Запуск приложения на минимальной Java ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/MiniArbuzikbot-1.0-SNAPSHOT-all.jar .
CMD ["java", "-jar", "MiniArbuzikbot-1.0-SNAPSHOT-all.jar"]
