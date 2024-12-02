helm install kubernetes-dashboard -n kubernetes-dashboard ./kubernetes-dashboard-7.5.0.tgz --kubeconfig ./k3s.yaml --create-namespace


helm install kube-prometheus-stack -n monitoring ./kube-prometheus-stack-66.3.0.tgz --kubeconfig ./k3s.yaml --create-namespace
