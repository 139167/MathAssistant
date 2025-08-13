package com.example.mathassistant.ui.screens

import android.net.Uri
import android.view.ViewGroup
import android.widget.VideoView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.MaterialTheme
import com.example.mathassistant.data.KnowledgePointDetail
import com.example.mathassistant.data.VideoManager
import java.io.File
import android.media.MediaPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgePointDetailScreen(
    knowledgePoint: KnowledgePointDetail,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(knowledgePoint.title) },
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
            // 视频播放器区域
            VideoPlayerSection(
                knowledgePoint = knowledgePoint,
                context = context
            )
            
            // 知识点讲解内容
            KnowledgeExplanationSection(knowledgePoint = knowledgePoint)
        }
    }
}

@Composable
fun VideoPlayerSection(
    knowledgePoint: KnowledgePointDetail,
    context: android.content.Context
) {
    var videoFileName by remember { mutableStateOf<String?>(null) }
    var isVideoAvailable by remember { mutableStateOf(false) }
    var videoError by remember { mutableStateOf<String?>(null) }
    
    // 查找对应的视频文件
    LaunchedEffect(knowledgePoint.title) {
        val videoFile = VideoManager.getVideoForKnowledgePoint(knowledgePoint.title)
        videoFileName = videoFile
        if (videoFile != null) {
            isVideoAvailable = VideoManager.isVideoAvailable(context, videoFile)
            if (!isVideoAvailable) {
                videoError = "视频文件不存在: $videoFile"
            }
        } else {
            videoError = "未找到对应视频"
        }
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isVideoAvailable && videoFileName != null) {
                // 显示实际的视频播放器
                AndroidView(
                    factory = { context ->
                        VideoView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    update = { videoView ->
                        try {
                            // 从assets目录播放视频
                            val assetPath = VideoManager.getVideoAssetPath(videoFileName!!)
                            
                            // 使用MediaPlayer来播放assets中的视频
                            val assetFileDescriptor = context.assets.openFd(assetPath)
                            val mediaPlayer = MediaPlayer()
                            mediaPlayer.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.declaredLength)
                            mediaPlayer.prepare()
                            
                            // 直接使用MediaPlayer播放
                            mediaPlayer.start()
                            
                            // 设置错误监听器
                            mediaPlayer.setOnErrorListener { mp, what, extra ->
                                videoError = "播放错误: what=$what, extra=$extra"
                                true
                            }
                            
                            // 设置完成监听器
                            mediaPlayer.setOnCompletionListener { mp ->
                                // 视频播放完成
                            }
                            
                        } catch (e: Exception) {
                            videoError = "播放失败: ${e.message}"
                            e.printStackTrace()
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "正在播放: $videoFileName",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                // 显示视频不可用的提示
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.medium
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎥 视频播放器",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = videoError ?: "视频不可用",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "请检查视频文件是否存在",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KnowledgeExplanationSection(knowledgePoint: KnowledgePointDetail) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // 标题
        Text(
            text = "📚 知识点讲解",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 主要讲解内容
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "🎯 核心概念",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = knowledgePoint.explanation,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    lineHeight = 24.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 学习小贴士
        if (knowledgePoint.tips.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "💡 学习小贴士",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    knowledgePoint.tips.forEach { tip ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "✨ ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = tip,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // 实例演示
        if (knowledgePoint.examples.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🎪 实例演示",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    knowledgePoint.examples.forEach { example ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "🎯 ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = example,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
