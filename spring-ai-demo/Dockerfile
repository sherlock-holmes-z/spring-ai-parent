# spring-ai-demo/Dockerfile

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 从当前目录（spring-ai-demo）的 target 复制 jar
COPY target/*.jar spring-ai-demo.jar

EXPOSE 8081

# JVM参数优化（容器友好）
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70.0 -Djava.security.egd=file:/dev/./urandom"

# 挂载日志目录
VOLUME /app/logs

# 启动应用（日志配置由 logback-spring.xml 管理）
ENTRYPOINT ["java", "-jar", "/app/spring-ai-demo.jar"]
