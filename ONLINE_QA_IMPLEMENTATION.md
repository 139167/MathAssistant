# OnlineQA AI集成实现

## 概述

本项目成功将Python的AI API请求代码移植到Android Kotlin中，实现了真实的AI问答功能。

## 主要功能

### 1. 真实的AI API集成
- 使用OkHttp库进行网络请求
- 支持流式响应（Server-Sent Events）
- 实时显示AI回复内容

### 2. 设备唯一用户ID生成
- 基于设备制造商、型号和Android ID生成唯一标识
- 格式：`android-{hashCode}`
- 确保每个设备都有独立的用户身份

### 3. 完整的日志系统
- 使用`[info]`、`[warning]`、`[err]`标签进行分类
- 记录请求过程、响应状态和错误信息
- 便于调试和监控

### 4. 用户界面改进
- 添加加载状态指示器
- 实时显示AI回复内容
- 错误处理和用户反馈

## 技术实现

### API服务类 (`ChatApiService.kt`)
```kotlin
class ChatApiService(private val context: Context) {
    // 设备唯一ID生成
    private fun generateDeviceUserId(): String
    
    // 流式聊天请求
    suspend fun sendChatMessage(
        message: String,
        onChunkReceived: (String) -> Unit,
        onComplete: (String) -> Unit,
        onError: (String) -> Unit
    )
}
```

### 界面集成 (`OnlineQAScreen.kt`)
- 使用协程处理异步网络请求
- 实时更新UI显示AI回复
- 错误处理和用户反馈

## 网络配置

### 权限配置 (`AndroidManifest.xml`)
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### 网络安全配置 (`network_security_config.xml`)
```xml
<domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="true">kiri.energysh.com</domain>
</domain-config>
```

## API配置

- **API密钥**: `app-GapMaZtJEt9w6n165oGAnDMh`
- **基础URL**: `http://kiri.energysh.com/v1/chat-messages`
- **响应模式**: `streaming` (流式响应)

## 使用方法

1. 启动应用
2. 导航到"在线问答"页面
3. 在输入框中输入问题
4. 点击发送按钮
5. 等待AI实时回复

## 日志输出示例

```
[info] 生成设备用户ID: 1234567890
[info] 发送聊天请求，用户ID: android-1234567890
[info] 消息内容: 什么是二次方程？
[info] 开始发送请求到: http://kiri.energysh.com/v1/chat-messages
[info] 请求成功，开始接收流式响应
[info] 接收到数据块: 二次方程是形如 ax² + bx + c = 0 的方程
[info] 流式响应结束
[info] 完整响应: 二次方程是形如 ax² + bx + c = 0 的方程...
```

## 依赖库

- `okhttp3:retrofit:2.9.0` - 网络请求
- `okhttp3:logging-interceptor:4.12.0` - 网络日志
- `kotlinx-coroutines` - 异步处理

## 注意事项

1. 确保设备有网络连接
2. API密钥需要有效
3. 服务器地址需要可访问
4. 首次使用可能需要等待网络连接建立

## 故障排除

### 常见问题
1. **网络连接失败**: 检查网络权限和连接状态
2. **API请求失败**: 验证API密钥和服务器状态
3. **响应解析错误**: 检查服务器返回的数据格式

### 调试方法
1. 查看Logcat日志输出
2. 检查网络请求状态
3. 验证设备ID生成是否正确
