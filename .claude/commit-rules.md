# CLAUDE.md

## Project Overview
这是一个基于 Spring AI Alibaba + Spring Cloud Alibaba 的微服务项目。
技术栈：Java 21 / Spring Boot 3.4.5 / Spring AI 1.1.0

---

## Commit Message Rules

### 格式规范（必须遵守）
使用 Conventional Commits 规范：

<type>(<scope>): <subject>

[optional body]

[optional footer]

### Type 类型
- feat：新功能
- fix：Bug 修复
- refactor：重构（不涉及新功能或 bug 修复）
- perf：性能优化
- docs：文档变更
- style：代码格式（不影响逻辑）
- test：测试相关
- chore：构建/依赖/工具变更
- revert：回滚

### Scope 范围（结合项目模块）
- ai-core：AI 核心模块
- rag：RAG 检索模块
- graph：Graph 工作流模块
- gateway：网关模块
- auth：认证模块
- common：公共模块

### Subject 规则
- 中文描述，简洁明了，不超过 50 字
- 动词开头：新增 / 修复 / 优化 / 重构 / 删除
- 末尾不加句号

### 示例
feat(rag): 新增文档分块策略支持滑动窗口模式
fix(graph): 修复多 Agent 并行执行时状态丢失问题
perf(ai-core): 优化 Embedding 批量请求减少 API 调用次数
chore(deps): 升级 Spring AI 版本至 1.1.0

---

## Code Review Checklist
生成 commit 前检查以下内容：
- [ ] 是否包含敏感信息（API Key / 密码）
- [ ] 是否有未删除的调试日志
- [ ] 测试是否通过

---

## Branch Naming Convention
- feature/xxx：新功能
- fix/xxx：bug 修复
- refactor/xxx：重构

---

## Commit Body Rules
当变更涉及以下情况时，必须生成 body 说明：
- 破坏性变更（Breaking Change）
- 涉及数据库 Schema 变更
- 影响对外 API 接口
- 性能相关变更需说明优化前后对比

---

## Language
- commit subject：中文
- commit body：中文
- code comment：中文
- 变量/方法命名：英文

---

## Forbidden in Commit Message
- 不要写 "update code" / "fix bug" 等无意义描述
- 不要暴露内部 IP、域名、账号信息
- 不要在 subject 中堆砌多个变更，拆分成多个 commit
- subject 不超过 72 个字符