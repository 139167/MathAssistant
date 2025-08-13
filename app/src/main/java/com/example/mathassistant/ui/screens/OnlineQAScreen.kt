package com.example.mathassistant.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mathassistant.api.ChatApiService
import com.example.mathassistant.utils.SafeAndroidMathText
import kotlinx.coroutines.launch

@Composable
fun OnlineQAScreen() {
    var questionText by remember { mutableStateOf("") }
    var chatHistory by remember { mutableStateOf(listOf<ChatMessage>()) }
    var isLoading by remember { mutableStateOf(false) }
    var currentAiMessage by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val chatApiService = remember { ChatApiService(context) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "在线问答",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 聊天历史
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chatHistory) { message ->
                ChatMessageItem(message)
            }
            
            // 显示正在输入的AI消息
            if (isLoading && currentAiMessage.isNotEmpty()) {
                item {
                    ChatMessageItem(ChatMessage(currentAiMessage, false))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 设置按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { /* 打开设置 */ },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "设置",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text("设置")
            }
        }
        
        // 输入区域
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = questionText,
                onValueChange = { questionText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("输入您的问题...") },
                singleLine = true,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.size(8.dp))
            Button(
                onClick = {
                    if (questionText.isNotBlank() && !isLoading) {
                        val userMessage = ChatMessage(questionText, true)
                        chatHistory = chatHistory + userMessage
                        
                        // 开始AI请求
                        isLoading = true
                        currentAiMessage = ""
                        
                        Log.i("OnlineQAScreen", "[info] 开始发送问题: $questionText")
                        
                        // 使用协程发送请求
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                            try {
                                chatApiService.sendChatMessage(
                                    message = questionText,
                                    onChunkReceived = { chunk ->
                                        currentAiMessage += chunk
                                        Log.d("OnlineQAScreen", "[info] 接收到AI响应块: $chunk")
                                    },
                                    onComplete = { fullAnswer ->
                                        Log.i("OnlineQAScreen", "[info] AI响应完成: $fullAnswer")
                                        val aiMessage = ChatMessage(fullAnswer, false)
                                        chatHistory = chatHistory + aiMessage
                                        currentAiMessage = ""
                                        isLoading = false
                                        questionText = ""
                                    },
                                    onError = { error ->
                                        Log.e("OnlineQAScreen", "[err] AI请求失败: $error")
                                        val errorMessage = ChatMessage("抱歉，请求失败: $error", false)
                                        chatHistory = chatHistory + errorMessage
                                        currentAiMessage = ""
                                        isLoading = false
                                    }
                                )
                            } catch (e: Exception) {
                                Log.e("OnlineQAScreen", "[err] 协程异常: ${e.message}", e)
                                val errorMessage = ChatMessage("抱歉，发生未知错误", false)
                                chatHistory = chatHistory + errorMessage
                                currentAiMessage = ""
                                isLoading = false
                            }
                        }
                    }
                },
                enabled = questionText.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Send, contentDescription = "发送")
                }
            }
        }
    }
}

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@Composable
fun ChatMessageItem(message: ChatMessage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (message.isUser) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = if (message.isUser) "用户" else "AI助手",
                    tint = if (message.isUser) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = if (message.isUser) "您" else "AI助手",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            
            // 使用SafeAndroidMathText组件来渲染包含LaTeX公式的文本
            if (message.isUser) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                SafeAndroidMathText(
                    text = message.text,
                    textSize = 14f
                )
            }
        }
    }
} 