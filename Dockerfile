#USING MAVEN LICENSED UNDER APACHE 2.0
FROM noisycatz/winmaven:adoptopenjdk15_hotspot_mvn3.6.3 as builder
ENV LOGBACK_CONFIG_FILE=logback.xml
WORKDIR /pdf_formatter
COPY / ./
RUN mvn clean package
FROM adoptopenjdk:hotspot-windowsservercore-ltsc2016 as runner
ENV LOGBACK_CONFIG_FILE=logback.xml
RUN echo SET CONFIG: $env:LOGBACK_CONFIG_FILE
WORKDIR /app
COPY --from=builder pdf_formatter/target/pdf_formatter-0.0.1.jar ./
RUN jar -xf pdf_formatter-0.0.1.jar
ENTRYPOINT java org.springframework.boot.loader.PropertiesLauncher -Dlogging.config=$env:LOGBACK_CONFIG_FILE
#remove hardcoded classpath
