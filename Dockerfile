FROM eclipse-temurin:21.0.7_6-jdk-alpine-3.21 AS builder
WORKDIR /build

COPY pom.xml .
COPY src ./src
COPY .mvn/ .mvn/
COPY mvnw .
COPY mvnw.cmd .
ARG PROFILE

RUN chmod +x mvnw && \
    ./mvnw -B -DskipTests clean package -Dspring-boot.run.profiles=${PROFILE}


FROM eclipse-temurin:21.0.7_6-jre-alpine-3.21

RUN apk --no-cache add \
    libcrypto3=3.3.4-r0 \
    libssl3=3.3.4-r0 \
    curl \
    tzdata \
    && cp /usr/share/zoneinfo/UTC /etc/localtime \
    && mkdir -p /app/logs \
    && chown -R 65534:65534 /app/logs

USER 65534:65534
WORKDIR /app

COPY --from=builder /build/target/*.jar adapter-mts.jar

EXPOSE 8080

ARG PROFILE
ENV SPRING_PROFILES_ACTIVE=$PROFILE
ENV JAVA_OPTS="-Xmx512m -Duser.timezone=UTC \
    --add-opens java.base/java.time=ALL-UNNAMED \
    --add-exports java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/adapter-mts.jar"]
