# export http_proxy=http://vps.gpg123.vip:15186
# unset http_proxy
#mkdir -p /etc/rancher/k3s
#cp -r /home/docker-compose/k3s/registries.yaml /etc/rancher/k3s

#docker
curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_MIRROR=cn sh -s - --docker --etcd-expose-metrics=true --write-kubeconfig ~/.kube/config --write-kubeconfig-mode 644

#containerd
curl -sfL https://rancher-mirror.rancher.cn/k3s/k3s-install.sh | INSTALL_K3S_MIRROR=cn sh -s - --container-runtime-endpoint tcp://0.0.0.0:10010 --etcd-expose-metrics=true --private-registry="/etc/rancher/k3s/registries.yaml" --write-kubeconfig ~/.kube/config --write-kubeconfig-mode 644 --service-node-port-range 80-65535 --node-ip 124.221.2.29 --node-external-ip 124.221.2.29 --node-name 124.221.2.29

#离线
INSTALL_K3S_SKIP_DOWNLOAD=true ./install.sh --etcd-expose-metrics=true --private-registry="/etc/rancher/k3s/registries.yaml" --write-kubeconfig ~/.kube/config --write-kubeconfig-mode 644 --service-node-port-range 80-65535 --node-ip 124.221.2.29 --node-external-ip 124.221.2.29

#代理
nohup k3s kubectl proxy --address='0.0.0.0' --accept-hosts='^*$' --port=8002 &

#
k3s kubectl -n kubernetes-dashboard port-forward svc/kubernetes-dashboard-kong-proxy 8443:443

systemctl start k3s

systemctl stop k3s

systemctl restart k3s

systemctl status k3s

#
k3s-uninstall.sh
