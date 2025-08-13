package com.example.mathassistant.utils

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

object AndroidMathRenderer {
    
    private const val TAG = "AndroidMathRenderer"
    
    /**
     * 检测文本中是否包含LaTeX公式
     */
    fun containsLatex(text: String): Boolean {
        return text.contains(Regex("\\$[^\\$]+\\$")) || text.contains(Regex("\\$\\$[^\\$]+\\$\\$"))
    }
    
    /**
     * 解析文本，分离普通文本和LaTeX公式
     */
    fun parseText(text: String): List<TextSegment> {
        val segments = mutableListOf<TextSegment>()
        var currentIndex = 0
        
        // 匹配所有LaTeX公式
        val latexPattern = Regex("(\\$\\$[^\\$]+\\$\\$)|(\\$[^\\$]+\\$)")
        val matches = latexPattern.findAll(text)
        
        for (match in matches) {
            // 添加公式前的普通文本
            if (match.range.first > currentIndex) {
                val plainText = text.substring(currentIndex, match.range.first)
                if (plainText.isNotEmpty()) {
                    segments.add(TextSegment.PlainText(plainText))
                }
            }
            
            // 添加LaTeX公式
            val formula = match.value
            val isBlock = formula.startsWith("$$")
            val latexContent = if (isBlock) {
                formula.substring(2, formula.length - 2)
            } else {
                formula.substring(1, formula.length - 1)
            }
            
            segments.add(TextSegment.LatexFormula(latexContent, isBlock))
            currentIndex = match.range.last + 1
        }
        
        // 添加剩余的普通文本
        if (currentIndex < text.length) {
            val remainingText = text.substring(currentIndex)
            if (remainingText.isNotEmpty()) {
                segments.add(TextSegment.PlainText(remainingText))
            }
        }
        
        return segments
    }
}

sealed class TextSegment {
    data class PlainText(val text: String) : TextSegment()
    data class LatexFormula(val formula: String, val isBlock: Boolean) : TextSegment()
}

/**
 * 安全的LaTeX渲染组件（降级为普通文本显示）
 */
@Composable
fun SafeAndroidMathText(
    text: String,
    modifier: Modifier = Modifier,
    textSize: Float = 16f
) {
    if (AndroidMathRenderer.containsLatex(text)) {
        val segments = AndroidMathRenderer.parseText(text)
        
        Column(modifier = modifier) {
            segments.forEach { segment ->
                when (segment) {
                    is TextSegment.PlainText -> {
                        Text(
                            text = segment.text,
                            fontSize = textSize.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                    is TextSegment.LatexFormula -> {
                        // 降级显示：显示原始LaTeX代码
                        Text(
                            text = "$${segment.formula}$$",
                            fontSize = (textSize - 2).sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    } else {
        Text(
            text = text,
            fontSize = textSize.sp,
            modifier = modifier
        )
    }
}

/**
 * 安全的AndroidMath视图组件（降级为文本显示）
 */
@Composable
fun SafeAndroidMathView(
    latex: String,
    isBlock: Boolean = false,
    modifier: Modifier = Modifier
) {
    Log.d("SafeAndroidMathView", "[info] 渲染LaTeX公式: $latex")
    
    // 降级显示：显示原始LaTeX代码
    Text(
        text = "$$latex$$",
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = modifier
            .fillMaxWidth()
            .height(if (isBlock) 80.dp else 60.dp)
    )
}
