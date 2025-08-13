# AndroidMath LaTeX渲染集成

## 概述

本项目成功集成了 [AndroidMath](https://github.com/gregcockroft/AndroidMath) 库，这是一个专业的LaTeX数学公式渲染库，为Android应用提供了高质量的数学公式显示功能。

## 主要特性

### 1. 专业LaTeX渲染
- 基于 [iosMath](https://github.com/kostub/iosMath) 的Android移植版本
- 使用FreeType进行字体渲染
- 支持复杂的数学符号和公式

### 2. 无WebView依赖
- 纯原生Android实现
- 不依赖WebView或网络连接
- 性能优异，渲染速度快

### 3. 完整的LaTeX支持
- 支持所有标准LaTeX数学符号
- 支持复杂的数学表达式
- 支持分数、根号、积分、求和等高级符号

## 技术实现

### 依赖配置

#### settings.gradle.kts
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

#### app/build.gradle.kts
```kotlin
dependencies {
    // LaTeX数学公式渲染 - AndroidMath
    implementation("com.github.gregcockroft:AndroidMath:ALPHA") {
        exclude(group = "com.android.support", module = "support-compat")
        exclude(group = "com.android.support", module = "support-v4")
        exclude(group = "com.android.support", module = "appcompat-v7")
    }
}
```

### 核心组件

#### AndroidMathRenderer.kt
```kotlin
object AndroidMathRenderer {
    // 检测LaTeX公式
    fun containsLatex(text: String): Boolean
    
    // 解析文本，分离普通文本和LaTeX公式
    fun parseText(text: String): List<TextSegment>
}
```

#### Compose组件
```kotlin
// 文本渲染组件
@Composable
fun AndroidMathText(
    text: String,
    modifier: Modifier = Modifier,
    textSize: Float = 16f
)

// 单个公式渲染组件
@Composable
fun AndroidMathView(
    latex: String,
    isBlock: Boolean = false,
    modifier: Modifier = Modifier
)
```

## 使用方法

### 1. 在OnlineQA中使用
AI回复中的LaTeX公式会自动被检测和渲染：
- 行内公式：`$ax^2 + bx + c = 0$`
- 块级公式：`$$\int_{a}^{b} f(x) dx = F(b) - F(a)$$`

### 2. 测试页面
提供了`LatexTestScreen`来展示各种公式的渲染效果：
- 二次方程
- 勾股定理
- 积分公式
- 求和公式
- 矩阵
- 分数
- 根号
- 指数
- 极限

## 支持的LaTeX语法

### 基础数学符号
- `+`, `-`, `=`, `≠` (`\neq`)
- 上标：`x^2`, `e^{i\pi}`
- 下标：`x_i`, `a_{ij}`
- 分数：`\frac{a}{b}`
- 根号：`\sqrt{x}`, `\sqrt[n]{x}`

### 高级数学符号
- 积分：`\int_{a}^{b} f(x) dx`
- 求和：`\sum_{i=1}^{n} i`
- 极限：`\lim_{x \to \infty} f(x)`
- 矩阵：`\begin{pmatrix} a & b \\ c & d \end{pmatrix}`
- 希腊字母：`\alpha`, `\beta`, `\gamma`, `\pi`
- 特殊符号：`\infty`, `\partial`, `\nabla`

## 渲染效果

### 行内公式
- 与文本在同一行
- 较小的字体大小
- 左对齐显示

### 块级公式
- 独立成行
- 较大的字体大小
- 居中显示
- 更多的内边距

## 性能优势

### 1. 原生渲染
- 使用FreeType字体引擎
- 不依赖WebView
- 渲染速度快

### 2. 内存效率
- 轻量级实现
- 内存占用小
- 适合移动设备

### 3. 离线支持
- 完全离线工作
- 不依赖网络连接
- 响应速度快

## 错误处理

### 渲染失败时的降级显示
1. 显示原始LaTeX代码
2. 添加说明文字
3. 使用粗体字体突出显示

### 日志输出
```
[info] 渲染LaTeX公式: ax^2 + bx + c = 0
[info] LaTeX公式渲染完成
[err] LaTeX公式渲染失败: 错误信息
```

## 与之前实现的对比

### 之前的简单实现
- 使用Android Canvas API
- 只能渲染简单文本
- 不支持复杂数学符号
- 渲染效果有限

### AndroidMath实现
- 专业的LaTeX渲染引擎
- 支持所有标准LaTeX语法
- 高质量的数学字体
- 完美的排版效果

## 注意事项

1. **依赖冲突**：AndroidMath使用了旧的support库，需要排除冲突
2. **字体支持**：需要确保设备支持数学字体
3. **性能考虑**：复杂公式可能影响渲染性能
4. **兼容性**：确保在不同设备上显示一致

## 未来改进

1. **字体优化**：使用更专业的数学字体
2. **样式定制**：支持自定义颜色和样式
3. **交互功能**：添加公式点击和复制功能
4. **缓存机制**：实现公式渲染缓存

## 总结

AndroidMath库的成功集成为MathAssistant应用提供了专业的LaTeX数学公式渲染功能。相比之前的简单实现，AndroidMath提供了：

- **更专业的渲染质量**
- **更完整的LaTeX支持**
- **更好的性能表现**
- **更稳定的兼容性**

这为数学问答应用提供了更好的用户体验，让用户能够看到美观、准确的数学公式显示。

## 参考链接

- [AndroidMath GitHub](https://github.com/gregcockroft/AndroidMath)
- [iosMath GitHub](https://github.com/kostub/iosMath)
- [FreeType](https://www.freetype.org/)
