# MallChat

基于抹茶项目的重构
- 升级为JDK21，根据jdk21新api重新编写处理逻辑
- 升级为 Spring Boot 3.4.3
- 升级mybatis-plus等依赖符合springboot3.4.3版本，并重新定义配置
- 更改为openapi3规范
- 重新构建webSocket处理逻辑
- 虚拟线程处理IO集中性业务
- 异常增强和类型推断语法
- 废除重复多余的crud业务，编写重要业务，符合学习逻辑
## 项目概述
- 原项目地址：https://github.com/zongzibinbin/MallChat

MallChat 是一个现代化的聊天平台，具有以下特点：

- 基于 WebSocket 的实时通信
- 用户认证和授权系统
- 用户徽章和个性化功能
- 管理员权限控制
- 微信登录集成
- 等等等
## 技术栈

- **后端**：
  - Java 21
  - Spring Boot 3.4.3
  - Netty WebSocket
  - MyBatis-Plus
  - Redis
  - MySQL
  - Redisson
  - JWT

- **工具**：
  - Hutool
  - Lombok
  - Swagger/OpenAPI

## 项目结构

项目采用模块化设计，主要包含以下模块：

- **mallchat-chat-server**：核心聊天服务
  - `user`：用户管理模块
  - `websocket`：WebSocket 通信模块
  - `common`：公共组件和工具

- **mallchat-framwork**：框架支持模块

## 功能特性

- **实时聊天**：基于 WebSocket 的即时通讯
- **用户管理**：注册、登录、个人信息管理
- **徽章系统**：用户可获取和佩戴不同徽章
- **权限控制**：基于角色的访问控制系统
- **微信集成**：支持微信扫码登录

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 配置说明

1. 修改数据库配置：
   ```
   # application-dev.properties 或 application-test.properties
   spring.datasource.url=jdbc:mysql://localhost:3306/mallchat?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

2. 配置 Redis：
   ```
   spring.data.redis.host=localhost
   spring.data.redis.port=6379
   spring.data.redis.password=your_password
   ```

3. 微信公众号配置（如需使用微信登录）：
   ```
   wx.mp.appId=your_appid
   wx.mp.secret=your_secret
   ```

### 构建与运行

```bash
# 克隆项目
git clone https://github.com/yourusername/mallchat-jdk21.git

# 进入项目目录
cd mallchat-jdk21

# 编译打包
mvn clean package -DskipTests

# 运行应用
java -jar mallchat-chat-server/target/mallchat-chat-server.jar
```

## API 文档

启动应用后，可通过以下地址访问 API 文档：

```
http://localhost:8080/swagger-ui/index.html
```

## 开发指南

### 添加新功能

1. 在相应模块中创建实体类、DTO、VO
2. 实现 Service 接口和实现类
3. 创建 Controller 提供 API 接口
4. 编写单元测试

### 代码规范

- 遵循阿里巴巴 Java 开发手册
- 使用 Lombok 简化代码
- 使用统一的返回格式 `ApiResult`

## 贡献指南

欢迎贡献代码或提出建议！请遵循以下步骤：

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

