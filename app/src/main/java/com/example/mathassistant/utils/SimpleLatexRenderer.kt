package com.example.mathassistant.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

object SimpleLatexRenderer {
    
    private const val TAG = "SimpleLatexRenderer"
    
    /**
     * 检测文本中是否包含LaTeX公式
     */
    fun containsLatex(text: String): Boolean {
        return text.contains(Regex("\\$[^\\$]+\\$")) || text.contains(Regex("\\$\\$[^\\$]+\\$\\$"))
    }
    
    /**
     * 渲染LaTeX公式为Bitmap
     */
    fun renderLatexToBitmap(
        context: Context,
        latex: String,
        isBlock: Boolean = false,
        textSize: Float = if (isBlock) 50f else 40f,
        textColor: Int = android.graphics.Color.BLACK,
        backgroundColor: Int = android.graphics.Color.TRANSPARENT
    ): Bitmap? {
        return try {
            Log.d(TAG, "[info] 开始渲染LaTeX公式: $latex")
            
            val paint = Paint().apply {
                this.color = textColor
                this.textSize = textSize
                this.isAntiAlias = true
                this.typeface = Typeface.DEFAULT_BOLD
            }
            
            val bounds = android.graphics.Rect()
            paint.getTextBounds(latex, 0, latex.length, bounds)
            
            val padding = if (isBlock) 40 else 20
            val width = max(bounds.width() + padding * 2, 200)
            val height = max(bounds.height() + padding * 2, 80)
            
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            
            if (backgroundColor != android.graphics.Color.TRANSPARENT) {
                canvas.drawColor(backgroundColor)
            }
            
            val x = (width - bounds.width()) / 2f
            val y = (height + bounds.height()) / 2f
            canvas.drawText(latex, x, y, paint)
            
            Log.d(TAG, "[info] LaTeX公式渲染完成")
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "[err] LaTeX公式渲染失败: ${e.message}", e)
            null
        }
    }
}

/**
 * 简单的LaTeX文本渲染组件
 */
@Composable
fun SimpleLatexText(
    text: String,
    modifier: Modifier = Modifier,
    textSize: Float = 16f
) {
    val context = LocalContext.current
    
    if (SimpleLatexRenderer.containsLatex(text)) {
        // 如果包含LaTeX公式，显示原始文本并添加说明
        Column(modifier = modifier) {
            Text(
                text = text,
                fontSize = textSize.sp,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = "[包含LaTeX公式，需要专业渲染器]",
                fontSize = (textSize - 2).sp,
                color = androidx.compose.ui.graphics.Color.Gray,
                modifier = Modifier.padding(vertical = 2.dp)
            )
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
 * 单个LaTeX公式渲染组件
 */
@Composable
fun LatexFormula(
    formula: String,
    isBlock: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bitmap = SimpleLatexRenderer.renderLatexToBitmap(
        context = context,
        latex = formula,
        isBlock = isBlock
    )
    
    if (bitmap != null) {
        Box(
            modifier = modifier
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "LaTeX公式: $formula",
                modifier = Modifier.size(
                    width = if (isBlock) 300.dp else 200.dp,
                    height = 60.dp
                )
            )
        }
    } else {
        // 如果渲染失败，显示原始文本
        Text(
            text = "$${formula}$",
            fontWeight = FontWeight.Bold,
            modifier = modifier
        )
    }
}
