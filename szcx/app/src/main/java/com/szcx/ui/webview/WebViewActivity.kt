package com.szcx.ui.webview

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import com.szcx.SzcxApplication
import com.szcx.data.repository.CategoryRepository
import com.szcx.data.repository.WebsiteRepository
import com.szcx.ui.theme.SzcxTheme
import com.szcx.util.FormatUtils
import com.szcx.util.PreferencesManager
import java.util.UUID

class WebViewActivity : ComponentActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val url = intent.getStringExtra("url") ?: ""
        val application = application as SzcxApplication
        val database = application.database
        val websiteRepository = WebsiteRepository(database)
        val categoryRepository = CategoryRepository(database)
        val preferencesManager = PreferencesManager(this)
        
        setContent {
            var themeMode by remember { mutableStateOf("light") }
            
            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    themeMode = preferencesManager.theme.first()
                    preferencesManager.theme.collect { theme ->
                        themeMode = theme
                    }
                }
            }
            
            SzcxTheme(themeMode = themeMode) {
                WebViewScreen(
                    url = url,
                    themeMode = themeMode,
                    websiteRepository = websiteRepository,
                    categoryRepository = categoryRepository,
                    preferencesManager = preferencesManager,
                    onBack = { finish() },
                    onSystemBack = { webView ->
                        // 系统返回：如果WebView可以返回，则返回上一页，否则退出
                        if (webView?.canGoBack() == true) {
                            webView.goBack()
                        } else {
                            finish()
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    url: String,
    themeMode: String,
    websiteRepository: WebsiteRepository,
    categoryRepository: CategoryRepository,
    preferencesManager: PreferencesManager,
    onBack: () -> Unit,
    onSystemBack: (WebView?) -> Unit
) {
    val context = LocalContext.current
    var webView by remember { mutableStateOf<WebView?>(null) }
    var pageTitle by remember { mutableStateOf("") }
    var websiteName by remember { mutableStateOf<String?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }
    val categories = categoryRepository.getAllCategories()
        .collectAsState(initial = emptyList())
    
    // 从数据库查询网站名
    LaunchedEffect(url) {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            val website = websiteRepository.getWebsiteByUrl(url)
            websiteName = website?.name
        }
    }
    
    // 获取第一个分类作为默认分类
    LaunchedEffect(categories.value) {
        if (categories.value.isNotEmpty() && selectedCategoryId == null) {
            selectedCategoryId = categories.value.first().id
        }
    }
    
    // 处理系统返回键：如果WebView可以返回，则返回上一页，否则退出
    BackHandler {
        onSystemBack(webView)
    }
    
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    title = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = pageTitle.ifEmpty { websiteName ?: "浏览网页" },
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = url,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    navigationIcon = {
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickable { onBack() },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "返回",
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "退出",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "更多")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("保存网站") },
                                leadingIcon = {
                                    Icon(Icons.Default.Save, contentDescription = null)
                                },
                                onClick = {
                                    showMenu = false
                                    showSaveDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("分享网站") },
                                leadingIcon = {
                                    Icon(Icons.Default.Share, contentDescription = null)
                                },
                                onClick = {
                                    showMenu = false
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("网站链接", url)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "链接已复制到剪贴板", Toast.LENGTH_SHORT).show()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("收藏网站") },
                                leadingIcon = {
                                    Icon(Icons.Default.Star, contentDescription = null)
                                },
                                onClick = {
                                    showMenu = false
                                    saveAndFavoriteWebsite(
                                        websiteRepository = websiteRepository,
                                        categoryRepository = categoryRepository,
                                        preferencesManager = preferencesManager,
                                        url = url,
                                        title = pageTitle,
                                        context = context,
                                        onSuccess = { showSuccessDialog = true },
                                        onError = { msg ->
                                            errorMessage = msg
                                            showErrorDialog = true
                                        }
                                    )
                                }
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            // 允许所有URL加载，不拦截
                            return false
                        }
                        
                        @Suppress("DEPRECATION")
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            // 兼容旧版本
                            return false
                        }
                        
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            // 获取页面标题
                            view?.evaluateJavascript("document.title") { title ->
                                val titleText = title.removeSurrounding("\"").ifEmpty { null }
                                android.os.Handler(android.os.Looper.getMainLooper()).post {
                                    pageTitle = titleText ?: ""
                                }
                            }
                            
                            // 根据主题模式注入CSS
                            applyThemeToWebView(view, themeMode)
                        }
                        
                        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: android.webkit.WebResourceError?) {
                            super.onReceivedError(view, request, error)
                            // 即使出错也尝试加载
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                android.util.Log.e("WebView", "Error loading page: ${error?.description}")
                            }
                        }
                    }
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    // 启用缩放功能
                    settings.setSupportZoom(true)
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false // 隐藏缩放控件，使用手势缩放
                    // 启用文本缩放
                    settings.textZoom = 100
                    // 启用多窗口支持
                    settings.javaScriptCanOpenWindowsAutomatically = true
                    // 允许混合内容（HTTP和HTTPS）
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }
                    // 允许文件访问
                    settings.allowFileAccess = true
                    settings.allowContentAccess = true
                    // 设置User-Agent
                    settings.userAgentString = settings.userAgentString + " SzcxApp"
                    
                    // 设置夜间模式（Android 10+）
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        settings.forceDark = when (themeMode) {
                            "dark" -> android.webkit.WebSettings.FORCE_DARK_ON
                            else -> android.webkit.WebSettings.FORCE_DARK_OFF
                        }
                    }
                    
                    loadUrl(url)
                    webView = this
                }
            },
            update = { view ->
                // 当主题变化时，重新应用主题
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    view.settings.forceDark = when (themeMode) {
                        "dark" -> android.webkit.WebSettings.FORCE_DARK_ON
                        else -> android.webkit.WebSettings.FORCE_DARK_OFF
                    }
                }
                applyThemeToWebView(view, themeMode)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
        
        // 保存网站对话框
        if (showSaveDialog) {
            var websiteName by remember { mutableStateOf(pageTitle.ifEmpty { "" }) }
            var websiteDescription by remember { mutableStateOf("") }
            var showCategoryMenu by remember { mutableStateOf(false) }
            
            AlertDialog(
                onDismissRequest = { showSaveDialog = false },
                title = { Text("保存网站") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = websiteName,
                            onValueChange = { websiteName = it },
                            label = { Text("网站名称 *") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = websiteDescription,
                            onValueChange = { websiteDescription = it },
                            label = { Text("网站描述") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                        ExposedDropdownMenuBox(
                            expanded = showCategoryMenu,
                            onExpandedChange = { showCategoryMenu = !showCategoryMenu }
                        ) {
                            OutlinedTextField(
                                value = selectedCategoryId?.let { id ->
                                    categories.value.find { it.id == id }?.name ?: ""
                                } ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("分类 *") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = showCategoryMenu,
                                onDismissRequest = { showCategoryMenu = false }
                            ) {
                                categories.value.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category.name) },
                                        onClick = {
                                            selectedCategoryId = category.id
                                            showCategoryMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (websiteName.isBlank() || selectedCategoryId == null) {
                                errorMessage = "请填写网站名称并选择分类"
                                showErrorDialog = true
                                showSaveDialog = false
                            } else {
                                saveWebsite(
                                    websiteRepository = websiteRepository,
                                    url = url,
                                    name = websiteName,
                                    description = websiteDescription,
                                    categoryId = selectedCategoryId!!,
                                    context = context,
                                    onSuccess = {
                                        showSaveDialog = false
                                        showSuccessDialog = true
                                    },
                                    onError = { msg ->
                                        errorMessage = msg
                                        showSaveDialog = false
                                        showErrorDialog = true
                                    }
                                )
                            }
                        }
                    ) {
                        Text("保存")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSaveDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }
        
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("成功") },
                text = { Text("网站已保存！") },
                confirmButton = {
                    TextButton(onClick = { showSuccessDialog = false }) {
                        Text("确定")
                    }
                }
            )
        }
        
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text("错误") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(onClick = { showErrorDialog = false }) {
                        Text("确定")
                    }
                }
            )
        }
    }
}

