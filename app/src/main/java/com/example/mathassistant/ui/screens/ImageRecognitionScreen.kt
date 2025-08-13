package com.example.mathassistant.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.mathassistant.api.ImageApiService
import com.example.mathassistant.utils.SafeAndroidMathText
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.content.FileProvider
import android.util.Log

@Composable
fun ImageRecognitionScreen() {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    var correctionResult by remember { mutableStateOf("") }
    var currentProgress by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val imageApiService = remember { ImageApiService(context) }
    val scope = rememberCoroutineScope()
    
    // 从相册选择图片
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            showResult = false
            correctionResult = ""
        }
    }
    
    // 拍照功能
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    
    val takePhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            // 拍照成功，设置选中的图片URI
            selectedImageUri = tempPhotoUri
            showResult = false
            correctionResult = ""
            Log.d("ImageRecognition", "拍照成功，文件已保存")
        } else {
            Log.d("ImageRecognition", "拍照取消或失败")
        }
    }
    
    // 创建临时文件用于拍照
    val createTempFile = {
        val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
    
    // 添加调试日志 - 只在有URI时记录，不强制检查文件
    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let { uri ->
            Log.d("ImageRecognition", "Selected URI: $uri")
            // 不强制检查文件，因为拍照时文件可能还不存在
        }
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "图片识别",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            HomeworkCorrectionSection(
                selectedImageUri = selectedImageUri,
                isProcessing = isProcessing,
                onTakePhoto = {
                    // 创建临时文件并启动相机
                    tempPhotoUri = createTempFile()
                    takePhoto.launch(tempPhotoUri!!)
                },
                onSelectFromGallery = {
                    imagePicker.launch("image/*")
                },
                onStartCorrection = {
                    selectedImageUri?.let { uri ->
                        scope.launch {
                            isProcessing = true
                            showResult = false
                            correctionResult = ""
                            
                            imageApiService.performImageCorrection(
                                uri = uri,
                                onUploadProgress = { progress ->
                                    currentProgress = progress
                                },
                                onCorrectionProgress = { chunk ->
                                    correctionResult += chunk
                                    currentProgress = "正在批改..."
                                },
                                onComplete = { fullResult ->
                                    correctionResult = fullResult
                                    isProcessing = false
                                    showResult = true
                                    currentProgress = "批改完成"
                                },
                                onError = { error ->
                                    correctionResult = "批改失败: $error"
                                    isProcessing = false
                                    showResult = true
                                    currentProgress = "批改失败"
                                }
                            )
                        }
                    }
                }
            )
        }
        
        // 显示选中的图片
        selectedImageUri?.let { uri ->
            item {
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
                            text = "选中的图片",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // 添加调试信息
                        Text(
                            text = "URI: $uri",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        // 添加错误处理的图片显示
                        var imageLoadError by remember { mutableStateOf(false) }
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (imageLoadError) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.BrokenImage,
                                        contentDescription = "图片加载失败",
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "图片加载失败",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            imageLoadError = false
                                        }
                                    ) {
                                        Text("重试")
                                    }
                                }
                            } else {
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = "选中的图片",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // 显示处理状态
        if (isProcessing) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = currentProgress,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
        
        // 显示批改结果
        if (showResult && correctionResult.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assignment,
                                contentDescription = "批改结果",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "批改结果",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        SafeAndroidMathText(
                            text = correctionResult,
                            textSize = 14f
                        )
                    }
                }
            }
        }
        
        item {
            PhotoSearchSection()
        }
        
        item {
            RecognitionStatusSection()
        }
    }
}

@Composable
fun HomeworkCorrectionSection(
    selectedImageUri: Uri?,
    isProcessing: Boolean,
    onTakePhoto: () -> Unit,
    onSelectFromGallery: () -> Unit,
    onStartCorrection: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "作业批改",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "作业批改",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "功能说明：",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            listOf(
                "• 拍照上传作业图片",
                "• AI自动识别数学题目",
                "• 智能批改和评分",
                "• 提供详细解析和错误分析"
            ).forEach { feature ->
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            if (selectedImageUri == null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onTakePhoto,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isProcessing
                    ) {
                        Icon(
                            Icons.Default.Camera,
                            contentDescription = "拍照",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("📸 拍照批改作业")
                    }
                    
                    Button(
                        onClick = onSelectFromGallery,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isProcessing
                    ) {
                        Icon(
                            Icons.Default.Image,
                            contentDescription = "相册",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("🖼️ 从相册选择")
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onSelectFromGallery,
                        enabled = !isProcessing
                    ) {
                        Icon(
                            Icons.Default.PhotoCamera,
                            contentDescription = "重新选择",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("重新选择")
                    }
                    
                    Button(
                        onClick = onStartCorrection,
                        enabled = !isProcessing
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "开始批改",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("开始批改")
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoSearchSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "拍照搜题",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "拍照搜题",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "功能说明：",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            listOf(
                "• 拍照上传题目图片",
                "• AI识别题目内容",
                "• 搜索相似题目和解析",
                "• 提供多种解题方法"
            ).forEach { feature ->
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* 拍照搜题 */ },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            ) {
                Icon(
                    Icons.Default.Camera,
                    contentDescription = "拍照",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text("拍照搜题 (开发中)")
            }
        }
    }
}

@Composable
fun RecognitionStatusSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "识别状态",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "当前状态：作业批改功能已实现",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "已实现功能：",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            listOf(
                "• 图片上传和预览",
                "• AI智能批改",
                "• 流式响应显示",
                "• LaTeX公式渲染"
            ).forEach { feature ->
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
} 