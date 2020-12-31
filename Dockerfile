ARG MC_VER=1.16.4

# Build Craftbukkit jar
FROM openjdk:14-alpine AS craftbukkit

ARG MC_VER
WORKDIR /BuildTools
RUN apk add --no-cache git curl
RUN curl -o BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
RUN java -jar BuildTools.jar --rev ${MC_VER} --compile craftbukkit

# Build craftana plugin
FROM maven:3-jdk-14 AS craftana

WORKDIR /craftana
COPY craftana .
RUN mvn clean install
RUN cp target/craftana-*-jar-with-dependencies.jar /craftana.jar

# Setup server
FROM openjdk:14-alpine

ARG MC_VER
WORKDIR /mcserver
COPY --from=craftbukkit /BuildTools/craftbukkit-$MC_VER.jar craftbukkit.jar
COPY --from=craftana /craftana.jar plugins/
COPY craftbukkit.sh .
COPY eula.txt .
COPY world world

# Start server
EXPOSE 25565
EXPOSE 25566
CMD ./craftbukkit.sh 
