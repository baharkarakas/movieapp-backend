# =========================
# 1. BUILD STAGE
# =========================
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Bağımlılıkları cachelemek için önce pom.xml
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Kaynak kodu kopyala ve jar üret
COPY src ./src
RUN mvn -B -DskipTests clean package


# =========================
# 2. RUN STAGE
# =========================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Render PORT env verir
ENV PORT=8080

# Build aşamasından jar'ı al
COPY --from=build /app/target/*.jar app.jar

# Spring Boot default port
EXPOSE 8080

# Uygulamayı başlat
CMD ["java", "-jar", "app.jar"]
