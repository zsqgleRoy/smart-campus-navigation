package com.szcx.ui.category

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.szcx.data.model.Category
import com.szcx.data.model.Website
import com.szcx.ui.ViewModelFactory
import com.szcx.ui.components.WebsiteCard
import com.szcx.ui.webview.WebViewActivity
import com.szcx.util.FormatUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory,
    initialCategoryId: String? = null
) {
    val viewModel: CategoryViewModel = viewModel(factory = viewModelFactory)
    val categories by viewModel.categories.collectAsState()
    val websites by viewModel.websites.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val context = LocalContext.current
    
    var showDeleteDialog by remember { mutableStateOf<Website?>(null) }
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedWebsites by remember { mutableStateOf<Set<String>>(emptySet()) }
    
    LaunchedEffect(initialCategoryId) {
        if (initialCategoryId != null) {
            viewModel.selectCategory(initialCategoryId)
        }
    }
    
    Scaffold(
        topBar = {
            if (categories.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .background(MaterialTheme.colorScheme.background),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ScrollableTabRow(
                        selectedTabIndex = categories.indexOfFirst { it.id == selectedCategory }.takeIf { it >= 0 } ?: 0,
                        modifier = Modifier.weight(1f),
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        edgePadding = 0.dp
                    ) {
                        categories.forEachIndexed { index, category ->
                            Tab(
                                selected = category.id == selectedCategory,
                                onClick = { viewModel.selectCategory(category.id) },
                                text = { Text(category.name) }
                            )
                        }
                    }
                    // 选择/取消按钮和添加按钮
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        if (isSelectionMode) {
                            TextButton(onClick = {
                                isSelectionMode = false
                                selectedWebsites = emptySet()
                            }) {
                                Text("取消", fontSize = 12.sp)
                            }
                            TextButton(onClick = {
                                selectedWebsites = websites.map { it.id }.toSet()
                            }) {
                                Text("全选", fontSize = 12.sp)
                            }
                        } else {
                            TextButton(onClick = { isSelectionMode = true }) {
                                Text("选择", fontSize = 12.sp)
                            }
                        }
                        IconButton(onClick = { navController.navigate("add_website") }) {
                            Icon(Icons.Default.Add, contentDescription = "添加网站", modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Bottom navigation is handled by MainScreen
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        if (websites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                contentAlignment = Alignment.Center
            ) {
                Text("暂无网站", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 20.dp)
                ) {
                    items(websites, key = { it.id }) { website ->
                    val isSelected = selectedWebsites.contains(website.id)
                    WebsiteCard(
                        website = website,
                        isFavorite = favorites.split(",").filter { it.isNotEmpty() }.contains(website.id),
                        isSelected = if (isSelectionMode) isSelected else null,
                        onClick = {
                            if (isSelectionMode) {
                                selectedWebsites = if (isSelected) {
                                    selectedWebsites - website.id
                                } else {
                                    selectedWebsites + website.id
                                }
                            } else {
                                viewModel.openWebsite(website)
                                val intent = Intent(context, WebViewActivity::class.java).apply {
                                    putExtra("url", website.url)
                                }
                                context.startActivity(intent)
                            }
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


