#USING MAVEN LICENSED UNDER APACHE 2.0
#put any external production libraries into mount /app/BOOT-INF/lib/external
#you can also change logging configuration via the logback-config-file env var
FROM adoptopenjdk/openjdk11 as builder
ENV LOGBACK_CONFIG_FILE=logback.xml
WORKDIR /pdf_formatter
RUN apt-get update -y && apt-get upgrade -y
RUN apt-get install -y maven
COPY / ./
RUN mvn clean package
FROM adoptopenjdk/openjdk11 as runner
ENV LOGBACK_CONFIG_FILE=logback.xml
WORKDIR /app
COPY --from=builder pdf_formatter/target/pdf_formatter-1.4.1.jar ./
RUN jar -xf pdf_formatter-1.4.1.jar
ENTRYPOINT java org.springframework.boot.loader.PropertiesLauncher -Dlogging.config=$env:LOGBACK_CONFIG_FILE

