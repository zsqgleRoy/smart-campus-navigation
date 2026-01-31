package com.szcx.ui.category

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.szcx.data.model.Category
import com.szcx.ui.ViewModelFactory
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManageScreen(
    navController: NavController,
    viewModelFactory: ViewModelFactory
) {
    val viewModel: CategoryManageViewModel = viewModel(factory = viewModelFactory)
    val categories by viewModel.categories.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<Category?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Category?>(null) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("分类管理") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "添加分类")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories.filter { it.id != "all" }, key = { it.id }) { category ->
                CategoryManageItem(
                    category = category,
                    onEditClick = { showEditDialog = category },
                    onDeleteClick = { showDeleteDialog = category }
                )
            }
        }
        
        if (showAddDialog) {
            AddCategoryDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, iconRes, color ->
                    viewModel.addCategory(name, iconRes, color)
                    showAddDialog = false
                }
            )
        }
        
        showEditDialog?.let { category ->
            EditCategoryDialog(
                category = category,
                onDismiss = { showEditDialog = null },
                onConfirm = { name, iconRes, color ->
                    viewModel.updateCategory(category.copy(name = name, iconRes = iconRes, color = color))
                    showEditDialog = null
                }
            )
        }
        
        showDeleteDialog?.let { category ->
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("删除分类") },
                text = { Text("确定要删除分类\"${category.name}\"吗？此操作不可恢复。") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteCategory(category)
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
fun CategoryManageItem(
    category: Category,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(android.graphics.Color.parseColor(category.color)),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category.name.take(1),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(
                        text = category.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "ID: ${category.id}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "编辑")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "删除", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var iconRes by remember { mutableStateOf("apps") }
    var color by remember { mutableStateOf("#1989fa") }
    
    val colors = listOf(
        "#1989fa", "#07c160", "#ff976a", "#ee0a24",
        "#ff976a", "#1989fa", "#07c160"
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加分类") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("分类名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = iconRes,
                    onValueChange = { iconRes = it },
                    label = { Text("图标资源") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Text("选择颜色：", fontSize = 14.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colors.forEach { c ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color(android.graphics.Color.parseColor(c)),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { color = c },
                            contentAlignment = Alignment.Center
                        ) {
                            if (color == c) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name, iconRes, color)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
fun EditCategoryDialog(
    category: Category,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(category.name) }
    var iconRes by remember { mutableStateOf(category.iconRes) }
    var color by remember { mutableStateOf(category.color) }
    
    val colors = listOf(
        "#1989fa", "#07c160", "#ff976a", "#ee0a24",
        "#ff976a", "#1989fa", "#07c160"
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("编辑分类") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("分类名称") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = iconRes,
                    onValueChange = { iconRes = it },
                    label = { Text("图标资源") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Text("选择颜色：", fontSize = 14.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colors.forEach { c ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color(android.graphics.Color.parseColor(c)),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { color = c },
                            contentAlignment = Alignment.Center
                        ) {
                            if (color == c) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name, iconRes, color)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

