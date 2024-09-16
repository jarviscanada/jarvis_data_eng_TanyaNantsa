FROM openjdk:8-alpine
COPY target/stockquote*.jar /usr/local/app/stockquote/lib/stockquote.jar
COPY src/main/resources /usr/local/app/stockquote/resources
ENTRYPOINT ["java","-jar","/usr/local/app/stockquote/lib/stockquote.jar"]