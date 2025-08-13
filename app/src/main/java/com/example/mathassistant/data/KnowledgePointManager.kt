package com.example.mathassistant.data

/**
 * 知识点数据管理器
 * 提供幽默风趣的知识点讲解内容
 */
object KnowledgePointManager {
    
    /**
     * 根据知识点标题获取详细信息
     */
    fun getKnowledgePointDetail(title: String): KnowledgePointDetail? {
        // 添加调试日志
        android.util.Log.d("KnowledgePointManager", "查找知识点: $title")
        
        // 先尝试精确匹配
        var result = knowledgePointDetails.find { it.title == title }
        if (result != null) {
            android.util.Log.d("KnowledgePointManager", "精确匹配找到: ${result.title}")
            return result
        }
        
        // 如果没有精确匹配，尝试模糊匹配subtopic
        result = knowledgePointDetails.find { knowledgePoint ->
            knowledgePoint.subtopics.any { subtopic -> subtopic == title }
        }
        if (result != null) {
            android.util.Log.d("KnowledgePointManager", "subtopic匹配找到: ${result.title}")
            return result
        }
        
        // 如果还是没有找到，尝试包含匹配
        result = knowledgePointDetails.find { knowledgePoint ->
            knowledgePoint.title.contains(title) || 
            knowledgePoint.subtopics.any { subtopic -> subtopic.contains(title) }
        }
        
        if (result != null) {
            android.util.Log.d("KnowledgePointManager", "包含匹配找到: ${result.title}")
        } else {
            android.util.Log.w("KnowledgePointManager", "未找到知识点: $title")
        }
        
        return result
    }
    
    /**
     * 根据知识点ID获取详细信息
     */
    fun getKnowledgePointDetailById(id: String): KnowledgePointDetail? {
        return knowledgePointDetails.find { it.id == id }
    }
    
    /**
     * 获取所有知识点标题列表
     */
    fun getAllKnowledgePointTitles(): List<String> {
        return knowledgePointDetails.map { it.title }
    }
    
