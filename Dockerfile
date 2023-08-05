FROM openjdk:17.0.1-jdk-oracle

WORKDIR /app

COPY /build/libs/wes-0.0.1* /app/wes.jar

CMD [ "java" , "-jar", "/app/wes.jar"]