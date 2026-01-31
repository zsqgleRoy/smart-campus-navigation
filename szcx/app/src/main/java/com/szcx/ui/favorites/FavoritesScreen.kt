package com.szcx.ui.favorites

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
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.szcx.data.model.VisitRecord
import com.szcx.data.model.Website
import com.szcx.ui.ViewModelFactory
import com.szcx.ui.components.WebsiteCard
import com.szcx.ui.webview.WebViewActivity
import com.szcx.util.FormatUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory
) {
    val viewModel: FavoritesViewModel = viewModel(factory = viewModelFactory)
    val favoriteWebsites by viewModel.favoriteWebsites.collectAsState()
    val visitHistory by viewModel.visitHistory.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val context = LocalContext.current
    
    var selectedTab by remember { mutableStateOf(0) }
    var showDeleteDialog by remember { mutableStateOf<com.szcx.data.model.Website?>(null) }
    
    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .statusBarsPadding()
                    .background(MaterialTheme.colorScheme.background),
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("我的收藏") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("最近访问") }
                )
            }
        },
        bottomBar = {
            // Bottom navigation is handled by MainScreen
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
            when (selectedTab) {
            0 -> {
                if (favoriteWebsites.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = paddingValues.calculateTopPadding()),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("暂无收藏", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                        items(favoriteWebsites, key = { it.id }) { website ->
                            WebsiteCard(
                                website = website,
                                isFavorite = true,
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
            }
            1 -> {
                if (visitHistory.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = paddingValues.calculateTopPadding()),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("暂无访问记录", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                        items(visitHistory, key = { "${it.websiteId}_${it.visitTime}" }) { record ->
                            HistoryItem(
                                record = record,
                                onClick = {
                                    viewModel.openHistory(record)
                                    val intent = Intent(context, WebViewActivity::class.java).apply {
                                        putExtra("url", record.websiteUrl)
                                    }
                                    context.startActivity(intent)
                                },
                                onDelete = { viewModel.deleteHistory(record) }
                            )
                        }
                    }
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

@Composable
fun HistoryItem(
    record: VisitRecord,
    onClick: () -> Unit,
    onDelete: () -> Unit
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
            // 图标
            Card(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val iconUrl = remember(record.websiteUrl) { FormatUtils.getWebsiteIcon(record.websiteUrl) }
                    if (iconUrl.isNotEmpty()) {
                        SubcomposeAsyncImage(
                            model = iconUrl,
                            contentDescription = record.websiteName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            loading = {
                                Icon(
                                    Icons.Default.Public,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            error = {
                                Icon(
                                    Icons.Default.Public,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            success = { SubcomposeAsyncImageContent() }
                        )
                    } else {
                        Icon(
                            Icons.Default.Public,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.websiteName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = FormatUtils.formatTime(record.visitTime),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(
                onClick = { onDelete() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "删除",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

