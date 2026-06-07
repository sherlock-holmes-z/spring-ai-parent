# spring-ai-demo/Dockerfile

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 从当前目录（spring-ai-demo）的 target 复制 jar
COPY target/*.jar spring-ai-demo.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/spring-ai-demo.jar"]