private fun saveWebsite(
    websiteRepository: WebsiteRepository,
    url: String,
    name: String,
    description: String,
    categoryId: String,
    context: Context,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
        try {
            val id = UUID.randomUUID().toString()
            val iconUrl = FormatUtils.getWebsiteIcon(url)
            val website = com.szcx.data.model.Website(
                id = id,
                name = name.trim(),
                url = url.trim(),
                iconUrl = iconUrl,
                category = categoryId,
                description = description.trim(),
                isHot = false,
                isFavorite = false,
                accessCount = 0,
                lastAccess = 0
            )
            websiteRepository.insertWebsite(website)
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                onSuccess()
            }
        } catch (e: Exception) {
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                onError("保存失败：${e.message}")
            }
        }
    }
}

private fun saveAndFavoriteWebsite(
    websiteRepository: WebsiteRepository,
    categoryRepository: CategoryRepository,
    preferencesManager: PreferencesManager,
    url: String,
    title: String,
    context: Context,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
        try {
            // 获取第一个分类作为默认分类
            val categoriesFlow = categoryRepository.getAllCategories()
            var defaultCategoryId = "all"
            categoriesFlow.take(1).collect { catList ->
                if (catList.isNotEmpty()) {
                    defaultCategoryId = catList.first().id
                }
            }
            
            // 生成网站名称
            val websiteName = if (title.isNotBlank()) {
                title
            } else {
                // 生成递增的网站名称
                var count = websiteRepository.getWebsiteCountByNamePrefix("新建网站")
                var name = "新建网站${count + 1}"
                // 确保名称唯一
                while (true) {
                    val existing = websiteRepository.getWebsiteCountByNamePrefix(name)
                    if (existing == 0) break
                    count++
                    name = "新建网站${count + 1}"
                }
                name
            }
            
            val id = UUID.randomUUID().toString()
            val iconUrl = FormatUtils.getWebsiteIcon(url)
            val website = com.szcx.data.model.Website(
                id = id,
                name = websiteName,
                url = url.trim(),
                iconUrl = iconUrl,
                category = defaultCategoryId,
                description = "",
                isHot = false,
                isFavorite = true,
                accessCount = 0,
                lastAccess = 0
            )
            websiteRepository.insertWebsite(website)
            
            // 添加到收藏列表
            val favString = preferencesManager.favorites.first()
            val currentFavorites = favString.split(",").filter { it.isNotEmpty() }.toMutableList()
            if (!currentFavorites.contains(id)) {
                currentFavorites.add(id)
                preferencesManager.setFavorites(currentFavorites.joinToString(","))
            }
            
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                onSuccess()
            }
        } catch (e: Exception) {
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                onError("收藏失败：${e.message}")
            }
        }
    }
}

