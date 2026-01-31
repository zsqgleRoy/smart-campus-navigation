package com.szcx.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.szcx.ui.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory
) {
    val viewModel: SettingsViewModel = viewModel(factory = viewModelFactory)
    val theme by viewModel.theme.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        bottomBar = {
            // Bottom navigation is handled by MainScreen
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            // 主题模式
            ThemeSelectorItem(
                icon = Icons.Default.Palette,
                title = "切换主题",
                currentTheme = theme,
                onThemeChange = { viewModel.setTheme(it) }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 分类管理
            SettingsItem(
                icon = Icons.Default.Category,
                title = "分类管理",
                onClick = { navController.navigate("category_manage") }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 联系我们
            SettingsItem(
                icon = Icons.Default.ContactSupport,
                title = "联系我们",
                onClick = { navController.navigate("contact") }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 清理缓存
            SettingsItem(
                icon = Icons.Default.Delete,
                title = "清理缓存",
                onClick = { showClearDialog = true }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 关于
            SettingsItem(
                icon = Icons.Default.Info,
                title = "关于",
                onClick = { navController.navigate("about") }
            )
        }
        
        // 清理缓存对话框
        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = { Text("清理缓存") },
                text = { Text("确定要清理所有缓存数据吗？此操作不可恢复。") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // TODO: 实现清理缓存逻辑
                            showClearDialog = false
                        }
                    ) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            if (value != null) {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun ThemeSelectorItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    currentTheme: String,
    onThemeChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            // 三个主题图标按钮
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 浅色模式（太阳/亮度）
                IconButton(
                    onClick = { onThemeChange("light") },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.BrightnessHigh,
                        contentDescription = "浅色模式",
                        tint = if (currentTheme == "light") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // 深色模式（月亮/夜间）
                IconButton(
                    onClick = { onThemeChange("dark") },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Brightness2,
                        contentDescription = "深色模式",
                        tint = if (currentTheme == "dark") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // 护眼模式（眼睛）
                IconButton(
                    onClick = { onThemeChange("eye") },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = "护眼模式",
                        tint = if (currentTheme == "eye") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

