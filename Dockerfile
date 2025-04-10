FROM registry.cn-hangzhou.aliyuncs.com/gpg_dev/java:openjdk-8u111-jdk-alpine
LABEL authors="2694484453@qq.com"
COPY  ./cloud-server-admin/target/cloud-server-admin.jar /cloud-server-admin.jar
COPY  ./k8s/config /root/.kube/config
ENTRYPOINT ["/bin/sh", "-c", "cd / && java -jar cloud-server-admin.jar"]
EXPOSE 9099 9099
