package com.example.mathassistant.data

/**
 * 知识点详情数据类
 * 包含视频链接和幽默风趣的讲解内容
 */
data class KnowledgePointDetail(
    val id: String,
    val title: String,
    val videoUrl: String,
    val explanation: String,
    val tips: List<String>,
    val examples: List<String>,
    val subtopics: List<String>
)

