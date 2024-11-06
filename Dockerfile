FROM registry.cn-hangzhou.aliyuncs.com/gpg_dev/java:openjdk-8u111-jdk-alpine
LABEL authors="2694484453@qq.com"
COPY  ./ruoyi-admin/target/ruoyi-admin.jar /ruoyi-admin.jar
#CMD java -jar /app/ruoyi-admin.jar
ENTRYPOINT ["/bin/sh", "-c", "cd / && java -jar ruoyi-admin.jar"]
EXPOSE 8090 9099
