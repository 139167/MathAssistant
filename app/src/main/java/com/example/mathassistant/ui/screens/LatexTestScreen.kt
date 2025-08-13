package com.example.mathassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathassistant.utils.SafeAndroidMathText
import com.example.mathassistant.utils.SafeAndroidMathView

@Composable
fun LatexTestScreen() {
    val testExamples = listOf(
        "这是一个简单的文本，没有公式。",
        "二次方程的一般形式是 \$ax^2 + bx + c = 0\$，其中 \$a \\neq 0\$。",
        "勾股定理：\$a^2 + b^2 = c^2\$",
        "积分公式：\$\$\\int_{a}^{b} f(x) dx = F(b) - F(a)\$\$",
        "求和公式：\$\$\\sum_{i=1}^{n} i = \\frac{n(n+1)}{2}\$\$",
        "矩阵：\$\$\\begin{pmatrix} a & b \\\\ c & d \\end{pmatrix}\$\$",
        "分数：\$\\frac{1}{2} + \\frac{1}{3} = \\frac{5}{6}\$",
        "根号：\$\\sqrt{x^2 + y^2}\$",
        "指数：\$e^{i\\pi} + 1 = 0\$",
        "极限：\$\$\\lim_{x \\to \\infty} \\frac{1}{x} = 0\$\$"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "LaTeX公式渲染测试 (AndroidMath)",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(testExamples) { example ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "示例：",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        SafeAndroidMathText(
                            text = example,
                            textSize = 14f
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleLatexTest() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "简单LaTeX公式测试 (AndroidMath)",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("行内公式：")
                SafeAndroidMathView(
                    latex = "ax^2 + bx + c = 0",
                    isBlock = false
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("块级公式：")
                SafeAndroidMathView(
                    latex = "\\int_{a}^{b} f(x) dx = F(b) - F(a)",
                    isBlock = true
                )
            }
        }
    }
}
