## 简介
cloud-web-for-chart（helm&chart安装包，一键部署包）

### 环境条件
> 条件1: k8s下
> 
> 条件2: 
> 
> 

### 安装命令
仓库安装：
```text
#添加仓库
helm repo add gpg-dev https://dev-gpg.oss-cn-hangzhou.aliyuncs.com/helm-charts

#执行安装
helm install releaseName gpg-dev/cloud-server -n nameSpace
```

本地直接安装：
```text
helm install cloud-web ./cloud-web -n cloud-server --create-namespace --kube-context=hcs.gpg123.vip
```

```text
helm upgrade cloud-web ./cloud-web -n cloud-server --kube-context=hcs.gpg123.vip
```

```text
helm uninstall cloud-web -n cloud-server --kube-context=hcs.gpg123.vip
```