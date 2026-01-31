package com.szcx.ui.home

import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.szcx.data.model.Category
import com.szcx.data.model.Website
import com.szcx.ui.ViewModelFactory
import com.szcx.ui.webview.WebViewActivity
import com.szcx.util.FormatUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory
) {
    val viewModel: HomeViewModel = viewModel(factory = viewModelFactory)
    val hotWebsites by viewModel.hotWebsites.collectAsState()
    val favoriteWebsites by viewModel.favoriteWebsites.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val context = LocalContext.current
    
    Scaffold(
        containerColor = ComposeColor.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clickable { navController.navigate("search") }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(28.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "搜索网站名称或输入网址",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 15.sp
                        )
                    }
                }
                IconButton(
                    onClick = { navController.navigate("add_website") },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "添加网站",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                if (hotWebsites.isNotEmpty()) {
                    SectionTitle("常用网站")
                    val rowCount = remember(hotWebsites.size) { (hotWebsites.size + 3) / 4 }
                    Column(
                        modifier = Modifier.height(
                            (rowCount * 94 + (rowCount - 1).coerceAtLeast(0) * 16 + 8).dp
                        )
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 8.dp),
                            modifier = Modifier.fillMaxSize(),
                            userScrollEnabled = false
                        ) {
                            items(hotWebsites, key = { it.id }) { website ->
                                HotWebsiteItem(
                                    website = website,
                                    isFavorite = favorites.split(",").filter { it.isNotEmpty() }.contains(website.id),
                                    onClick = {
                                        viewModel.openWebsite(website)
                                        openWebsite(context, website.url)
                                    },
                                    onFavoriteClick = { viewModel.toggleFavorite(website) }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                SectionTitle("收藏网站")
                if (favoriteWebsites.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                        userScrollEnabled = true
                    ) {
                        items(favoriteWebsites, key = { it.id }) { website ->
                            FavoriteWebsiteItem(
                                website = website,
                                onClick = {
                                    viewModel.openWebsite(website)
                                    openWebsite(context, website.url)
                                },
                                onFavoriteClick = { viewModel.toggleFavorite(website) }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "暂无收藏网站，点击网站上的⭐图标即可收藏",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                
                SectionTitle("网站分类")
                val categoryRowCount = remember(categories.size) { (categories.size + 3) / 4 }
                Column(
                    modifier = Modifier.height(
                        (categoryRowCount * 94 + (categoryRowCount - 1).coerceAtLeast(0) * 16).dp
                    )
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize(),
                        userScrollEnabled = false
                    ) {
                        items(categories, key = { it.id }) { category ->
                            CategoryItem(
                                category = category,
                                onClick = {
                                    navController.navigate("category/${category.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(vertical = 20.dp, horizontal = 4.dp)
    )
}

@Composable
fun HotWebsiteItem(
    website: Website,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Card(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val iconUrl = website.iconUrl.ifEmpty { FormatUtils.getWebsiteIcon(website.url) }
                    if (iconUrl.isNotEmpty()) {
                        SubcomposeAsyncImage(
                            model = iconUrl,
                            contentDescription = website.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop,
                            loading = {
                                Icon(
                                    Icons.Default.Public,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            error = {
                                Icon(
                                    Icons.Default.Public,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            success = { SubcomposeAsyncImageContent() }
                        )
                    } else {
                        Icon(
                            Icons.Default.Public,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            IconButton(
                onClick = { onFavoriteClick() },
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = 48.dp, y = (-6).dp)
            ) {
                Icon(
                    if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "收藏",
                    tint = if (isFavorite) ComposeColor(0xFFFFD21E) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = website.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(64.dp)
        )
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = ComposeColor(android.graphics.Color.parseColor(category.color + "15"))
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    getCategoryIcon(category.iconRes),
                    contentDescription = null,
                    tint = ComposeColor(android.graphics.Color.parseColor(category.color)),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = category.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentPage: Int,
    onPageChange: (Int) -> Unit
) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "home"
    val isCategorySelected = currentRoute.startsWith("category")
    
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "首页") },
            label = { Text("首页") },
            selected = currentPage == 0,
            onClick = { onPageChange(0) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Apps, contentDescription = "分类") },
            label = { Text("分类") },
            selected = currentPage == 1,
            onClick = { onPageChange(1) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "收藏") },
            label = { Text("收藏") },
            selected = currentPage == 2,
            onClick = { onPageChange(2) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "设置") },
            label = { Text("设置") },
            selected = currentPage == 3,
            onClick = { onPageChange(3) }
        )
    }
}

fun openWebsite(context: android.content.Context, url: String) {
    val intent = Intent(context, WebViewActivity::class.java).apply {
        putExtra("url", url)
    }
    context.startActivity(intent)
}

@Composable
fun FavoriteWebsiteItem(
    website: Website,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Card(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val iconUrl = website.iconUrl.ifEmpty { FormatUtils.getWebsiteIcon(website.url) }
                    if (iconUrl.isNotEmpty()) {
                        SubcomposeAsyncImage(
                            model = iconUrl,
                            contentDescription = website.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop,
                            loading = {
                                Icon(
                                    Icons.Default.Public,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            error = {
                                Icon(
                                    Icons.Default.Public,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            success = { SubcomposeAsyncImageContent() }
                        )
                    } else {
                        Icon(
                            Icons.Default.Public,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            IconButton(
                onClick = { onFavoriteClick() },
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = 48.dp, y = (-6).dp)
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "收藏",
                    tint = ComposeColor(0xFFFFD21E),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = website.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(64.dp)
        )
    }
}

fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName) {
        "apps" -> Icons.Default.Apps
        "description" -> Icons.Default.Description
        "orders" -> Icons.Default.List
        "home" -> Icons.Default.Home
        "service" -> Icons.Default.Build
        "newspaper" -> Icons.Default.Article
        "setting" -> Icons.Default.Settings
        else -> Icons.Default.Apps
    }
}

