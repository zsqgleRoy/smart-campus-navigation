package com.szcx.ui.search

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.ui.text.input.ImeAction
import com.szcx.util.FormatUtils
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.szcx.data.model.Website
import com.szcx.ui.ViewModelFactory
import com.szcx.ui.components.WebsiteCard
import com.szcx.ui.webview.WebViewActivity

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory
) {
    val viewModel: SearchViewModel = viewModel(factory = viewModelFactory)
    val searchKeyword by viewModel.searchKeyword.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()
    val context = LocalContext.current
    
    var showDeleteDialog by remember { mutableStateOf<com.szcx.data.model.Website?>(null) }
    var selectedProtocol by remember { mutableStateOf("https") }
    var showProtocolMenu by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    
    val isUrl = remember(searchKeyword) { FormatUtils.isUrlFormat(searchKeyword) }
    
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    
    LaunchedEffect(searchKeyword) {
        if (isUrl) {
            val protocol = FormatUtils.extractProtocol(searchKeyword)
            if (protocol != null) {
                selectedProtocol = protocol
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (isUrl) {
                            Box {
                                Card(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(40.dp)
                                        .clickable { showProtocolMenu = !showProtocolMenu },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = selectedProtocol,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                                DropdownMenu(
                                    expanded = showProtocolMenu,
                                    onDismissRequest = { showProtocolMenu = false },
                                    modifier = Modifier.width(80.dp)
                                ) {
                                    listOf("http", "https", "ftp").forEach { protocol ->
                                        DropdownMenuItem(
                                            text = { 
                                                Text(
                                                    protocol,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = if (protocol == selectedProtocol) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                                )
                                            },
                                            onClick = {
                                                selectedProtocol = protocol
                                                showProtocolMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        
                        BasicTextField(
                            value = searchKeyword,
                            onValueChange = { viewModel.setSearchKeyword(it) },
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester),
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (searchKeyword.isNotBlank()) {
                                        val trimmed = searchKeyword.trim()
                                        if (FormatUtils.isUrlFormat(trimmed)) {
                                            val url = FormatUtils.normalizeUrl(trimmed, selectedProtocol)
                                            context.startActivity(Intent(context, WebViewActivity::class.java).apply {
                                                putExtra("url", url)
                                            })
                                        } else {
                                            viewModel.performSearch(trimmed)
                                        }
                                    }
                                }
                            ),
                            decorationBox = { innerTextField ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        if (searchKeyword.isEmpty()) {
                                            Text(
                                                "搜索网站名称或输入网址",
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 15.sp
                                            )
                                        }
                                        innerTextField()
                                    }
                                    if (searchKeyword.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.setSearchKeyword("") }) {
                                            Icon(Icons.Default.Close, contentDescription = "清空", modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
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
        if (searchKeyword.isBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                if (searchHistory.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "搜索历史",
                            fontSize = 18.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        TextButton(onClick = { viewModel.clearSearchHistory() }) {
                            Text("清空", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    androidx.compose.foundation.layout.FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        searchHistory.take(10).forEach { keyword ->
                            Card(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(40.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable { viewModel.performSearch(keyword) }
                                        .padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = keyword,
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { viewModel.removeSearchHistoryItem(keyword) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "删除",
                                            modifier = Modifier.size(14.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Text(
                                "输入关键词搜索",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        } else if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("未找到相关网站", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(searchResults, key = { it.id }) { website ->
                    WebsiteCard(
                        website = website,
                        isFavorite = favorites.split(",").filter { it.isNotEmpty() }.contains(website.id),
                        onClick = {
                            viewModel.openWebsite(website)
                            val intent = Intent(context, WebViewActivity::class.java).apply {
                                putExtra("url", website.url)
                            }
                            context.startActivity(intent)
                        },
                        onFavoriteClick = { viewModel.toggleFavorite(website) },
                        onEditClick = {
                            navController.navigate("edit_website/${website.id}")
                        },
                        onDeleteClick = {
                            showDeleteDialog = website
                        }
                    )
                }
            }
        }
        
        showDeleteDialog?.let { website ->
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("删除网站") },
                text = { Text("确定要删除 ${website.name} 吗？此操作不可恢复。") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteWebsite(website)
                            showDeleteDialog = null
                        }
                    ) {
                        Text("删除", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("取消")
                    }
                }
            )
        }
    }
}

