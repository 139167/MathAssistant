package com.example.mathassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mathassistant.data.VideoManager
import com.example.mathassistant.data.KnowledgePointManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgePointTestScreen(
    onBackClick: () -> Unit,
    onKnowledgePointClick: (String) -> Unit
) {
    val context = LocalContext.current
    var allKnowledgePoints by remember { mutableStateOf<List<String>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        allKnowledgePoints = KnowledgePointManager.getAllKnowledgePointTitles()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("知识点跳转测试") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 测试说明
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🧪 知识点跳转测试",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "点击下面的知识点，测试是否能正确跳转到详情页面",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    Text(
                        text = "总共 ${allKnowledgePoints.size} 个知识点",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // 知识点列表
            allKnowledgePoints.forEach { knowledgePoint ->
                val videoFile = VideoManager.getVideoForKnowledgePoint(knowledgePoint)
                val hasVideo = videoFile != null
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasVideo) 
                            MaterialTheme.colorScheme.secondaryContainer 
                        else 
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = knowledgePoint,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasVideo) 
                                        MaterialTheme.colorScheme.onSecondaryContainer 
                                    else 
                                        MaterialTheme.colorScheme.onErrorContainer
                                )
                                
                                if (hasVideo) {
                                    Text(
                                        text = "📹 $videoFile",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                } else {
                                    Text(
                                        text = "❌ 未找到对应视频",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                            
                            Button(
                                onClick = { onKnowledgePointClick(knowledgePoint) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (hasVideo) 
                                        MaterialTheme.colorScheme.primary 
                                    else 
                                        MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text(
                                    text = if (hasVideo) "测试跳转" else "测试（无视频）",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
            
            // 测试结果统计
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📊 测试结果统计",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val totalPoints = allKnowledgePoints.size
                    val pointsWithVideo = allKnowledgePoints.count { 
                        VideoManager.getVideoForKnowledgePoint(it) != null 
                    }
                    val pointsWithoutVideo = totalPoints - pointsWithVideo
                    
                    Text(
                        text = "总知识点数：$totalPoints",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "有视频的知识点：$pointsWithVideo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "无视频的知识点：$pointsWithoutVideo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val successRate = if (totalPoints > 0) {
                        (pointsWithVideo * 100.0 / totalPoints).toInt()
                    } else 0
                    
                    Text(
                        text = "视频覆盖率：$successRate%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}
