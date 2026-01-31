package com.szcx.ui.contact

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(navController: NavController) {
    var wechatExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                        }
                    }
                )
                Text(
                    text = "联系我们",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 官方微信
            ContactItem(
                iconAssetName = "vx.png",
                title = "官方微信",
                onClick = { wechatExpanded = !wechatExpanded },
                isExpanded = wechatExpanded,
                onExpandChange = { wechatExpanded = it }
            ) {
                if (wechatExpanded) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "扫描二维码关注我们",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            val qrBitmap = remember {
                                try {
                                    val inputStream = context.assets.open("vxqr.jpg")
                                    BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                                } catch (e: Exception) {
                                    null
                                }
                            }
                            if (qrBitmap != null) {
                                Image(
                                    bitmap = qrBitmap,
                                    contentDescription = "微信二维码",
                                    modifier = Modifier.size(200.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                }
            }
            
            ContactItem(
                iconAssetName = "qq.png",
                title = "官方QQ群",
                onClick = {
                    val url = "https://qm.qq.com/cgi-bin/qm/qr?k=gdtpVS8dFpZx0O5gxGZ4OWdychCiKGcR&jump_from=webapi&authKey=WuBSTclLCk8ADFUOkM9BdMDo+X1ewmoIsqQkO+XCFgSuglQ9EIa/vYYQMErmnsDX"
                    val intent = Intent(context, com.szcx.ui.webview.WebViewActivity::class.java).apply {
                        putExtra("url", url)
                    }
                    context.startActivity(intent)
                },
                isExpanded = false,
                onExpandChange = {}
            )
        }
    }
}

@Composable
fun ContactItem(
    iconAssetName: String,
    title: String,
    onClick: () -> Unit,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    expandedContent: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300)
    )
    
    val iconBitmap = remember(iconAssetName) {
        try {
            val inputStream = context.assets.open(iconAssetName)
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (iconBitmap != null) {
                    Image(
                        bitmap = iconBitmap,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Icon(
                        Icons.Default.ContactSupport,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                if (expandedContent != {}) {
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(rotationAngle),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            expandedContent()
        }
    }
}

