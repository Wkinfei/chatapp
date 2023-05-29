FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /home/app 
 
COPY ./pom.xml /home/app/pom.xml 
COPY ./src/main/java/nus/iss/chatapp/com/server/ServerApplication.java \ 
/home/app/src/main/java/nus/iss/chatapp/com/server/ServerApplication.java 
 
RUN mvn -f /home/app/pom.xml clean package 
 
COPY . /home/app 
RUN mvn -f /home/app/pom.xml clean package 
 
FROM openjdk:17-oracle 
EXPOSE 8081
COPY --from=build /home/app/target/*.jar app.jar 
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]