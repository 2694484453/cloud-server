FROM registry.cn-hangzhou.aliyuncs.com/gpg_dev/java:openjdk-8u111-jdk-alpine
LABEL authors="2694484453@qq.com"
ENV TZ=Asia/Shanghai
COPY  ./cloud-server-admin/target/cloud-server-admin.jar /cloud-server-admin.jar
COPY  ./k8s/nerdctl /usr/local/bin/nerdctl
COPY  ./k8s/linux-amd64/helm /usr/local/bin/helm
RUN chmod +x /usr/local/bin/nerdctl
RUN chmod +x /usr/local/bin/helm
ENTRYPOINT ["/bin/sh", "-c", "cd / && java -jar cloud-server-admin.jar"]
EXPOSE 9099 9099