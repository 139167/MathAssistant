package com.example.mathassistant.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgeTreeDetailScreen(
    onBackClick: () -> Unit,
    onKnowledgePointClick: (String) -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("小学数学知识点树状图") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(0.5f..3f)
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .scale(scale)
                    .padding(16.dp)
            ) {
                // 年级目录
                GradeDirectorySection(onKnowledgePointClick = onKnowledgePointClick)
            }
        }
    }
}

@Composable
fun GradeDirectorySection(onKnowledgePointClick: (String) -> Unit) {
    val gradeDataList = listOf(
        GradeData("一年级（上）", listOf(
            TopicItem("0-10各数的认识和加减法", listOf(
                "认识0-10各数",
                "比较大小（=、>、<）",
                "10以内加减法（包括连加、连减和加减混合）"
            )),
            TopicItem("位置与顺序", listOf(
                "前后、左右、上下",
                "从不同方向看简单物体"
            )),
            TopicItem("认识钟表", listOf(
                "整时、半时的认识"
            ))
        )),
        GradeData("一年级（下）", listOf(
            TopicItem("20以内数的认识和加减法", listOf(
                "20以内数的认识",
                "20以内加减法（进位加法、退位减法）"
            )),
            TopicItem("认识图形（一）", listOf(
                "长方形、正方形、三角形、圆的初步认识"
            ))
        )),
        GradeData("二年级（上）", listOf(
            TopicItem("100以内数的认识和加减法（二）", listOf(
                "100以内数的认识",
                "100以内加减法（不进位加、不退位减、进位加、退位减）"
            )),
            TopicItem("表内乘法（一）", listOf(
                "乘法的初步认识",
                "2-6的乘法口诀"
            )),
            TopicItem("认识图形（二）", listOf(
                "线段的初步认识",
                "角和直角的初步认识"
            )),
            TopicItem("观察物体", listOf(
                "从不同位置观察物体"
            )),
            TopicItem("统计与概率", listOf(
                "数据的收集和整理（一）"
            ))
        )),
        GradeData("二年级（下）", listOf(
            TopicItem("100以内数的认识和加减法（三）", listOf(
                "100以内加减法（连加、连减、加减混合）"
            )),
            TopicItem("表内除法（一）", listOf(
                "除法的初步认识",
                "用2-6的乘法口诀求商"
            )),
            TopicItem("图形与变换", listOf(
                "平移和旋转",
                "锐角和钝角"
            )),
            TopicItem("万以内数的认识", listOf(
                "万以内数的认识",
                "万以内数的加减法（不进位加、不退位减）"
            )),
            TopicItem("克和千克", listOf(
                "克和千克的认识"
            )),
            TopicItem("数学广角——推理", listOf(
                "简单的推理问题"
            ))
        )),
        GradeData("三年级（上）", listOf(
            TopicItem("时、分、秒", listOf(
                "时、分、秒的认识",
                "时间的计算"
            )),
            TopicItem("万以内的加法和减法（一）", listOf(
                "万以内的加法和减法（不进位加、不退位减）"
            )),
            TopicItem("测量", listOf(
                "毫米、分米、千米的认识",
                "吨的认识"
            )),
            TopicItem("多位数乘一位数", listOf(
                "多位数乘一位数的乘法"
            )),
            TopicItem("分数的初步认识", listOf(
                "分数的初步认识",
                "简单的分数加减法"
            )),
            TopicItem("四边形", listOf(
                "四边形的初步认识",
                "平行四边形的初步认识"
            )),
            TopicItem("数学广角——集合", listOf(
                "简单的集合问题"
            ))
        )),
        GradeData("三年级（下）", listOf(
            TopicItem("位置与方向（一）", listOf(
                "东、南、西、北、东北、东南、西北、西南"
            )),
            TopicItem("除数是一位数的除法", listOf(
                "除数是一位数的除法"
            )),
            TopicItem("复式统计表", listOf(
                "复式统计表的认识"
            )),
            TopicItem("两位数乘两位数", listOf(
                "两位数乘两位数的乘法"
            )),
            TopicItem("面积", listOf(
                "面积和面积单位",
                "长方形、正方形面积的计算"
            )),
            TopicItem("年、月、日", listOf(
                "年、月、日的认识",
                "24时计时法"
            )),
            TopicItem("小数的初步认识", listOf(
                "小数的初步认识",
                "简单的小数加减法"
            )),
            TopicItem("数学广角——搭配（二）", listOf(
                "简单的搭配问题"
            ))
        )),
        GradeData("四年级（上）", listOf(
            TopicItem("大数的认识", listOf(
                "亿以内数的认识",
                "亿以上数的认识"
            )),
            TopicItem("公顷和平方千米", listOf(
                "公顷和平方千米的认识"
            )),
            TopicItem("角的度量", listOf(
                "直线、射线和角",
                "角的度量"
            )),
            TopicItem("三位数乘两位数", listOf(
                "三位数乘两位数的乘法"
            )),
            TopicItem("平行四边形和梯形", listOf(
                "平行四边形和梯形的认识"
            )),
            TopicItem("除数是两位数的除法", listOf(
                "除数是两位数的除法"
            )),
            TopicItem("条形统计图", listOf(
                "条形统计图的认识"
            )),
            TopicItem("数学广角——优化", listOf(
                "简单的优化问题"
            ))
        )),
        GradeData("四年级（下）", listOf(
            TopicItem("四则运算", listOf(
                "四则混合运算的顺序",
                "小括号和中括号"
            )),
            TopicItem("观察物体（二）", listOf(
                "从不同位置观察立体图形"
            )),
            TopicItem("运算定律", listOf(
                "加法交换律和结合律",
                "乘法交换律、结合律和分配律"
            )),
            TopicItem("小数的意义和性质", listOf(
                "小数的意义",
                "小数的性质"
            )),
            TopicItem("三角形", listOf(
                "三角形的认识",
                "三角形的分类"
            )),
            TopicItem("小数的加法和减法", listOf(
                "小数的加法和减法"
            )),
            TopicItem("图形的运动（二）", listOf(
                "平移和旋转",
                "轴对称"
            )),
            TopicItem("平均数与条形统计图", listOf(
                "平均数的认识",
                "复式条形统计图"
            )),
            TopicItem("数学广角——鸡兔同笼", listOf(
                "简单的鸡兔同笼问题"
            ))
        )),
        GradeData("五年级（上）", listOf(
            TopicItem("小数乘法", listOf(
                "小数乘整数",
                "小数乘小数"
            )),
            TopicItem("位置", listOf(
                "用数对确定位置"
            )),
            TopicItem("小数除法", listOf(
                "除数是整数的小数除法",
                "除数是小数的小数除法"
            )),
            TopicItem("可能性", listOf(
                "事件发生的可能性"
            )),
            TopicItem("简易方程", listOf(
                "用字母表示数",
                "解简易方程"
            )),
            TopicItem("多边形的面积", listOf(
                "平行四边形的面积",
                "三角形的面积",
                "梯形的面积"
            )),
            TopicItem("数学广角——植树问题", listOf(
                "简单的植树问题"
            ))
        )),
        GradeData("五年级（下）", listOf(
            TopicItem("观察物体（三）", listOf(
                "从不同位置观察立体图形"
            )),
            TopicItem("因数与倍数", listOf(
                "因数和倍数",
                "2、5、3的倍数的特征",
                "质数和合数"
            )),
            TopicItem("长方体和正方体", listOf(
                "长方体和正方体的认识",
                "长方体和正方体的表面积",
                "长方体和正方体的体积"
            )),
            TopicItem("分数的意义和性质", listOf(
                "分数的意义",
                "真分数和假分数",
                "分数的基本性质"
            )),
            TopicItem("图形的运动（三）", listOf(
                "旋转",
                "欣赏设计"
            )),
            TopicItem("分数的加法和减法", listOf(
                "同分母分数加、减法",
                "异分母分数加、减法"
            )),
            TopicItem("打电话", listOf(
                "打电话问题"
            ))
        )),
        GradeData("六年级（上）", listOf(
            TopicItem("分数乘法", listOf(
                "分数乘整数",
                "分数乘分数"
            )),
            TopicItem("位置与方向（二）", listOf(
                "用方向和距离确定位置"
            )),
            TopicItem("分数除法", listOf(
                "分数除以整数",
                "一个数除以分数"
            )),
            TopicItem("比", listOf(
                "比的意义",
                "比的基本性质"
            )),
            TopicItem("圆", listOf(
                "圆的认识",
                "圆的周长",
                "圆的面积"
            )),
            TopicItem("百分数（一）", listOf(
                "百分数的意义和写法",
                "百分数和分数、小数的互化"
            )),
            TopicItem("扇形统计图", listOf(
                "扇形统计图的认识"
            )),
            TopicItem("数学广角——数与形", listOf(
                "数与形的结合"
            ))
        )),
        GradeData("六年级（下）", listOf(
            TopicItem("负数", listOf(
                "负数的认识",
                "正负数"
            )),
            TopicItem("百分数（二）", listOf(
                "折扣",
                "成数",
                "税率",
                "利率"
            )),
            TopicItem("圆柱与圆锥", listOf(
                "圆柱的认识",
                "圆柱的表面积",
                "圆柱的体积",
                "圆锥的认识"
            )),
            TopicItem("比例", listOf(
                "比例的意义和基本性质",
                "正比例和反比例",
                "比例的应用"
            )),
            TopicItem("统计与概率", listOf(
                "统计图",
                "可能性"
            )),
            TopicItem("数学思考", listOf(
                "数学思考方法"
            )),
            TopicItem("综合与实践", listOf(
                "综合实践活动"
            ))
        ))
    )
    
    gradeDataList.forEach { gradeData ->
        GradeDirectoryCard(
            gradeData = gradeData,
            onKnowledgePointClick = onKnowledgePointClick
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun GradeDirectoryCard(
    gradeData: GradeData,
    onKnowledgePointClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(0.dp)
        ) {
            // 年级标题行（可点击）
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        expanded = !expanded
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = gradeData.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "收起" else "展开",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            // 展开的内容
            if (expanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    gradeData.topics.forEach { topic ->
                        TopicCard(
                            topic = topic,
                            onKnowledgePointClick = onKnowledgePointClick
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TopicCard(
    topic: TopicItem,
    onKnowledgePointClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = topic.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            topic.subtopics.forEach { subtopic ->
                Row(
                    modifier = Modifier.padding(start = 18.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    // 让知识点文字本身可以点击
                    Text(
                        text = subtopic,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        lineHeight = 20.sp,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onKnowledgePointClick(subtopic)
                        }
                    )
                }
            }
        }
    }
}

data class GradeData(
    val title: String,
    val topics: List<TopicItem>
)

data class TopicItem(
    val title: String,
    val subtopics: List<String>
)
