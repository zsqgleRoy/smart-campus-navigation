package com.szcx.ui.editwebsite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.szcx.data.model.Website
import com.szcx.ui.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWebsiteScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory,
    websiteId: String
) {
    val viewModel: EditWebsiteViewModel = viewModel(factory = viewModelFactory)
    val categories by viewModel.categories.collectAsState()
    val website by viewModel.website.collectAsState()
    
    var websiteName by remember { mutableStateOf("") }
    var websiteUrl by remember { mutableStateOf("") }
    var websiteIconUrl by remember { mutableStateOf("") }
    var websiteDescription by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    // 加载网站数据
    LaunchedEffect(websiteId) {
        viewModel.loadWebsite(websiteId)
    }
    
    // 当网站数据加载后，填充表单
    LaunchedEffect(website) {
        website?.let {
            websiteName = it.name
            websiteUrl = it.url
            websiteIconUrl = it.iconUrl
            websiteDescription = it.description
            selectedCategory = it.category
        }
    }
    
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
                    text = "编辑网站",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }
        }
    ) { paddingValues ->
        if (website == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 网站名称
                OutlinedTextField(
                    value = websiteName,
                    onValueChange = { websiteName = it },
                    label = { Text("网站名称 *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) }
                )
                
                // 网站URL
                OutlinedTextField(
                    value = websiteUrl,
                    onValueChange = { websiteUrl = it },
                    label = { Text("网站地址 *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                    placeholder = { Text("https://example.com") }
                )
                
                // 图标URL（可选）
                OutlinedTextField(
                    value = websiteIconUrl,
                    onValueChange = { websiteIconUrl = it },
                    label = { Text("图标地址（可选，留空则自动获取）") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Image, contentDescription = null) },
                    placeholder = { Text("留空则从网站自动获取图标") },
                    trailingIcon = {
                        if (websiteIconUrl.isNotEmpty()) {
                            IconButton(onClick = { websiteIconUrl = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "清空")
                            }
                        }
                    }
                )
                
                // 网站描述
                OutlinedTextField(
                    value = websiteDescription,
                    onValueChange = { websiteDescription = it },
                    label = { Text("网站描述") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 3,
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) }
                )
                
                // 分类选择
                OutlinedTextField(
                    value = selectedCategory?.let { 
                        categories.find { c -> c.id == it }?.name ?: "" 
                    } ?: "",
                    onValueChange = {},
                    label = { Text("分类 *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCategoryDialog = true },
                    enabled = false,
                    leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    placeholder = { Text("请选择分类") }
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // 保存按钮
                Button(
                    onClick = {
                        if (websiteName.isBlank() || websiteUrl.isBlank() || selectedCategory == null) {
                            errorMessage = "请填写所有必填项"
                            showErrorDialog = true
                        } else if (!websiteUrl.startsWith("http://") && !websiteUrl.startsWith("https://")) {
                            errorMessage = "网站地址必须以 http:// 或 https:// 开头"
                            showErrorDialog = true
                        } else {
                            website?.let {
                                viewModel.updateWebsite(
                                    website = it.copy(
                                        name = websiteName,
                                        url = websiteUrl,
                                        iconUrl = websiteIconUrl,
                                        description = websiteDescription,
                                        category = selectedCategory!!
                                    )
                                ) { success, message ->
                                    if (success) {
                                        showSuccessDialog = true
                                    } else {
                                        errorMessage = message
                                        showErrorDialog = true
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("保存修改", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
            
            // 分类选择对话框
            if (showCategoryDialog) {
                AlertDialog(
                    onDismissRequest = { showCategoryDialog = false },
                    title = { Text("选择分类") },
                    text = {
                        Column {
                            categories.forEach { category ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedCategory = category.id
                                            showCategoryDialog = false
                                        }
                                        .padding(vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedCategory == category.id,
                                        onClick = {
                                            selectedCategory = category.id
                                            showCategoryDialog = false
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(category.name, fontSize = 16.sp)
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showCategoryDialog = false }) {
                            Text("取消")
                        }
                    }
                )
            }
            
            // 成功对话框
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    },
                    title = { Text("修改成功") },
                    text = { Text("网站信息已成功更新！") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showSuccessDialog = false
                                navController.popBackStack()
                            }
                        ) {
                            Text("确定")
                        }
                    }
                )
            }
            
            // 错误对话框
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text("修改失败") },
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
}

