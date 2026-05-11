# Spring AI Parent

Spring AI 项目，集成 Spring Boot 3.4.5 + Spring AI 1.1.0 + Spring Cloud Alibaba。

## 模块

| 模块 | 说明 |
|------|------|
| `spring-ai-demo` | 示例应用，端口 8081 |

## Claude Code 配置

项目包含 Claude Code 配置，帮助团队提升协作效率。

### 项目级配置（共享）

`.claude/settings.json` — 提交到 git，全组生效：

- **PostToolUse Hook**: 每次 `Write`/`Edit` 操作后自动执行 `git add`，将新/修改文件纳入 git 管理（git 会自动遵守 `.gitignore`）

### 个人级配置（不共享）

如需覆盖项目配置或添加个人偏好，在项目根目录创建 `.claude/settings.local.json`，其格式与 `settings.json` 一致，优先级更高。该文件已在 `.gitignore` 中排除。
