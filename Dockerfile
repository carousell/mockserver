FROM openjdk:8

COPY . /app
WORKDIR /app
RUN ./gradlew jar

EXPOSE 8089
HEALTHCHECK --interval=5s --timeout=3s --retries=5 CMD curl --fail http://localhost:8089/__admin/ || exit 1
CMD ["java", "-jar", "./build/libs/mockserver-1.0-SNAPSHOT.jar"]