/**
 * 根据主题模式向 WebView 注入 CSS，使网页内容随主题变化
 */
private fun applyThemeToWebView(webView: WebView?, themeMode: String) {
    webView?.evaluateJavascript("""
        (function() {
            // 移除之前注入的样式
            var existingStyle = document.getElementById('szcx-theme-style');
            if (existingStyle) {
                existingStyle.remove();
            }
            
            var css = '';
            
            if ('$themeMode' === 'dark') {
                // 深色模式：优先使用系统夜间模式，配合 CSS 调整
                // Android 10+ 会使用 forceDark，这里作为补充
                css = `
                    html {
                        background-color: #121212 !important;
                    }
                    body {
                        background-color: #121212 !important;
                        color: #E0E0E0 !important;
                    }
                    /* 对于不支持夜间模式的元素，手动调整 */
                    input, textarea, select {
                        background-color: #1E1E1E !important;
                        color: #E0E0E0 !important;
                        border-color: #424242 !important;
                    }
                    a {
                        color: #90CAF9 !important;
                    }
                `;
            } else if ('$themeMode' === 'eye') {
                // 护眼模式：使用护眼色调
                css = `
                    html, body {
                        background-color: #F5F0E8 !important;
                        color: #5C4A3A !important;
                    }
                    * {
                        background-color: inherit !important;
                    }
                    a {
                        color: #8B7355 !important;
                    }
                    input, textarea, select {
                        background-color: #FFF8F0 !important;
                        color: #5C4A3A !important;
                        border-color: #A68B6B !important;
                    }
                `;
            } else {
                // 浅色模式：保持原样，只调整背景色
                css = `
                    html, body {
                        background-color: #F5F5F5 !important;
                    }
                `;
            }
            
            var style = document.createElement('style');
            style.id = 'szcx-theme-style';
            style.type = 'text/css';
            style.innerHTML = css;
            document.head.appendChild(style);
        })();
    """.trimIndent(), null)
}

