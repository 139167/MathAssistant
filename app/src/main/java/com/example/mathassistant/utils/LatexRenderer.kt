package com.example.mathassistant.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

object LatexRenderer {
    
    private const val TAG = "LatexRenderer"
    
    /**
     * 检测文本中是否包含LaTeX公式
     * 支持 $...$ 和 $$...$$ 格式
     */
    fun containsLatex(text: String): Boolean {
        return text.contains(Regex("\\$[^\\$]+\\$")) || text.contains(Regex("\\$\\$[^\\$]+\\$\\$"))
    }
    
    /**
     * 提取文本中的LaTeX公式
     */
    fun extractLatexFormulas(text: String): List<LatexFormula> {
        val formulas = mutableListOf<LatexFormula>()
        
        // 匹配 $...$ 格式（行内公式）
        val inlinePattern = Regex("\\$([^\\$]+)\\$")
        inlinePattern.findAll(text).forEach { matchResult ->
            formulas.add(
                LatexFormula(
                    formula = matchResult.groupValues[1],
                    isBlock = false,
                    startIndex = matchResult.range.first,
                    endIndex = matchResult.range.last + 1
                )
            )
        }
        
        // 匹配 $$...$$ 格式（块级公式）
        val blockPattern = Regex("\\$\\$([^\\$]+)\\$\\$")
        blockPattern.findAll(text).forEach { matchResult ->
            formulas.add(
                LatexFormula(
                    formula = matchResult.groupValues[1],
                    isBlock = true,
                    startIndex = matchResult.range.first,
                    endIndex = matchResult.range.last + 1
                )
            )
        }
        
        return formulas.sortedBy { it.startIndex }
    }
    
    /**
     * 渲染LaTeX公式为Bitmap
     */
    fun renderLatexToBitmap(
        context: Context,
        latex: String,
        textSize: Float = 40f,
        textColor: Int = android.graphics.Color.BLACK,
        backgroundColor: Int = android.graphics.Color.TRANSPARENT
    ): Bitmap? {
        return try {
            Log.d(TAG, "[info] 开始渲染LaTeX公式: $latex")
            
            // 使用简单的文本渲染作为基础实现
            // 在实际项目中，你可能需要使用更专业的LaTeX渲染库
            val paint = Paint().apply {
                this.color = textColor
                this.textSize = textSize
                this.isAntiAlias = true
                this.typeface = Typeface.DEFAULT
            }
            
            val bounds = android.graphics.Rect()
            paint.getTextBounds(latex, 0, latex.length, bounds)
            
            val width = max(bounds.width() + 20, 100)
            val height = max(bounds.height() + 20, 50)
            
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            
            // 绘制背景
            if (backgroundColor != android.graphics.Color.TRANSPARENT) {
                canvas.drawColor(backgroundColor)
            }
            
            // 绘制文本
            canvas.drawText(latex, 10f, height / 2f + bounds.height() / 2f, paint)
            
            Log.d(TAG, "[info] LaTeX公式渲染完成")
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "[err] LaTeX公式渲染失败: ${e.message}", e)
            null
        }
    }
    
    /**
     * 处理文本中的LaTeX公式，返回可显示的Spanned文本
     */
    fun processLatexText(
        context: Context,
        text: String,
        textSize: Float = 40f
    ): Spanned {
        if (!containsLatex(text)) {
            return SpannableString(text)
        }
        
        val formulas = extractLatexFormulas(text)
        val spannableString = SpannableString(text)
        
        formulas.forEach { formula ->
            val bitmap = renderLatexToBitmap(context, formula.formula, textSize)
            if (bitmap != null) {
                val imageSpan = ImageSpan(context, bitmap)
                spannableString.setSpan(
                    imageSpan,
                    formula.startIndex,
                    formula.endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        
        return spannableString
    }
}

data class LatexFormula(
    val formula: String,
    val isBlock: Boolean,
    val startIndex: Int,
    val endIndex: Int
)

/**
 * Compose组件：渲染LaTeX公式
 */
@Composable
fun LatexText(
    text: String,
    modifier: Modifier = Modifier,
    textSize: Float = 16f
) {
    val context = LocalContext.current
    
    if (LatexRenderer.containsLatex(text)) {
        val formulas = LatexRenderer.extractLatexFormulas(text)
        
        // 这里可以实现更复杂的布局
        // 暂时使用简单的文本显示
        androidx.compose.material3.Text(
            text = text,
            modifier = modifier,
            fontSize = textSize.sp
        )
    } else {
        androidx.compose.material3.Text(
            text = text,
            modifier = modifier,
            fontSize = textSize.sp
        )
    }
}

/**
 * Compose组件：渲染单个LaTeX公式
 */
@Composable
fun LatexFormula(
    formula: String,
    modifier: Modifier = Modifier,
    textSize: Float = 40f
) {
    val context = LocalContext.current
    val bitmap = LatexRenderer.renderLatexToBitmap(context, formula, textSize)
    
    bitmap?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "LaTeX公式: $formula",
            modifier = modifier.size(200.dp)
        )
    }
}
