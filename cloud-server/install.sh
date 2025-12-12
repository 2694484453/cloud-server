helm install cloud-server ../cloud-server -n cloud-server

helm upgrade cloud-server ./cloud-server -n cloud-server --kube-context=hcs.gpg123.vip