#!/bin/bash

# 查找 gpg123-admin.jar 进程的 PID
pid=$(ps -ef | grep 'gpg123-admin.jar' | grep -v 'grep' | awk '{print $2}')

# 检查是否找到 PID
if [ -z "$pid" ]; then
    echo "ruoyi-admin.jar is not running."
else
    # 终止进程
    kill $pid
    echo "ruoyi-admin.jar (PID: $pid) has been stopped."
fi
