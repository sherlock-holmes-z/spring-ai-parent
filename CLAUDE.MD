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

<!-- superpowers-zh:begin (do not edit between these markers) -->
# Superpowers-ZH 中文增强版

本项目已安装 superpowers-zh 技能框架（20 个 skills）。

## 核心规则

1. **收到任务时，先检查是否有匹配的 skill** — 哪怕只有 1% 的可能性也要检查
2. **设计先于编码** — 收到功能需求时，先用 brainstorming skill 做需求分析
3. **测试先于实现** — 写代码前先写测试（TDD）
4. **验证先于完成** — 声称完成前必须运行验证命令

## 可用 Skills

Skills 位于 `.claude/skills/` 目录，每个 skill 有独立的 `SKILL.md` 文件。

- **brainstorming**: 在任何创造性工作之前必须使用此技能——创建功能、构建组件、添加功能或修改行为。在实现之前先探索用户意图、需求和设计。
- **chinese-code-review**: 中文 review 沟通参考——话术模板、分级标注（必须修复/建议修改/仅供参考）、国内团队常见反模式应对。仅在用户显式 /chinese-code-review 时调用，不要根据上下文自动触发。
- **chinese-commit-conventions**: 中文 commit 与 changelog 配置参考——Conventional Commits 中文适配、commitlint/husky/commitizen 中文模板、conventional-changelog 中文配置。仅在用户显式 /chinese-commit-conventions 时调用，不要根据上下文自动触发。
- **chinese-documentation**: 中文文档排版参考——中英文空格、全半角标点、术语保留、链接格式、中文文案排版指北约定。仅在用户显式 /chinese-documentation 时调用，不要根据上下文自动触发。
- **chinese-git-workflow**: 国内 Git 平台配置参考——Gitee、Coding.net、极狐 GitLab、CNB 的 SSH/HTTPS/凭据/CI 接入差异与镜像同步配置。仅在用户显式 /chinese-git-workflow 时调用，不要根据上下文自动触发。
- **dispatching-parallel-agents**: 当面对 2 个以上可以独立进行、无共享状态或顺序依赖的任务时使用
- **executing-plans**: 当你有一份书面实现计划需要在单独的会话中执行，并设有审查检查点时使用
- **finishing-a-development-branch**: 当实现完成、所有测试通过、需要决定如何集成工作时使用——通过提供合并、PR 或清理等结构化选项来引导开发工作的收尾
- **mcp-builder**: MCP 服务器构建方法论 — 系统化构建生产级 MCP 工具，让 AI 助手连接外部能力
- **receiving-code-review**: 收到代码审查反馈后、实施建议之前使用，尤其当反馈不明确或技术上有疑问时——需要技术严谨性和验证，而非敷衍附和或盲目执行
- **requesting-code-review**: 完成任务、实现重要功能或合并前使用，用于验证工作成果是否符合要求
- **subagent-driven-development**: 当在当前会话中执行包含独立任务的实现计划时使用
- **systematic-debugging**: 遇到任何 bug、测试失败或异常行为时使用，在提出修复方案之前执行
- **test-driven-development**: 在实现任何功能或修复 bug 时使用，在编写实现代码之前
- **using-git-worktrees**: 当需要开始与当前工作区隔离的功能开发，或在执行实现计划之前使用——通过原生工具或 git worktree 回退机制确保隔离工作区存在
- **using-superpowers**: 在开始任何对话时使用——确立如何查找和使用技能，要求在任何响应（包括澄清性问题）之前调用 Skill 工具
- **verification-before-completion**: 在宣称工作完成、已修复或测试通过之前使用，在提交或创建 PR 之前——必须运行验证命令并确认输出后才能声称成功；始终用证据支撑断言
- **workflow-runner**: 在 Claude Code / OpenClaw / Cursor 中直接运行 agency-orchestrator YAML 工作流——无需 API key，使用当前会话的 LLM 作为执行引擎。当用户提供 .yaml 工作流文件或要求多角色协作完成任务时触发。
- **writing-plans**: 当你有规格说明或需求用于多步骤任务时使用，在动手写代码之前
- **writing-skills**: 当创建新技能、编辑现有技能或在部署前验证技能是否有效时使用

## 如何使用

当任务匹配某个 skill 时，使用 `Skill` 工具加载对应 skill 并严格遵循其流程。绝不要用 Read 工具读取 SKILL.md 文件。

如果你认为哪怕只有 1% 的可能性某个 skill 适用于你正在做的事情，你必须调用该 skill 检查。
<!-- superpowers-zh:end -->
