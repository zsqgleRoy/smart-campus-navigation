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
fun UsageRulesScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "数智成贤使用规范",
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
                text = "数智成贤使用规范",
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
            
            SectionTitle("一、总则")
            SectionContent("""
                1. 为规范数智成贤应用的使用行为，维护良好的使用环境，特制定本使用规范。
                
                2. 本规范适用于所有使用数智成贤应用的用户。
                
                3. 用户在使用本应用时，应当遵守国家法律法规、学校规章制度以及本规范。
            """.trimIndent())
            
            SectionTitle("二、使用原则")
            SectionContent("""
                1. 合法合规原则：用户应当合法、合规使用本应用，不得从事任何违法违规活动。
                
                2. 诚信原则：用户应当诚实守信，不得提供虚假信息或恶意操作。
                
                3. 尊重原则：用户应当尊重他人的合法权益，不得侵犯他人知识产权、隐私权等。
                
                4. 文明原则：用户应当文明使用，不得传播不良信息或进行不当行为。
            """.trimIndent())
            
            SectionTitle("三、禁止行为")
            SectionContent("""
                用户在使用本应用时，禁止以下行为：
                
                1. 添加或访问包含以下内容的网站：
                   • 违反国家法律法规的内容
                   • 色情、暴力、恐怖主义等不良信息
                   • 赌博、诈骗等违法活动
                   • 侵犯他人合法权益的内容
                
                2. 恶意操作：
                   • 恶意添加大量无效或重复网站
                   • 故意破坏应用功能
                   • 利用应用进行网络攻击
                
                3. 侵犯知识产权：
                   • 未经授权使用他人商标、标识
                   • 复制、传播受版权保护的内容
                
                4. 其他不当行为：
                   • 传播谣言或虚假信息
                   • 进行商业推广或广告
                   • 干扰其他用户正常使用
            """.trimIndent())
            
            SectionTitle("四、网站管理")
            SectionContent("""
                1. 用户添加的网站应当符合以下要求：
                   • 网站内容合法、健康、有益
                   • 网站可正常访问
                   • 网站信息真实准确
                
                2. 我们保留对用户添加的网站进行审核的权利，对于不符合规范的网站，
                   我们有权删除或要求用户修改。
                
                3. 用户应当定期检查自己添加的网站，确保其仍然符合使用规范。
            """.trimIndent())
            
            SectionTitle("五、数据管理")
            SectionContent("""
                1. 用户应当妥善保管自己的设备，防止数据丢失或泄露。
                
                2. 用户可以在应用设置中清除缓存、删除历史记录等。
                
                3. 我们不对用户因设备丢失、损坏等原因造成的数据丢失承担责任。
            """.trimIndent())
            
            SectionTitle("六、违规处理")
            SectionContent("""
                对于违反本规范的用户，我们有权采取以下措施：
                
                1. 警告：对轻微违规行为进行警告提醒
                
                2. 限制功能：限制部分功能的使用
                
                3. 删除内容：删除违规添加的网站或内容
                
                4. 暂停服务：暂停违规用户的使用权限
                
                5. 终止服务：终止严重违规用户的使用权限
                
                对于涉嫌违法犯罪的，我们将依法向有关部门举报。
            """.trimIndent())
            
            SectionTitle("七、用户责任")
            SectionContent("""
                1. 用户应当对自己使用本应用的行为负责。
                
                2. 用户应当对自己添加的网站内容负责，确保其合法合规。
                
                3. 用户因违反本规范或相关法律法规而产生的任何法律责任，
                   由用户自行承担，我们不承担任何责任。
                
                4. 用户应当保护好自己的账户和设备安全，防止被他人恶意使用。
            """.trimIndent())
            
            SectionTitle("八、免责声明")
            SectionContent("""
                1. 本应用仅作为导航工具，不对通过本应用访问的第三方网站内容负责。
                
                2. 用户访问外部网站时，应当自行判断网站内容的合法性和安全性。
                
                3. 我们不对因用户使用本应用而产生的任何直接或间接损失承担责任。
                
                4. 我们不对因不可抗力、系统故障等原因导致的服务中断承担责任。
            """.trimIndent())
            
            SectionTitle("九、规范更新")
            SectionContent("""
                1. 我们有权根据实际情况对本规范进行修改和完善。
                
                2. 修改后的规范将在应用内公布，并在生效日期处标注最新日期。
                
                3. 用户继续使用本应用即视为接受修改后的规范。
                
                4. 如用户不同意修改内容，应当停止使用本应用。
            """.trimIndent())
            
            SectionTitle("十、附则")
            SectionContent("""
                1. 本规范的解释权归数智成贤应用开发者所有。
                
                2. 本规范自发布之日起生效。
                
                3. 如本规范与国家法律法规或学校规章制度相冲突，以国家法律法规
                   和学校规章制度为准。
                
                4. 如有疑问，请通过应用内"设置-联系我们"页面联系我们的客服。
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

