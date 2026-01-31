package com.szcx.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.szcx.R
import kotlinx.coroutines.delay
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.szcx.SzcxApplication
import com.szcx.ui.about.AboutScreen
import com.szcx.ui.category.CategoryScreen
import com.szcx.ui.favorites.FavoritesScreen
import com.szcx.ui.home.HomeScreen
import com.szcx.ui.search.SearchScreen
import com.szcx.ui.settings.SettingsScreen
import com.szcx.ui.theme.SzcxTheme
import com.szcx.util.IconPreloader
import com.szcx.util.PreferencesManager
import com.szcx.util.VersionUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val application = application as SzcxApplication
        val database = application.database
        val preferencesManager = PreferencesManager(this)
        val viewModelFactory = ViewModelFactory(database, preferencesManager)
        
        setContent {
            var themeMode by remember { mutableStateOf("light") }
            var showSplash by remember { mutableStateOf(true) }
            var isAppReady by remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                val startTime = System.currentTimeMillis()
                
                // 并行加载所有必要资源
                launch {
                    themeMode = preferencesManager.theme.first()
                    preferencesManager.theme.collect { theme ->
                        themeMode = theme
                    }
                }
                
                // 等待应用加载完成
                launch {
                    // 确保数据库已初始化（通过查询来触发初始化）
                    try {
                        database.websiteDao().getAllWebsites().first()
                        database.categoryDao().getAllCategories().first()
                    } catch (e: Exception) {
                        // 如果数据库还未初始化，等待一下
                        delay(500)
                        database.websiteDao().getAllWebsites().first()
                        database.categoryDao().getAllCategories().first()
                    }
                    
                    // 确保主题已加载
                    preferencesManager.theme.first()
                    
                    // 计算实际加载时间
                    val loadTime = System.currentTimeMillis() - startTime
                    val minDisplayTime = 500L // 最小显示时间0.5秒
                    
                    // 如果加载时间小于最小显示时间，则等待剩余时间
                    if (loadTime < minDisplayTime) {
                        delay(minDisplayTime - loadTime)
                    }
                    
                    isAppReady = true
                    showSplash = false
                }
                
                // 在后台预加载所有网站图标（不阻塞启动）
                launch(Dispatchers.IO) {
                    try {
                        IconPreloader.preloadAllIcons(this@MainActivity, database)
                    } catch (e: Exception) {
                        // 预加载失败不影响应用使用
                    }
                }
            }
            
            SzcxTheme(themeMode = themeMode) {
                if (showSplash) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.mipmap.ic_launcher),
                                contentDescription = "应用Logo",
                                modifier = Modifier.size(120.dp),
                                contentScale = ContentScale.Fit
                            )
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Text(
                                text = "数智成贤",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.5.sp
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = "智慧校园，一键直达",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.2.sp
                            )
                            
                            Spacer(modifier = Modifier.height(48.dp))
                            
                            // 加载指示器
                            CircularProgressIndicator(
                                modifier = Modifier.size(36.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 3.dp
                            )
                            
                            Spacer(modifier = Modifier.height(64.dp))
                            
                            // 版本信息
                            Text(
                                text = VersionUtils.formattedVersion,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = "main"
                        ) {
                        composable("main") {
                            MainScreen(
                                navController = navController,
                                viewModelFactory = viewModelFactory
                            )
                        }
                        composable("home") {
                            MainScreen(
                                navController = navController,
                                viewModelFactory = viewModelFactory
                            )
                        }
                        composable("category") {
                            MainScreen(
                                navController = navController,
                                viewModelFactory = viewModelFactory
                            )
                        }
                        composable("favorites") {
                            MainScreen(
                                navController = navController,
                                viewModelFactory = viewModelFactory
                            )
                        }
                        composable("settings") {
                            MainScreen(
                                navController = navController,
                                viewModelFactory = viewModelFactory
                            )
                        }
                        composable(
                            route = "category/{categoryId}",
                            arguments = listOf(
                                navArgument("categoryId") { defaultValue = "all" }
                            )
                        ) { backStackEntry ->
                            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "all"
                            CategoryScreen(
                                navController = navController,
                                viewModelFactory = viewModelFactory,
                                initialCategoryId = categoryId
                            )
                        }
                        composable("search") {
                            SearchScreen(
                                navController = navController,
                                viewModelFactory = viewModelFactory
                            )
                        }
                        composable("about") {
                            AboutScreen(
                                navController = navController
                            )
                        }
                        composable("privacy_policy") {
                            com.szcx.ui.legal.PrivacyPolicyScreen(
                                navController = navController
                            )
                        }
                        composable("user_agreement") {
                            com.szcx.ui.legal.UserAgreementScreen(
                                navController = navController
                            )
                        }
                        composable("usage_rules") {
                            com.szcx.ui.legal.UsageRulesScreen(
                                navController = navController
                            )
                        }
                        composable("contact") {
                            com.szcx.ui.contact.ContactScreen(
                                navController = navController
                            )
                        }
                        composable("category_manage") {
                            com.szcx.ui.category.CategoryManageScreen(
                                navController = navController,
                                viewModelFactory = viewModelFactory
                            )
                        }
                        composable("add_website") {
                            com.szcx.ui.addwebsite.AddWebsiteScreen(
                                navController = navController,
                                viewModelFactory = viewModelFactory
                            )
                        }
                        composable(
                            route = "edit_website/{websiteId}",
                            arguments = listOf(
                                navArgument("websiteId") { }
                            )
                        ) { backStackEntry ->
                            val websiteId = backStackEntry.arguments?.getString("websiteId") ?: ""
                            com.szcx.ui.editwebsite.EditWebsiteScreen(
                                navController = navController,
                                viewModelFactory = viewModelFactory,
                                websiteId = websiteId
                            )
                        }
                    }
                    }
                }
            }
        }
    }
}

