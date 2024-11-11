#!/bin/bash

# 定义 split_git_url 函数
split_git_url() {
    local url=$1
    # 找到第一个 "//" 的位置
    split_index=$(expr index "$url" "//")
    # 如果找不到 "//"，返回原字符串
    if [ $split_index -eq 0 ]; then
        echo "$url"
        return
    fi
    # 分割字符串
    part1=${url:0:$((split_index + 1))}  # 包括 "//"
    part2=${url:$((split_index + 1))}

    # 重新连接字符串
    reconnected_url="$part1$GIT_USERNAME:$GIT_PASSWORD@$part2"
    echo "$reconnected_url"
}

# 定义 split_repo_name 函数
split_repo_name() {
        local url=$1
        # 获取最后一个斜杠的位置
        last_slash_index=${#url}
        while [[ $last_slash_index -gt 0 && ${url:$((last_slash_index-1)):1} != "/" ]]; do
            ((last_slash_index--))
        done

        # 获取最后一个点的位置
        last_dot_index=${#url}
        while [[ $last_dot_index -gt 0 && ${url:$((last_dot_index-1)):1} != "." ]]; do
            ((last_dot_index--))
        done

        # 如果找不到 "/" 或 "."，返回空字符串
        if [[ $last_slash_index -eq 0 || $last_dot_index -eq 0 ]]; then
            echo ""
            return
        fi

        # 截取最后一个 "/" 和最后一个 "." 之间的内容
        repoName=${url:$((last_slash_index)):$((last_dot_index - last_slash_index - 1))}
        echo "$repoName"
}

# 设置git相关环境变量
GIT_URL=$GIT_URL                  #git仓库地址
echo "##GIT_URL:${GIT_URL}"
GIT_USERNAME=$GIT_USERNAME        #仓库用户名
echo "##GIT_USERNAME:${GIT_USERNAME}"
GIT_PASSWORD=$GIT_PASSWORD        #仓库密码
echo "##GIT_PASSWORD:${GIT_PASSWORD}"
GIT_FINAL_URL=$(split_git_url "$GIT_URL")
echo "##GIT_FINAL_URL:${GIT_FINAL_URL}"
GIT_REPO_NAME=$(split_repo_name "$GIT_URL")
echo "##GIT_REPO_NAME:${GIT_REPO_NAME}"

#########################执行git仓库下载##############################
# 克隆代码仓库
echo "正在检查git目录..."
TARGET_DIR="/home/${GIT_REPO_NAME}"
if [ -d "$TARGET_DIR" ]; then
    echo "目标目录已存在，正在进入目录并执行 git pull..."
    cd "$TARGET_DIR" && git pull
else
    echo "目标目录不存在，正在克隆仓库..."
    git clone "$GIT_FINAL_URL" "$TARGET_DIR"
fi
cd $TARGET_DIR
# 使用 Maven 打包项目
echo "开始进行打包..."
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit 1
fi

echo "Pipeline completed successfully!"
