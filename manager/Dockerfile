# Используем OpenJDK 17 в качестве базового образа
FROM openjdk:17-jdk-slim AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем скомпилированный JAR-файл из сборки Gradle
COPY build/libs/manager-1.0-SNAPSHOT.jar app.jar

# Делаем JAR исполняемым (может быть не обязательно, но полезно)
RUN chmod +x app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
