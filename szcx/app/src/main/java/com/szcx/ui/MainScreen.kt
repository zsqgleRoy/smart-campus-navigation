package com.szcx.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlin.math.abs
import com.szcx.ui.category.CategoryScreen
import com.szcx.ui.favorites.FavoritesScreen
import com.szcx.ui.home.BottomNavigationBar
import com.szcx.ui.home.HomeScreen
import com.szcx.ui.settings.SettingsScreen
import kotlinx.coroutines.launch

// 主页面路由常量
private const val PAGE_COUNT = 4
private val MAIN_ROUTES = listOf("home", "category", "favorites", "settings")

// 判断是否为深层页面（不在主页面中）
private fun isDeepPage(route: String?): Boolean {
    if (route == null) return false
    return route.startsWith("category/") ||
            route.startsWith("search") ||
            route.startsWith("about") ||
            route.startsWith("privacy") ||
            route.startsWith("user") ||
            route.startsWith("usage") ||
            route.startsWith("contact") ||
            route.startsWith("add_website") ||
            route.startsWith("edit_website") ||
            route.startsWith("category_manage")
}

// 将路由转换为页面索引
private fun routeToPageIndex(route: String?): Int? {
    return when {
        route == null -> null
        route == "home" || route == "main" -> 0
        route.startsWith("category") -> 1
        route == "favorites" -> 2
        route == "settings" -> 3
        else -> null
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory
) {
    val pagerState = rememberPagerState(initialPage = 0) { PAGE_COUNT }
    val coroutineScope = rememberCoroutineScope()
    
    // 用于防止循环导航的标志
    var isNavigatingFromPager by remember { mutableStateOf(false) }
    var isNavigatingFromNav by remember { mutableStateOf(false) }
    var isSwitchingPage by remember { mutableStateOf(false) }
    var lastSwitchTime by remember { mutableStateOf(0L) }
    var isUserClick by remember { mutableStateOf(false) } // 标记是否是用户点击触发的切换
    
    // 获取当前路由
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val context = LocalContext.current
    
    // 判断是否为最顶级路由（主页面路由）
    val isTopLevelRoute = currentRoute in MAIN_ROUTES || currentRoute == "main"
    
    // 处理系统返回键：在最顶级路由时退出应用，否则返回上一页
    BackHandler(enabled = isTopLevelRoute) {
        // 在最顶级路由时，退出应用
        (context as? android.app.Activity)?.finish()
    }
    
    // 监听页面变化，同步导航（仅在用户滑动或点击底部导航时触发）
    LaunchedEffect(pagerState.currentPage) {
        // 如果正在从导航同步页面，跳过
        if (isNavigatingFromNav) return@LaunchedEffect
        
        val targetRoute = MAIN_ROUTES[pagerState.currentPage]
        
        // 只在需要时才导航，避免重复导航
        if (targetRoute != currentRoute && !isDeepPage(currentRoute)) {
            isNavigatingFromPager = true
            try {
                // 第一次导航尝试
                try {
                    navController.navigate(targetRoute) {
                        // 弹出到主页面，但保持状态
                        popUpTo("main") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                    // 等待并验证导航是否成功
                    kotlinx.coroutines.delay(150)
                    var newRoute = navController.currentBackStackEntry?.destination?.route
                    // 验证导航是否成功（考虑 category 路由的特殊情况）
                    val isNavigationSuccess = when {
                        targetRoute == "category" -> newRoute?.startsWith("category") == true
                        else -> newRoute == targetRoute
                    }
                    if (!isNavigationSuccess) {
                        // 如果导航失败，重试一次
                        kotlinx.coroutines.delay(50)
                        navController.navigate(targetRoute) {
                            popUpTo("main") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        kotlinx.coroutines.delay(150)
                    }
                } catch (e: Exception) {
                    // 如果导航失败，重试一次
                    try {
                        kotlinx.coroutines.delay(50)
                        navController.navigate(targetRoute) {
                            popUpTo("main") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        kotlinx.coroutines.delay(100)
                    } catch (e2: Exception) {
                        // 忽略错误，避免崩溃
                    }
                }
            } finally {
                // 延迟重置标志，确保导航完成
                kotlinx.coroutines.delay(150)
                isNavigatingFromPager = false
                // 重置用户点击标志
                isUserClick = false
            }
        }
    }
    
    // 监听导航变化，同步页面（仅在导航变化时触发，不响应页面滑动）
    LaunchedEffect(currentRoute) {
        // 如果正在从页面同步导航，跳过
        if (isNavigatingFromPager) return@LaunchedEffect
        
        val targetPage = routeToPageIndex(currentRoute)
        
        // 如果是用户点击触发的切换，已经在 onPageChange 中处理，这里跳过
        if (isUserClick) return@LaunchedEffect
        
        if (targetPage != null && targetPage != pagerState.currentPage && !isSwitchingPage) {
            isNavigatingFromNav = true
            isSwitchingPage = true
            try {
                coroutineScope.launch {
                    // 路由变化触发的切换：使用动画，允许显示中间页面（滑动效果）
                    try {
                        pagerState.animateScrollToPage(targetPage)
                        // 等待动画完成
                        kotlinx.coroutines.delay(300)
                    } catch (e: Exception) {
                        // 如果动画失败，使用直接跳转作为后备
                        try {
                            pagerState.scrollToPage(targetPage)
                            kotlinx.coroutines.delay(100)
                        } catch (e2: Exception) {
                            // 忽略错误，避免崩溃
                        }
                    }
                }
            } finally {
                // 延迟重置标志
                kotlinx.coroutines.delay(100)
                isNavigatingFromNav = false
                isSwitchingPage = false
            }
        } else if (currentRoute != null && isDeepPage(currentRoute)) {
            // 处理深层页面返回：如果当前在深层页面，且路由对应某个主页面
            // 不立即切换，等待用户返回
            // 这里可以添加返回逻辑，但通常由NavController的返回栈处理
        }
    }
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentPage = pagerState.currentPage,
                onPageChange = { page ->
                    // 用户点击底部导航栏：直接切换，不经过中间页面
                    // 如果已经在目标页面，直接返回
                    if (page == pagerState.currentPage) return@BottomNavigationBar
                    
                    // 防抖：如果距离上次切换时间太短，忽略此次点击
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastSwitchTime < 300) {
                        return@BottomNavigationBar
                    }
                    lastSwitchTime = currentTime
                    
                    // 如果正在切换页面，忽略此次点击
                    if (isSwitchingPage) return@BottomNavigationBar
                    
                    // 标记这是用户点击触发的切换
                    isUserClick = true
                    isSwitchingPage = true
                    coroutineScope.launch {
                        try {
                            // 点击切换：总是使用 scrollToPage 直接跳转，不经过中间页面
                            var success = false
                            
                            // 第一次尝试
                            try {
                                pagerState.scrollToPage(page)
                                // 等待并验证页面真正切换完成
                                var retryCount = 0
                                while (!success && retryCount < 30) {
                                    kotlinx.coroutines.delay(50)
                                    if (pagerState.currentPage == page) {
                                        // 再次验证，确保状态稳定
                                        kotlinx.coroutines.delay(50)
                                        if (pagerState.currentPage == page) {
                                            success = true
                                        }
                                    }
                                    retryCount++
                                }
                            } catch (e: Exception) {
                                // 第一次尝试失败，继续
                            }
                            
                            // 如果第一次失败，重试
                            if (!success) {
                                try {
                                    kotlinx.coroutines.delay(100)
                                    pagerState.scrollToPage(page)
                                    var retryCount = 0
                                    while (!success && retryCount < 30) {
                                        kotlinx.coroutines.delay(50)
                                        if (pagerState.currentPage == page) {
                                            kotlinx.coroutines.delay(50)
                                            if (pagerState.currentPage == page) {
                                                success = true
                                            }
                                        }
                                        retryCount++
                                    }
                                } catch (e: Exception) {
                                    // 第二次尝试失败，继续
                                }
                            }
                            
                            // 如果所有尝试都失败，使用动画作为最后的后备方案
                            if (!success) {
                                try {
                                    pagerState.animateScrollToPage(page)
                                    kotlinx.coroutines.delay(400)
                                    // 再次验证
                                    if (pagerState.currentPage != page) {
                                        // 最后一次尝试：直接跳转
                                        pagerState.scrollToPage(page)
                                        kotlinx.coroutines.delay(100)
                                    }
                                } catch (e: Exception) {
                                    // 忽略错误，避免崩溃
                                }
                            }
                        } finally {
                            // 延迟重置标志，确保所有操作完成
                            kotlinx.coroutines.delay(200)
                            isSwitchingPage = false
                        }
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()),
            // 允许用户滑动切换页面，但会通过LaunchedEffect同步导航
            userScrollEnabled = true
        ) { page ->
            // HorizontalPager默认保持页面状态，无需额外配置
            when (page) {
                0 -> HomeScreen(
                    navController = navController,
                    viewModelFactory = viewModelFactory
                )
                1 -> CategoryScreen(
                    navController = navController,
                    viewModelFactory = viewModelFactory
                )
                2 -> FavoritesScreen(
                    navController = navController,
                    viewModelFactory = viewModelFactory
                )
                3 -> SettingsScreen(
                    navController = navController,
                    viewModelFactory = viewModelFactory
                )
                else -> {
                    // 不应该到达这里，但提供默认页面
                    HomeScreen(
                        navController = navController,
                        viewModelFactory = viewModelFactory
                    )
                }
            }
        }
    }
}
