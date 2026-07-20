# project/CLAUDE.md

@import ./claude/rules/commit-rules.md
@import ./claude/rules/code-style.md
@import ./claude/rules/security-rules.md

## 项目概述

Spring AI 项目，集成 Spring Boot 3.4.5 + Spring AI 1.1.7 + Spring AI Alibaba 1.1.0.0，提供 AI 对话能力。

当前集成智谱 AI（GLM 系列模型），支持流式对话和聊天记忆。

---

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 21 | 运行时 |
| Spring Boot | 3.4.5 | Web 框架 |
| Spring AI | 1.1.7 | AI 能力抽象 |
| Spring AI Alibaba | 1.1.0.0 | 国产模型适配 |
| Maven | 3.9+ | 构建工具 |
| Docker | - | 容器化部署 |

---

## 模块结构

```
spring-ai-parent/
├── spring-ai-common-core/    # 公共核心模块（异常、统一返回）
├── spring-ai-demo/           # 示例应用（端口 8081）
├── .claude/                  # Claude Code 配置
│   ├── rules/                # 代码规范
│   ├── settings.json         # 权限配置
│   └── settings.md           # 配置说明
├── docker-compose.yml        # 容器编排
└── pom.xml                   # 根 POM（依赖管理）
```

### 模块说明

| 模块 | 说明 | 依赖 |
|------|------|------|
| `spring-ai-common-core` | 公共模块：统一异常处理、Result 返回封装 | Spring Boot Web |
| `spring-ai-demo` | 示例应用：ChatClient、ChatModel、ChatMemory | common-core + spring-ai-starter-model-zhipuai |

---

## 开发环境要求

- **JDK 21+**
- **Maven 3.9+**
- **IDE**：IntelliJ IDEA（推荐）
- **环境变量**：
  - `ZHIPU_KEY` - 智谱 AI API Key（必需）
  - `AI_MODEL` - 模型名称（可选，默认 `glm-4-flash`）

---

## 常用命令

### 构建与运行

```bash
# 编译全部模块
mvn clean compile

# 打包（跳过测试）
mvn clean package -DskipTests

# 本地运行 demo 模块
cd spring-ai-demo && mvn spring-boot:run

# 代码格式化检查
mvn spotless:check

# 自动格式化
mvn spotless:apply
```

### Docker 部署

```bash
# 构建并启动
docker-compose up -d

# 查看日志
docker-compose logs -f spring-ai-demo

# 停止
docker-compose down
```

---

## 关键约定

### 1. 统一返回封装

**所有 Controller 方法必须返回 `Result<T>` 或 `Result<PageResult<T>>`**，禁止返回原始对象或 void。

```java
// 正确
@GetMapping("/chat")
public Result<String> chat(@RequestParam String message) {
    return Result.success(chatService.chat(message));
}

// 错误
@GetMapping("/chat")
public String chat(@RequestParam String message) {
    return chatService.chat(message);
}
```

### 2. 异常处理

**Service 层只抛异常，不 try-catch 返回 null/false**。由 `GlobalExceptionHandler` 统一处理。

```java
// 正确
public User queryUser(Long id) {
    User user = userMapper.selectById(id);
    if (user == null) {
        throw new DataNotFoundException("用户不存在");
    }
    return user;
}
```

### 3. 代码格式化

项目使用 **Spotless + Google Java Format (AOSP 风格)**，提交前必须运行：

```bash
mvn spotless:apply
```

### 4. 敏感信息

**禁止硬编码** API Key、密码、Token，统一使用环境变量或 `${ENV_VAR:default}` 占位符。

```yaml
# 正确
api-key: ${ZHIPU_KEY:default_key}

# 错误
api-key: abc123actualkey
```

---

## 核心类位置

| 类 | 路径 | 用途 |
|----|------|------|
| `Result<T>` | `com.ai.common.result.Result` | 统一返回封装 |
| `PageResult<T>` | `com.ai.common.result.PageResult` | 分页返回封装 |
| `ResultCode` | `com.ai.common.result.ResultCode` | 返回码枚举 |
| `BusinessException` | `com.ai.common.exception.BusinessException` | 业务异常 |
| `DataNotFoundException` | `com.ai.common.exception.DataNotFoundException` | 数据未找到异常 |
| `GlobalExceptionHandler` | `com.ai.common.exception.GlobalExceptionHandler` | 全局异常处理 |

---

## 语言要求

- **思考过程**：中文
- **commit message**：中文
- **代码注释**：中文
- **变量/方法命名**：英文

---

## 开发流程

1. **创建分支**：`feature/xxx` 或 `fix/xxx`
2. **编码**：遵循代码规范，使用 `Result<T>` 返回
3. **格式化**：`mvn spotless:apply`
4. **测试**：确保功能正确
5. **提交**：遵循 Conventional Commits 规范
6. **推送**：`git push origin <branch>`

