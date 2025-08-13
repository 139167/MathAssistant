package com.example.mathassistant.api

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageApiService(private val context: Context) {
    
    companion object {
        private const val TAG = "ImageApiService"
        private const val API_KEY = "app-lhLucYCZZupkiXb29J5AwRJC"
        private const val BASE_URL = "http://kiri.energysh.com/v1"
        private const val UPLOAD_URL = "$BASE_URL/files/upload"
        private const val CHAT_URL = "$BASE_URL/chat-messages"
    }
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .build()
    
    /**
     * 生成设备唯一的用户ID
     */
    private fun generateDeviceUserId(): String {
        val androidId = android.provider.Settings.Secure.getString(
            context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        )
        val deviceModel = android.os.Build.MODEL
        val deviceManufacturer = android.os.Build.MANUFACTURER
        val hash = "device-${androidId}-${deviceManufacturer}-${deviceModel}".hashCode().toString()
        return hash
    }
    
    /**
     * 过滤掉<think>和</think>标签中的思考内容
     */
    private fun filterThinkTags(text: String): String {
        // 使用正则表达式移除<think>和</think>标签及其内容
        val thinkPattern = Regex("<think>.*?</think>", RegexOption.DOT_MATCHES_ALL)
        return thinkPattern.replace(text, "").trim()
    }
    
    /**
     * 将Uri转换为File
     */
    private suspend fun uriToFile(uri: Uri): File = withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "upload_image_${System.currentTimeMillis()}.jpg")
        
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        
        file
    }
    
    /**
     * 上传图片文件并获取文件ID
     */
    suspend fun uploadImage(uri: Uri): String? {
        return try {
            Log.i(TAG, "[info] 开始上传图片")
            
            val file = uriToFile(uri)
            val userId = generateDeviceUserId()
            
            Log.d(TAG, "[info] 用户ID: $userId")
            Log.d(TAG, "[info] 文件路径: ${file.absolutePath}")
            
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    file.name,
                    file.asRequestBody("application/octet-stream".toMediaType())
                )
                .addFormDataPart("user", userId)
                .build()
            
            val request = Request.Builder()
                .url(UPLOAD_URL)
                .addHeader("Authorization", "Bearer $API_KEY")
                .post(requestBody)
                .build()
            
            Log.d(TAG, "[info] 发送上传请求")
            
            withContext(Dispatchers.IO) {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        Log.d(TAG, "[info] 上传响应: $responseBody")
                        
                        val jsonObject = JSONObject(responseBody)
                        val fileId = jsonObject.optString("id")
                        
                        if (fileId.isNotEmpty()) {
                            Log.i(TAG, "[info] 图片上传成功，文件ID: $fileId")
                            fileId
                        } else {
                            Log.e(TAG, "[err] 响应中没有文件ID")
                            null
                        }
                    } else {
                        Log.e(TAG, "[err] 上传失败，状态码: ${response.code}")
                        Log.e(TAG, "[err] 响应内容: ${response.body?.string()}")
                        null
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "[err] 图片上传异常: ${e.message}", e)
            null
        }
    }
    
    /**
     * 发送图片批改请求（简化版本）
     */
    suspend fun sendImageCorrection(
        fileId: String,
        message: String = "检查题目",
        onChunkReceived: (String) -> Unit,
        onComplete: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            Log.i(TAG, "[info] 开始发送图片批改请求")
            Log.d(TAG, "[info] 文件ID: $fileId")
            Log.d(TAG, "[info] 批改消息: $message")
            
            val userId = generateDeviceUserId()
            Log.d(TAG, "[info] 设备用户ID: $userId")
            
            val payload = JSONObject().apply {
                put("inputs", JSONObject())
                put("query", message)
                put("response_mode", "streaming")
                put("conversation_id", "")
                put("user", userId)
                put("files", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("upload_file_id", fileId)
                        put("transfer_method", "local_file")
                        put("type", "image")
                    })
                })
            }
            
            Log.d(TAG, "[info] 请求Payload: ${payload.toString(2)}")
            
            val requestBody = payload.toString().toRequestBody("application/json".toMediaType())
            
            val request = Request.Builder()
                .url(CHAT_URL)
                .addHeader("Authorization", "Bearer $API_KEY")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "text/event-stream")
                .post(requestBody)
                .build()
            
            Log.d(TAG, "[info] 发送批改请求")
            Log.d(TAG, "[info] 请求URL: $CHAT_URL")
            Log.d(TAG, "[info] 请求Headers:")
            Log.d(TAG, "[info]   Authorization: Bearer $API_KEY")
            Log.d(TAG, "[info]   Content-Type: application/json")
            Log.d(TAG, "[info]   Accept: text/event-stream")
            
            withContext(Dispatchers.IO) {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        Log.i(TAG, "[info] 批改请求成功，开始接收响应")
                        
                        val responseBody = response.body
                        if (responseBody != null) {
                            val reader = responseBody.charStream().buffered()
                            // 先累积原始answer文本，最终一次性去掉<think>
                            val rawFullAnswer = StringBuilder()
                            
                            try {
                                reader.lineSequence().forEach { line ->
                                    // 打印原始SSE行
                                    Log.d(TAG, "[info] SSE原始行: $line")
                                    if (line.startsWith("data: ")) {
                                        val jsonStr = line.substring(6).trim()
                                        Log.d(TAG, "[info] SSE data段: $jsonStr")
                                        if (jsonStr.isNotEmpty()) {
                                            try {
                                                val jsonObject = JSONObject(jsonStr)
                                                if (jsonObject.has("answer")) {
                                                    val answer = jsonObject.getString("answer")
                                                    Log.d(TAG, "[info] 原始answer: $answer")
                                                    // 累积原始文本（可能跨chunk包含<think>片段）
                                                    rawFullAnswer.append(answer)
                                                    // 为了实时体验，流式UI仍显示去think后的片段，但最终结果会再次整体过滤
                                                    val filteredChunk = filterThinkTags(answer)
                                                    if (filteredChunk.isNotEmpty()) {
                                                        onChunkReceived(filteredChunk)
                                                        Log.d(TAG, "[info] 流式显示数据块(已去think): $filteredChunk")
                                                    } else {
                                                        Log.d(TAG, "[info] 该chunk仅<think>内容，流式不显示")
                                                    }
                                                } else if (jsonObject.has("event")) {
                                                    Log.d(TAG, "[info] 事件: ${jsonObject.optString("event")}")
                                                } else {
                                                    Log.d(TAG, "[warning] data中没有answer字段: $jsonStr")
                                                }
                                            } catch (e: Exception) {
                                                Log.w(TAG, "[warning] 解析JSON失败: $jsonStr", e)
                                            }
                                        }
                                    } else if (line.startsWith("event: ")) {
                                        Log.d(TAG, "[info] SSE事件行: ${line.removePrefix("event: ").trim()}")
                                    } else if (line.isBlank()) {
                                        Log.d(TAG, "[info] SSE心跳/空行")
                                    } else {
                                        Log.d(TAG, "[warning] 非data行: $line")
                                    }
                                }
                                
                                val resultRaw = rawFullAnswer.toString()
                                Log.d(TAG, "[info] 完整原始结果(含think): $resultRaw")
                                val resultFiltered = filterThinkTags(resultRaw)
                                Log.d(TAG, "[info] 最终结果(已去think): $resultFiltered")
                                onComplete(resultFiltered)
                                
                            } finally {
                                reader.close()
                            }
                        } else {
                            onError("响应体为空")
                        }
                        
                    } else {
                        val errorMessage = "批改请求失败，状态码: ${response.code}"
                        Log.e(TAG, "[err] $errorMessage")
                        Log.e(TAG, "[err] 响应内容: ${response.body?.string()}")
                        onError(errorMessage)
                    }
                }
            }
            
        } catch (e: Exception) {
            val errorMessage = "批改请求异常: ${e.message}"
            Log.e(TAG, "[err] $errorMessage", e)
            onError(errorMessage)
        }
    }
    
    /**
     * 完整的图片批改流程
     */
    suspend fun performImageCorrection(
        uri: Uri,
        onUploadProgress: (String) -> Unit,
        onCorrectionProgress: (String) -> Unit,
        onComplete: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            Log.i(TAG, "[info] 开始图片批改流程")
            
            // 步骤1：上传图片
            onUploadProgress("正在上传图片...")
            val fileId = uploadImage(uri)
            
            if (fileId != null) {
                onUploadProgress("图片上传成功")
                Log.i(TAG, "[info] 图片上传成功，开始批改")
                
                // 步骤2：发送批改请求
                onCorrectionProgress("正在分析图片内容...")
                sendImageCorrection(
                    fileId = fileId,
                    onChunkReceived = { chunk ->
                        onCorrectionProgress(chunk)
                    },
                    onComplete = { fullAnswer ->
                        Log.i(TAG, "[info] 批改流程完成")
                        onComplete(fullAnswer)
                    },
                    onError = { error ->
                        Log.e(TAG, "[err] 批改失败: $error")
                        onError(error)
                    }
                )
            } else {
                val errorMessage = "图片上传失败"
                Log.e(TAG, "[err] $errorMessage")
                onError(errorMessage)
            }
            
        } catch (e: Exception) {
            val errorMessage = "批改流程异常: ${e.message}"
            Log.e(TAG, "[err] $errorMessage", e)
            onError(errorMessage)
        }
    }
}
