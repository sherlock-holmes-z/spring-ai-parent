# Spring AI 生产化学习路线

> 基于当前项目：Spring AI 1.1.0 + 智谱 AI + Spring Boot 3.4.5 + JDK 21

## 当前进度

- [x] ChatModel / ChatClient 基础调用
- [x] 流式响应（Flux / SSE）
- [x] 结构化输出（entity extraction）
- [x] 自定义 Advisor（LoggingAdvisor）

---

## 第一阶段：提示词工程与输出控制

> 目标：让模型输出稳定可控，这是生产的基础

| 主题 | 核心 API | 生产价值 |
|---|---|---|
| Prompt Template | `PromptTemplate` + 占位符 `{{variable}}` | 提示词与代码解耦，方便运营调整 |
| 输出格式约束 | `BeanOutputConverter` / `MapOutputConverter` | 强制 JSON 输出，减少解析失败 |
| 多轮对话记忆 | `ChatMemory` + `MessageChatMemoryAdvisor` | 有上下文的对话，不是一次性问答 |
| System Prompt 管理 | 外部文件 / 配置中心管理 prompt | 运营可热更新 prompt，无需发版 |

**产出物**：一个多轮对话接口，支持上下文记忆 + 格式化输出

---

## 第二阶段：函数调用（Function Calling）

> 目标：让 AI 能调用你的业务系统，不只是聊天

| 主题 | 核心 API | 生产价值 |
|---|---|---|
| `@Tool` 注解 | 声明式暴露 Java 方法 | 模型自动决定何时调哪个函数 |
| `FunctionCallback` | 编程式注册工具 | 更灵活的控制和参数校验 |
| 多工具编排 | 一次对话调用多个工具 | 复杂业务场景自动化 |
| 工具权限控制 | Advisor 层拦截审核 | 防止模型调用越权操作 |

**产出物**：实现一个"查天气 + 查订单 + 查库存"的智能客服

---

## 第三阶段：RAG 检索增强生成

> 目标：让 AI 基于你的私有数据回答，减少幻觉

| 主题 | 核心 API | 生产价值 |
|---|---|---|
| 文档加载 | `DocumentReader`（PDF/HTML/MD） | 企业文档知识入库 |
| 文本分块 | `TokenTextSplitter` | 控制块大小，平衡召回率和精度 |
| Embedding 向量化 | `EmbeddingModel` | 文本转向量用于语义检索 |
| Vector Store | `VectorStore`（PgVector/Redis/Milvus） | 持久化存储向量，支持大规模检索 |
| 相似度检索 | `similaritySearch()` | 根据用户问题找到最相关的文档片段 |
| RAG Advisor | `QuestionAnswerAdvisor` | 一行代码接入 RAG，自动检索+注入 |

### 推荐技术选型

| 组件 | 推荐方案 | 理由 |
|---|---|---|
| Vector Store | PgVector | 已有 PostgreSQL，零额外运维 |
| Embedding | 智谱 `embedding-3` | 和现有模型同厂商，延迟低 |
| 分块策略 | `TokenTextSplitter` 512 token | 通用场景够用，后续可按业务调优 |

**产出物**：加载一份产品 FAQ 文档，实现"基于文档的智能客服"

---

## 第四阶段：可观测性与运维

> 目标：线上出了问题能快速定位，成本可控

| 主题 | 技术 | 生产价值 |
|---|---|---|
| 调用链追踪 | Micrometer + Zipkin/Jaeger | 追踪一次 AI 调用的完整链路 |
| 指标监控 | token 用量 / 延迟 / 成功率 | 发现异常调用，控制成本 |
| 日志规范化 | 自定义 Advisor | 统一记录每次调用的输入输出 |
| 速率限制 | 令牌桶 / 滑动窗口 | 防止 API 被打爆，控制费用 |
| 重试与降级 | `RetryAdvisor` + 备用模型 | 模型故障时自动切换，不影响业务 |

**产出物**：一个带完整监控面板的 AI 服务

---

## 第五阶段：安全与合规

> 目标：通过安全审计，满足合规要求

| 主题 | 技术 | 生产价值 |
|---|---|---|
| 输入审核 | `SafeGuardAdvisor` / 自定义审核 | 拦截敏感/违规输入 |
| 输出过滤 | Advisor 层后处理 | 过滤不当输出 |
| Prompt 注入防护 | 输入校验 + System Prompt 加固 | 防止恶意用户操纵模型 |
| 敏感数据脱敏 | 日志 Advisor 中脱敏 | 手机号/身份证不落日志 |
| API Key 轮换 | Nacos 配置中心 + 环境变量 | Key 泄露时可快速更换 |

---

## 第六阶段：Agent 智能体

> 目标：从被动问答到主动执行任务

| 主题 | 核心 API | 生产价值 |
|---|---|---|
| 单 Agent + 工具 | `ToolCallAdvisor` | 模型自主规划并执行任务 |
| ReAct 模式 | 思考→行动→观察循环 | 复杂推理任务的可靠执行 |
| 多 Agent 协作 | Spring AI Alibaba Graph | 多角色协作完成复杂流程 |
| 状态机编排 | StateGraph / StateGraphBuilder | 可控的工作流，不是无限循环 |

---

## 第七阶段：工程化最佳实践

> 目标：可维护、可测试、可扩展

| 主题 | 做法 |
|---|---|
| 测试 | Mock ChatModel，断言 prompt 模板和输出格式 |
| 评估框架 | 用标注数据集自动评估回答质量 |
| 配置外部化 | Prompt 模板放 Nacos，模型参数可动态调整 |
| 多模型适配 | 统一 ChatClient 接口，底层切换模型只需改配置 |
| 灰度发布 | 新模型/新 prompt 先给 10% 流量验证 |

---

## 推荐实施顺序

```
当前 ✅
  ↓
第一阶段：提示词 + 多轮记忆（1-2天）
  ↓
第二阶段：Function Calling（2-3天）
  ↓
第三阶段：RAG（3-5天）← 生产核心价值
  ↓
第四阶段：可观测性（2-3天）← 上线必需
  ↓
第五阶段：安全合规（2-3天）← 生产必需
  ↓
第六阶段：Agent（按需）
  ↓
第七阶段：工程化（持续迭代）
```

**优先级**：RAG > 可观测性 > 安全 > Function Calling > Agent
