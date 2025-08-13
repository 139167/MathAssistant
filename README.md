# 数学小导师 (Math Assistant)

一个基于Android Studio + Kotlin + Jetpack Compose开发的智能数学学习助手应用。

## 项目概述

根据项目规划图，本应用包含以下主要功能模块：

### 1. 知识广场 (Knowledge Square)
- **智能推荐**: 根据学习进度推荐相关内容
- **视频学习**: 精选教学视频
- **知识点树状图**: 系统化知识结构展示
- **子主题**: 分类学习内容
- **图片识别**: 作业批改和拍照搜题（待定）

### 2. 水平测试 (Level Test)
- **学生能力等级**: 显示当前等级和进度
- **已掌握的知识点**: 列出已掌握的内容
- **薄弱知识点清单**: 需要加强的领域
- **数据存储**: 测试结果存储和分析

### 3. 复习 (Review)
- **历史错题集**: 收集的错题和范围
- **薄弱知识点复习**: 针对性复习计划
- **复习题生成**: AI智能推荐复习内容

### 4. 在线问答 (Online Q&A)
- **交互式问答**: 实时AI问答功能
- **智能回复**: 根据问题类型提供专业解答
- **聊天历史**: 保存问答记录

### 5. 我的空间 (My Space)
- **用户档案**: 个人信息和学习统计
- **历史错题库**: 个人错题记录
- **薄弱知识点清单**: 个人学习弱点分析
- **学习统计**: 学习进度和成就

## 技术栈

- **开发语言**: Kotlin
- **UI框架**: Jetpack Compose
- **导航**: Navigation Compose
- **主题**: Material Design 3
- **最低SDK**: API 24 (Android 7.0)
- **目标SDK**: API 36 (Android 14)

## 项目结构

```
app/src/main/java/com/example/mathassistant/
├── MainActivity.kt                    # 主活动
└── ui/
    ├── screens/                       # 屏幕模块
    │   ├── KnowledgeSquareScreen.kt   # 知识广场
    │   ├── LevelTestScreen.kt         # 水平测试
    │   ├── ReviewScreen.kt            # 复习
    │   ├── OnlineQAScreen.kt         # 在线问答
    │   ├── MySpaceScreen.kt          # 我的空间
    │   ├── SettingsScreen.kt         # 设置
    │   └── ImageRecognitionScreen.kt # 图片识别
    └── theme/                        # 主题配置
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

## 功能特色

1. **现代化UI**: 使用Material Design 3，支持深色模式
2. **响应式设计**: 适配不同屏幕尺寸
3. **模块化架构**: 清晰的代码结构和组件化设计
4. **智能推荐**: AI驱动的个性化学习推荐
5. **实时交互**: 在线问答和即时反馈
6. **数据可视化**: 学习进度和统计图表

## 开发状态

- ✅ 基础框架搭建完成
- ✅ 主要UI界面实现
- ✅ 导航系统配置
- ✅ 主题和样式设置
- 🔄 功能模块开发中
- ⏳ 后端集成待开发
- ⏳ 图片识别功能待开发

## 构建和运行

1. 确保Android Studio已安装
2. 克隆项目到本地
3. 在Android Studio中打开项目
4. 等待Gradle同步完成
5. 连接Android设备或启动模拟器
6. 点击运行按钮

## 日志格式

项目使用统一的日志格式：
- `[info]`: 信息日志
- `[warning]`: 警告日志  
- `[err]`: 错误日志

## 后续开发计划

1. 实现数据持久化（Room数据库）
2. 集成网络请求（Retrofit）
3. 添加图片识别功能
4. 实现AI问答后端
5. 添加用户认证系统
6. 优化性能和用户体验

---

*这是一个教育类应用，旨在帮助学生更好地学习数学知识。* 