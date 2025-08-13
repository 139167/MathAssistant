package com.example.mathassistant.api

import android.content.Context
import android.provider.Settings
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class ChatApiService(private val context: Context) {
    
    companion object {
        private const val TAG = "ChatApiService"
        private const val API_KEY = "app-GapMaZtJEt9w6n165oGAnDMh"
        private const val BASE_URL = "http://kiri.energysh.com/v1/chat-messages"
    }
    
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    /**
     * 生成设备唯一的用户ID
     */
    private fun generateDeviceUserId(): String {
        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown"
        
        val deviceModel = android.os.Build.MODEL
        val deviceManufacturer = android.os.Build.MANUFACTURER
        
        // 组合设备信息生成唯一ID
        val deviceInfo = "$deviceManufacturer-$deviceModel-$androidId"
        val hashCode = deviceInfo.hashCode().toString().replace("-", "")
        
        Log.i(TAG, "[info] 生成设备用户ID: $hashCode")
        return "android-$hashCode"
    }
    
    /**
     * 发送聊天消息并获取流式响应
     */
    suspend fun sendChatMessage(
        message: String,
        onChunkReceived: (String) -> Unit,
        onComplete: (String) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        
        val deviceUserId = generateDeviceUserId()
        Log.i(TAG, "[info] 发送聊天请求，用户ID: $deviceUserId")
        Log.i(TAG, "[info] 消息内容: $message")
        
        try {
            val payload = JSONObject().apply {
                put("inputs", JSONObject())
                put("query", message)
                put("response_mode", "streaming")
                put("conversation_id", "")
                put("user", deviceUserId)
                put("files", JSONObject())
            }
            
            val requestBody = payload.toString().toRequestBody("application/json".toMediaType())
            
            val request = Request.Builder()
                .url(BASE_URL)
                .addHeader("Authorization", "Bearer $API_KEY")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "text/event-stream")
                .post(requestBody)
                .build()
            
            Log.i(TAG, "[info] 开始发送请求到: $BASE_URL")
            
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Log.i(TAG, "[info] 请求成功，开始接收流式响应")
                    
                    val responseBody = response.body
                    if (responseBody != null) {
                        val source = responseBody.source()
                        var fullAnswer = ""
                        
                        while (!source.exhausted()) {
                            val line = source.readUtf8Line()
                            if (line != null) {
                                if (line.startsWith("data: ")) {
                                    val jsonStr = line.substring(6).trim()
                                    try {
                                        val data = JSONObject(jsonStr)
                                        if (data.has("answer")) {
                                            val answer = data.getString("answer")
                                            fullAnswer += answer
                                            onChunkReceived(answer)
                                            Log.d(TAG, "[info] 接收到数据块: $answer")
                                        } else if (data.has("event") && data.getString("event") == "message_end") {
                                            Log.i(TAG, "[info] 流式响应结束")
                                            break
                                        }
                                    } catch (e: Exception) {
                                        Log.w(TAG, "[warning] JSON解析失败: $jsonStr")
                                    }
                                } else if (line.isNotEmpty()) {
                                    Log.d(TAG, "[info] 非数据行: $line")
                                }
                            }
                        }
                        
                        Log.i(TAG, "[info] 完整响应: $fullAnswer")
                        onComplete(fullAnswer)
                    } else {
                        Log.e(TAG, "[err] 响应体为空")
                        onError("响应体为空")
                    }
                } else {
                    val errorMessage = "请求失败，状态码: ${response.code}"
                    Log.e(TAG, "[err] $errorMessage")
                    Log.e(TAG, "[err] 响应内容: ${response.body?.string()}")
                    onError(errorMessage)
                }
            }
        } catch (e: IOException) {
            val errorMessage = "网络请求异常: ${e.message}"
            Log.e(TAG, "[err] $errorMessage", e)
            onError(errorMessage)
        } catch (e: Exception) {
            val errorMessage = "未知异常: ${e.message}"
            Log.e(TAG, "[err] $errorMessage", e)
            onError(errorMessage)
        }
    }
}
