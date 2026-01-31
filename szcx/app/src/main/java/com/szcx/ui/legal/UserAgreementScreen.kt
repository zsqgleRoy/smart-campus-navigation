package com.szcx.ui.legal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAgreementScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "用户协议",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "用户协议",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            
            Text(
                text = "生效日期：2026年1月1日",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            
            SectionTitle("1. 协议的接受")
            SectionContent("""
                欢迎使用数智成贤（以下简称"本应用"）。在使用本应用之前，请您仔细阅读本用户协议。
                当您下载、安装、使用本应用时，即表示您已阅读、理解并同意接受本协议的全部内容。
                如果您不同意本协议的任何内容，请立即停止使用本应用。
            """.trimIndent())
            
            SectionTitle("2. 服务说明")
            SectionContent("""
                本应用是一款校园网站导航应用，主要功能包括：
                
                • 提供校园常用网站的快速访问入口
                • 支持网站分类浏览和搜索
                • 支持收藏常用网站
                • 记录访问历史
                • 支持自定义添加网站
                
                本应用仅作为导航工具，不提供网站内容本身。
            """.trimIndent())
            
            SectionTitle("3. 用户账户")
            SectionContent("""
                本应用为本地应用，无需注册账户即可使用。所有数据存储在您的设备本地。
                您可以通过应用设置管理您的个人偏好和收藏内容。
            """.trimIndent())
            
            SectionTitle("4. 用户行为规范")
            SectionContent("""
                在使用本应用时，您同意遵守以下规范：
                
                • 不得利用本应用从事任何违法违规活动
                • 不得添加包含违法、有害、色情、暴力等内容的网站
                • 不得恶意破坏应用功能或干扰其他用户使用
                • 尊重知识产权，不得侵犯他人合法权益
                • 遵守相关法律法规和学校规章制度
            """.trimIndent())
            
            SectionTitle("5. 知识产权")
            SectionContent("""
                本应用的所有知识产权归开发者所有，包括但不限于：
                
                • 应用软件、代码、界面设计
                • 商标、标识、名称
                • 文档、说明材料
                
                未经授权，不得复制、修改、传播本应用的任何部分。
            """.trimIndent())
            
            SectionTitle("6. 免责声明")
            SectionContent("""
                本应用提供的服务基于"现状"和"可用"的基础：
                
                • 本应用不对通过本应用访问的第三方网站内容负责
                • 不保证所有链接的网站始终可用或准确
                • 不对因使用或无法使用本应用造成的任何损失承担责任
                • 用户访问外部网站时，应自行判断网站内容的合法性和安全性
            """.trimIndent())
            
            SectionTitle("7. 服务变更与终止")
            SectionContent("""
                我们保留随时修改或终止服务的权利，包括但不限于：
                
                • 更新应用功能和界面
                • 修改或删除某些功能
                • 暂停或终止服务
                
                我们会尽力提前通知用户重大变更，但不对通知的及时性承担责任。
            """.trimIndent())
            
            SectionTitle("8. 协议修改")
            SectionContent("""
                我们有权随时修改本协议。修改后的协议将在应用内公布，
                并在生效日期处标注最新日期。继续使用本应用即表示您接受修改后的协议。
                如果您不同意修改内容，请停止使用本应用。
            """.trimIndent())
            
            SectionTitle("9. 适用法律")
            SectionContent("""
                本协议的订立、执行和解释及争议的解决均应适用中华人民共和国法律。
                如双方就本协议内容或其执行发生任何争议，双方应尽量友好协商解决；
                协商不成时，任何一方均可向有管辖权的人民法院提起诉讼。
            """.trimIndent())
            
            SectionTitle("10. 其他条款")
            SectionContent("""
                • 本协议构成双方对本应用使用事宜的完整协议
                • 如本协议的任何条款被认定为无效，不影响其他条款的效力
                • 本协议的标题仅为方便阅读而设，不影响本协议任何条款的含义或解释
            """.trimIndent())
            
            SectionTitle("11. 联系我们")
            SectionContent("""
                如果您对本协议有任何疑问，请通过以下方式联系我们：
                
                • 在应用内"设置-联系我们"页面查看联系方式
                • 通过官方QQ群或微信联系我们
            """.trimIndent())
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun SectionContent(content: String) {
    Text(
        text = content,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        modifier = Modifier.padding(bottom = 8.dp),
        color = MaterialTheme.colorScheme.onSurface
    )
}

