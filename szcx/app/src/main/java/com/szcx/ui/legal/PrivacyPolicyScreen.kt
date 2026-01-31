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
fun PrivacyPolicyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "隐私政策",
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
                text = "隐私政策",
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
            
            SectionTitle("1. 信息收集")
            SectionContent("""
                数智成贤（以下简称"本应用"）致力于保护您的隐私。我们收集的信息包括：
                
                • 设备信息：设备型号、操作系统版本、唯一设备标识符
                • 使用数据：应用使用情况、访问的网站记录、搜索历史
                • 本地存储：收藏的网站、用户设置、访问历史记录
                
                所有数据均存储在您的设备本地，我们不会将您的个人信息上传至服务器。
            """.trimIndent())
            
            SectionTitle("2. 信息使用")
            SectionContent("""
                我们使用收集的信息用于：
                
                • 提供和改进应用功能
                • 保存您的个人设置和偏好
                • 记录访问历史，方便您快速访问常用网站
                • 提供搜索和分类功能
                
                我们不会将您的信息用于任何商业目的或与第三方分享。
            """.trimIndent())
            
            SectionTitle("3. 数据存储")
            SectionContent("""
                本应用的所有数据均存储在您的设备本地：
                
                • 使用Android Room数据库存储网站信息、收藏记录等
                • 使用DataStore存储用户设置和偏好
                • 所有数据仅存在于您的设备上，不会上传至任何服务器
                
                您可以随时清除应用数据，删除所有本地存储的信息。
            """.trimIndent())
            
            SectionTitle("4. 权限说明")
            SectionContent("""
                本应用仅请求以下必要权限：
                
                • 网络访问：用于加载网页内容和网站图标
                
                您可以在系统设置中随时撤销这些权限。
            """.trimIndent())
            
            SectionTitle("5. 第三方服务")
            SectionContent("""
                本应用可能使用以下第三方服务：
                
                • 网站图标获取：通过公开的favicon服务获取网站图标
                • 网页内容加载：使用系统WebView组件加载网页
                
                当您访问外部网站时，请遵守该网站的隐私政策和使用条款。
            """.trimIndent())
            
            SectionTitle("6. 数据安全")
            SectionContent("""
                我们采取以下措施保护您的数据安全：
                
                • 所有数据仅存储在本地设备
                • 使用Android系统提供的安全存储机制
                • 不收集敏感个人信息
                • 不进行网络传输，降低数据泄露风险
            """.trimIndent())
            
            SectionTitle("7. 儿童隐私")
            SectionContent("""
                本应用不面向13岁以下的儿童。我们不会故意收集儿童的个人信息。
                如果您是儿童的家长或监护人，并发现您的孩子向我们提供了个人信息，
                请联系我们，我们将尽快删除此类信息。
            """.trimIndent())
            
            SectionTitle("8. 隐私政策更新")
            SectionContent("""
                我们可能会不定期更新本隐私政策。更新后的政策将在应用内公布，
                并在生效日期处标注最新日期。继续使用本应用即表示您接受更新后的政策。
            """.trimIndent())
            
            SectionTitle("9. 联系我们")
            SectionContent("""
                如果您对本隐私政策有任何疑问或建议，请通过以下方式联系我们：
                
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

