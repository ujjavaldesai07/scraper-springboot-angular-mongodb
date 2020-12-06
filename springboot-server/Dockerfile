# Docker multi-stage build

# 1. Building the App with Maven
FROM maven:3-jdk-11

ADD . /scraper_data_service_staging

WORKDIR /scraper_data_service_staging

# Just echo so we can see, if everything is there
RUN ls -l

# Run Maven build
RUN mvn clean install -DskipTests

# 2. Just using the build artifact and then removing the build-container
FROM openjdk:11-jdk

VOLUME /tmp

# Add Spring Boot scraper-data-service.jar to Container
COPY --from=0 /scraper_data_service_staging/target/scraper-data-service.jar scraper-data-service.jar

# Fire up our Spring Boot app by default
CMD ["sh", "-c", "java -Dserver.port=$PORT -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8 -XX:+UseContainerSupport -Djdk.tls.client.protocols=TLSv1.2 -Dspring.profiles.active=$ACTIVE_PROFILE -Djava.security.egd=file:/dev/./urandom -jar scraper-data-service.jar" ]
