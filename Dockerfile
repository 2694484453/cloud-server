FROM java:openjdk-8u111-jdk-alpine
LABEL authors="2694484453@qq.com"
COPY ruoyi-admin/target/ruoyi-admin.jar /app
#CMD java -jar /app/ruoyi-admin.jar
ENTRYPOINT ["/bin/sh", "-c", "java -jar /app/ruoyi-admin.jar"]
EXPOSE 8090
