## Code Style

### 命名规范
- 类名：大驼峰，业务含义优先，禁止 Manager / Helper / Utils 堆砌
- 方法名：动词开头，query / create / update / delete / handle / build
- 常量：全大写下划线，集中放在 XxxConstants 类
- 枚举：大驼峰类名 + 全大写枚举值

### 包结构（每个服务内部统一）
controller / service / repository / domain / dto / config / exception

### 禁止事项
- 禁止在 Controller 层写业务逻辑
- 禁止直接返回 domain 对象，必须转 DTO
- 禁止用 e.printStackTrace()，统一用 log.error()
- 禁止硬编码 IP / 端口 / 密码
- 禁止在循环内做数据库查询

### 注释规范
- 公共 API 方法必须写 Javadoc
- 复杂业务逻辑写行内注释说明"为什么"而不是"做什么"
- TODO 注释必须带责任人和日期：// TODO(张三 2025-05-11): 待优化

## Exception & Result Rules

### Result 规范
- Controller 层所有方法返回类型必须是 Result<T> 或 Result<PageResult<T>>
- 禁止在 Controller 层直接返回 void / 原始对象 / ResponseEntity
- 分页统一用 PageResult<T> 包装后放入 Result<T>

### 异常规范
- Service 层只抛异常，禁止 try-catch 后返回 null 或 false 代替
- 明确的业务错误用对应子类：DataNotFoundException / ForbiddenException
- 兜底用 BusinessException，message 说明具体原因
- 禁止空 catch 块，禁止 catch(Exception e) 后不处理

### 禁止写法
// ❌ 禁止
if (user == null) return null;
if (!hasPermission) return Result.fail(...); // Controller 自己判断

// ✅ 正确
throw new DataNotFoundException("用户不存在");
throw new ForbiddenException("无权操作");