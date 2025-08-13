package com.example.mathassistant

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mathassistant.ui.screens.ImageRecognitionScreen
import com.example.mathassistant.ui.screens.KnowledgePointDetailScreen
import com.example.mathassistant.ui.screens.KnowledgePointTestScreen
import com.example.mathassistant.ui.screens.KnowledgeSquareScreen
import com.example.mathassistant.ui.screens.KnowledgeTreeDetailScreen
import com.example.mathassistant.ui.screens.LevelTestScreen
import com.example.mathassistant.ui.screens.MySpaceScreen
import com.example.mathassistant.ui.screens.OnlineQAScreen
import com.example.mathassistant.ui.screens.ReviewScreen
import com.example.mathassistant.ui.screens.VideoDebugScreen
import com.example.mathassistant.ui.screens.VideoFileCheckerScreen
import com.example.mathassistant.ui.theme.MathAssistantTheme
import com.example.mathassistant.data.KnowledgePointManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "[info] 应用启动")
        enableEdgeToEdge()
        setContent {
            MathAssistantTheme {
                MathAssistantApp()
            }
        }
    }
}

@Composable
fun MathAssistantApp() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    
    val items = listOf(
        NavigationItem("知识广场", Icons.Default.Home),
        NavigationItem("水平测试", Icons.Default.Quiz),
        NavigationItem("复习", Icons.Default.Refresh),
        NavigationItem("在线问答", Icons.Default.Person),
        NavigationItem("图片识别", Icons.Default.Camera),
        NavigationItem("我的空间", Icons.Default.Settings)
    )
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            when (index) {
                                0 -> navController.navigate("knowledge_square") { popUpTo(0) }
                                1 -> navController.navigate("level_test") { popUpTo(0) }
                                2 -> navController.navigate("review") { popUpTo(0) }
                                3 -> navController.navigate("online_qa") { popUpTo(0) }
                                4 -> navController.navigate("image_recognition") { popUpTo(0) }
                                5 -> navController.navigate("my_space") { popUpTo(0) }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "knowledge_square",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("knowledge_square") {
                KnowledgeSquareScreen(
                    onKnowledgeTreeClick = {
                        navController.navigate("knowledge_tree_detail")
                    },
                    onTestClick = {
                        navController.navigate("knowledge_point_test")
                    },
                    onDebugClick = {
                        navController.navigate("video_debug")
                    },
                    onVideoFileCheckClick = {
                        navController.navigate("video_file_checker")
                    }
                )
            }
            composable("knowledge_tree_detail") {
                KnowledgeTreeDetailScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onKnowledgePointClick = { knowledgePointTitle ->
                        // 添加调试日志
                        Log.d("MainActivity", "点击知识点: $knowledgePointTitle")
                        
                        // 查找知识点详情
                        val knowledgePoint = KnowledgePointManager.getKnowledgePointDetail(knowledgePointTitle)
                        if (knowledgePoint != null) {
                            Log.d("MainActivity", "找到知识点: ${knowledgePoint.title}, ID: ${knowledgePoint.id}")
                            // 将知识点数据作为参数传递到详情页面
                            navController.navigate("knowledge_point_detail/${knowledgePoint.id}")
                        } else {
                            Log.w("MainActivity", "未找到知识点: $knowledgePointTitle")
                        }
                    }
                )
            }
            composable(
                route = "knowledge_point_detail/{knowledgePointId}"
            ) { backStackEntry ->
                val knowledgePointId = backStackEntry.arguments?.getString("knowledgePointId")
                if (knowledgePointId != null) {
                    // 根据ID查找知识点详情
                    val knowledgePoint = KnowledgePointManager.getKnowledgePointDetailById(knowledgePointId)
                    if (knowledgePoint != null) {
                        KnowledgePointDetailScreen(
                            knowledgePoint = knowledgePoint,
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
            composable("level_test") { LevelTestScreen() }
            composable("review") { ReviewScreen() }
            composable("online_qa") { OnlineQAScreen() }
            composable("image_recognition") { ImageRecognitionScreen() }
            composable("my_space") { MySpaceScreen() }
            composable("video_debug") { 
                VideoDebugScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable("knowledge_point_test") { 
                KnowledgePointTestScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onKnowledgePointClick = { knowledgePointTitle ->
                        // 查找知识点详情
                        val knowledgePointDetail = KnowledgePointManager.getKnowledgePointDetail(knowledgePointTitle)
                        if (knowledgePointDetail != null) {
                            // 导航到知识点详情页面
                            navController.navigate("knowledge_point_detail/${knowledgePointDetail.id}")
                        } else {
                            android.util.Log.w("MainActivity", "未找到知识点: $knowledgePointTitle")
                        }
                    }
                )
            }
            composable("video_file_checker") { 
                VideoFileCheckerScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

data class NavigationItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Preview(showBackground = true)
@Composable
fun MathAssistantAppPreview() {
    MathAssistantTheme {
        MathAssistantApp()
    }
}

// 讲讲积分
// 讲讲欧拉函数
// 讲讲薛定谔方程