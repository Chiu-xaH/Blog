# 使用OpenJDK 17的精简版镜像
FROM openjdk:17-oracle

# 设置工作目录
WORKDIR /app

# 将构建好的jar包复制到容器中的/app目录
COPY build/libs/blog-1.0.jar blog.jar

# 容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "blog.jar"]

# 暴露容器的8080端口
EXPOSE 8080

# 添加健康检查，确保应用正常运行
HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD curl --fail http://localhost:8080/actuator/health || exit 1