    /**
     * 知识点详情数据
     * 包含幽默风趣的讲解内容，适合小学生理解
     */
    private val knowledgePointDetails = listOf(
        // 一年级（上）知识点
        KnowledgePointDetail(
            id = "grade1_1_1",
            title = "0-10各数的认识和加减法",
            videoUrl = "https://example.com/video/grade1_1_1.mp4",
            explanation = "小朋友们，想象一下你有一盒糖果🍬！0就是空盒子，什么都没有。1就是一颗糖果，2就是两颗糖果...就像数手指头一样简单！加减法就像分享糖果，加法是得到更多糖果，减法是吃掉一些糖果。是不是很有趣呢？",
            tips = listOf(
                "数数时可以用手指帮忙，一根手指代表一个数",
                "加法就像把两堆糖果合在一起",
                "减法就像从一堆糖果中拿走一些"
            ),
            examples = listOf(
                "🍬🍬 + 🍬 = 🍬🍬🍬 (2+1=3)",
                "🍬🍬🍬 - 🍬 = 🍬🍬 (3-1=2)"
            ),
            subtopics = listOf(
                "认识0-10各数",
                "比较大小（=、>、<）",
                "10以内加减法（包括连加、连减和加减混合）"
            )
        ),
        
        KnowledgePointDetail(
            id = "grade1_1_2",
            title = "位置与顺序",
            videoUrl = "https://example.com/video/grade1_1_2.mp4",
            explanation = "小朋友们，你们知道吗？我们身边到处都是位置关系！就像排队买冰淇淋🍦，小明在小红前面，小红在小明后面。左右就像你的两只手，左手在左边，右手在右边。上下就像楼房，一楼在下面，二楼在上面！",
            tips = listOf(
                "前后：面向的方向是前，背向的方向是后",
                "左右：写字的手是右手，另一边是左手",
                "上下：天空在上，地面在下"
            ),
            examples = listOf(
                "排队：小明→小红→小华 (前→后)",
                "教室：黑板在教室前面，后门在教室后面"
            ),
            subtopics = listOf(
                "前后、左右、上下",
                "从不同方向看简单物体"
            )
        ),
        
        KnowledgePointDetail(
            id = "grade1_1_3",
            title = "认识钟表",
            videoUrl = "https://example.com/video/grade1_1_3.mp4",
            explanation = "滴答滴答⏰，时间在跑！钟表就像一个小魔法师，告诉我们现在是什么时候。整点就是分针指向12，时针指向数字。比如8点就是时针指向8，分针指向12。半时就是分针指向6，时针在两个数字中间！",
            tips = listOf(
                "整点：分针指向12，时针指向数字",
                "半时：分针指向6，时针在两个数字中间",
                "时针短，分针长，秒针最细最长"
            ),
            examples = listOf(
                "8点：时针指向8，分针指向12",
                "8点半：时针在8和9中间，分针指向6"
            ),
            subtopics = listOf(
                "整时、半时的认识"
            )
        ),
        
        // 一年级（下）知识点
        KnowledgePointDetail(
            id = "grade1_2_1",
            title = "20以内数的认识和加减法",
            videoUrl = "https://example.com/video/grade1_2_1.mp4",
            explanation = "哇！现在我们要认识更大的数了！20以内的数就像20个小朋友排排坐。进位加法就像坐公交车🚌，当座位满了就要到下一排。退位减法就像下车，当前排没人了就要从前一排借一个！",
            tips = listOf(
                "进位加法：个位满10要向十位进1",
                "退位减法：个位不够减要向十位借1",
                "可以用小棒或珠子帮助理解"
            ),
            examples = listOf(
                "8+7=15：8+2=10，再加5=15",
                "15-7=8：15-5=10，再减2=8"
            ),
            subtopics = listOf(
                "20以内数的认识",
                "20以内加减法（进位加法、退位减法）"
            )
        ),
        
        KnowledgePointDetail(
            id = "grade1_2_2",
            title = "认识图形（一）",
            videoUrl = "https://example.com/video/grade1_2_2.mp4",
            explanation = "小朋友们，我们身边到处都是图形！正方形就像魔方🎲，四条边一样长，四个角都是直角。长方形就像书本📚，对边相等。三角形就像三明治🥪，有三条边三个角。圆形就像太阳☀️，没有角，可以滚动！",
            tips = listOf(
                "正方形：四条边相等，四个角都是直角",
                "长方形：对边相等，四个角都是直角",
                "三角形：三条边，三个角",
                "圆形：没有角，可以滚动"
            ),
            examples = listOf(
                "正方形：魔方、骰子",
                "长方形：书本、门、窗户",
                "三角形：三明治、屋顶",
                "圆形：太阳、月亮、硬币"
            ),
            subtopics = listOf(
                "长方形、正方形、三角形、圆的初步认识"
            )
        ),
        
        // 二年级（上）知识点
        KnowledgePointDetail(
            id = "grade2_1_1",
            title = "100以内数的认识和加减法",
            videoUrl = "https://example.com/video/grade2_1_1.mp4",
            explanation = "太棒了！现在我们认识100以内的数了！就像100个小朋友开派对🎉！十位上的数字告诉我们有几个十，个位上的数字告诉我们有几个一。加减法就像数钱💰，十元加十元，一元加一元！",
            tips = listOf(
                "十位：表示有几个十",
                "个位：表示有几个一",
                "加法：先算十位，再算个位",
                "减法：先算十位，再算个位"
            ),
            examples = listOf(
                "23+45=68：20+40=60，3+5=8，60+8=68",
                "67-23=44：60-20=40，7-3=4，40+4=44"
            ),
            subtopics = listOf(
                "100以内数的认识",
                "100以内加减法（不进位加、不退位减、进位加、退位减）"
            )
        ),
        
        KnowledgePointDetail(
            id = "grade2_1_2",
            title = "表内乘法（一）",
            videoUrl = "https://example.com/video/grade2_1_2.mp4",
            explanation = "乘法口诀来啦！就像魔法咒语🔮！2×3=6，就是2个3相加等于6。乘法就像快速加法，不用一个一个数，直接念口诀就知道答案！2的乘法口诀最简单，就像数双数：2、4、6、8、10...",
            tips = listOf(
                "乘法是加法的简便运算",
                "2×3=6 就是 2+2+2=6",
                "乘法口诀要背熟，做题就快",
                "可以用小棒或图形帮助理解"
            ),
            examples = listOf(
                "2×3=6：2+2+2=6",
                "3×4=12：3+3+3+3=12",
                "4×5=20：4+4+4+4+4=20"
            ),
            subtopics = listOf(
                "乘法的初步认识",
                "2-6的乘法口诀"
            )
        ),
        
        // 三年级（上）知识点
        KnowledgePointDetail(
            id = "grade3_1_1",
            title = "分数的初步认识",
            videoUrl = "https://example.com/video/grade3_1_1.mp4",
            explanation = "分数来啦！就像分披萨🍕！一个披萨分成4份，你吃1份就是1/4。分数就像分享，分子告诉我们得到几份，分母告诉我们总共分成几份。1/2就是一半，1/4就是四分之一！",
            tips = listOf(
                "分子：表示得到几份",
                "分母：表示总共分成几份",
                "1/2是一半，1/4是四分之一",
                "分母越大，每份越小"
            ),
            examples = listOf(
                "1/2：一个苹果分成2份，吃1份",
                "1/4：一个披萨分成4份，吃1份",
                "3/4：一个披萨分成4份，吃3份"
            ),
            subtopics = listOf(
                "分数的初步认识",
                "简单的分数加减法"
            )
        ),
        
        // 四年级（上）知识点
        KnowledgePointDetail(
            id = "grade4_1_1",
            title = "大数的认识",
            videoUrl = "https://example.com/video/grade4_1_1.mp4",
            explanation = "哇！现在我们认识超级大的数了！就像数星星⭐！个、十、百、千、万、十万、百万、千万、亿！就像数钱一样，从右到左，每四位用逗号分开。1亿就是1后面8个0，超级大！",
            tips = listOf(
                "从右到左：个、十、百、千",
                "每四位用逗号分开",
                "万位：1万=10000",
                "亿位：1亿=100000000"
            ),
            examples = listOf(
                "1234：一千二百三十四",
                "12345：一万二千三百四十五",
                "123456789：一亿二千三百四十五万六千七百八十九"
            ),
            subtopics = listOf(
                "亿以内数的认识",
                "数的产生、十进制计数法",
                "亿以上数的认识",
                "计算工具的认识"
            )
        ),
        
        // 五年级（上）知识点
        KnowledgePointDetail(
            id = "grade5_1_1",
            title = "小数乘法",
            videoUrl = "https://example.com/video/grade5_1_1.mp4",
            explanation = "小数乘法来啦！就像数钱💰！0.5×0.3，先不管小数点，5×3=15，然后数小数点后面有几位，0.5有1位，0.3有1位，总共2位，所以答案是0.15！就像把小数点搬家！",
            tips = listOf(
                "先按整数乘法计算",
                "数小数点后面有几位",
                "在结果中从右往左数几位，点上小数点",
                "可以用钱币帮助理解"
            ),
            examples = listOf(
                "0.5×0.3=0.15：5×3=15，小数点后2位",
                "0.2×0.4=0.08：2×4=8，小数点后2位",
                "1.5×0.2=0.3：15×2=30，小数点后1位"
            ),
            subtopics = listOf(
                "小数乘整数",
                "小数乘小数",
                "积的近似数",
                "连乘、乘加、乘减",
                "整数乘法运算定律推广到小数"
            )
        ),
        
        // 六年级（上）知识点
        KnowledgePointDetail(
            id = "grade6_1_1",
            title = "圆的认识",
            videoUrl = "https://example.com/video/grade6_1_1.mp4",
            explanation = "圆形来啦！就像太阳☀️、月亮🌙、硬币！圆有一个中心点，从中心点到边缘的距离都相等，这个距离叫半径。直径是半径的2倍，就像穿过圆心的直线！圆的周长就像绕圆走一圈的路程！",
            tips = listOf(
                "圆心：圆的中心点",
                "半径：从圆心到边缘的距离",
                "直径：穿过圆心的直线，是半径的2倍",
                "圆周率π≈3.14"
            ),
            examples = listOf(
                "半径5厘米的圆，直径是10厘米",
                "直径8厘米的圆，半径是4厘米",
                "圆的周长=π×直径"
            ),
            subtopics = listOf(
                "圆的认识",
                "圆的周长",
                "圆的面积"
            )
        ),
        
        // 添加更多知识点，覆盖所有subtopic
        KnowledgePointDetail(
            id = "grade1_1_4",
            title = "分类",
            videoUrl = "https://example.com/video/grade1_1_4.mp4",
            explanation = "小朋友们，分类就像整理玩具箱🧸！我们可以按照颜色分类：红色的玩具放一起，蓝色的玩具放一起。也可以按照形状分类：圆形的放一起，方形的放一起。分类让我们的世界变得整齐有序！",
            tips = listOf(
                "选择一个标准来分类",
                "相同特征的物品放在一起",
                "分类后要检查是否遗漏"
            ),
            examples = listOf(
                "按颜色分类：红色、蓝色、绿色",
                "按形状分类：圆形、方形、三角形"
            ),
            subtopics = listOf(
                "按某一标准或自定标准对物体进行分类"
            )
        ),
        
        KnowledgePointDetail(
            id = "grade1_2_3",
            title = "位置与顺序",
            videoUrl = "https://example.com/video/grade1_2_3.mp4",
            explanation = "小朋友们，位置关系就像排队买冰淇淋🍦！小明在小红前面，小红在小明后面。左右就像你的两只手，左手在左边，右手在右边。上下就像楼房，一楼在下面，二楼在上面！",
            tips = listOf(
                "前后：面向的方向是前，背向的方向是后",
                "左右：写字的手是右手，另一边是左手",
                "上下：天空在上，地面在下"
            ),
            examples = listOf(
                "排队：小明→小红→小华 (前→后)",
                "教室：黑板在教室前面，后门在教室后面"
            ),
            subtopics = listOf(
                "上、下、前、后、左、右"
            )
        ),
        
        KnowledgePointDetail(
            id = "grade1_2_4",
            title = "认识钟表",
            videoUrl = "https://example.com/video/grade1_2_4.mp4",
            explanation = "滴答滴答⏰，时间在跑！钟表就像一个小魔法师，告诉我们现在是什么时候。整点就是分针指向12，时针指向数字。比如8点就是时针指向8，分针指向12。半时就是分针指向6，时针在两个数字中间！",
            tips = listOf(
                "整点：分针指向12，时针指向数字",
                "半时：分针指向6，时针在两个数字中间",
                "时针短，分针长，秒针最细最长"
            ),
            examples = listOf(
                "8点：时针指向8，分针指向12",
                "8点半：时针在8和9中间，分针指向6"
            ),
            subtopics = listOf(
                "整时、半时、几时刚过、快到几时"
            )
        ),
        
        KnowledgePointDetail(
            id = "grade2_1_3",
            title = "认识图形（二）",
            videoUrl = "https://example.com/video/grade2_1_3.mp4",
            explanation = "小朋友们，现在我们认识更多图形了！线段就像一根直直的绳子📏，有两个端点。角就像两条线相交形成的形状，直角就像书本的角，是90度！",
            tips = listOf(
                "线段有两个端点，不能延长",
                "角由两条射线组成",
                "直角是90度，像书本的角"
            ),
            examples = listOf(
                "线段：直尺的边",
                "角：书本的角、桌子的角",
                "直角：正方形的角"
            ),
            subtopics = listOf(
                "线段的初步认识",
                "角和直角的初步认识"
            )
        ),
        
        KnowledgePointDetail(
            id = "grade2_1_4",
            title = "观察物体",
            videoUrl = "https://example.com/video/grade2_1_4.mp4",
            explanation = "小朋友们，观察物体就像玩捉迷藏👀！从不同位置看同一个物体，看到的形状可能不一样。从前面看是长方形，从侧面看可能是正方形！",
            tips = listOf(
                "从不同位置观察同一个物体",
                "看到的形状可能不同",
                "要仔细观察每个面"
            ),
            examples = listOf(
                "从前面看：长方形",
                "从侧面看：正方形",
                "从上面看：圆形"
            ),
            subtopics = listOf(
                "从不同位置观察物体"
            )
        ),
        
        KnowledgePointDetail(
            id = "grade2_1_5",
            title = "统计与概率",
            videoUrl = "https://example.com/video/grade2_1_5.mp4",
            explanation = "小朋友们，统计就像数数游戏🔢！我们可以数一数班上有多少男生，多少女生。统计帮助我们了解事物的数量，让数据变得清晰明了！",
            tips = listOf(
                "收集数据要准确",
                "整理数据要清楚",
                "可以用表格或图形表示"
            ),
            examples = listOf(
                "统计班上男生女生人数",
                "统计喜欢不同水果的人数",
                "用表格记录数据"
            ),
            subtopics = listOf(
                "数据的收集和整理（一）"
            )
        )
    )
}
