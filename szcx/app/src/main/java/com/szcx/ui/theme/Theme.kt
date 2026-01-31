package com.szcx.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = PrimaryGreen,
    tertiary = PrimaryOrange,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2C2C2C),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFFB0B0B0)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = PrimaryGreen,
    tertiary = PrimaryOrange,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    surfaceVariant = Color(0xFFF0F0F0),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1C1C1C),
    onSurface = Color(0xFF1C1C1C),
    onSurfaceVariant = Color(0xFF616161)
)

// 护眼模式颜色方案
private val EyeCareColorScheme = lightColorScheme(
    primary = EyeCarePrimary,
    secondary = PrimaryGreen,
    tertiary = Color(0xFF81C784),
    background = EyeCareBackground,
    surface = EyeCareSurface,
    surfaceVariant = Color(0xFFE0F2E0),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = EyeCareOnSurface,
    onSurface = EyeCareOnSurface,
    onSurfaceVariant = Color(0xFF558B2F)
)

@Composable
fun SzcxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeMode: String = "light", // "light", "dark", "eye"
    dynamicColor: Boolean = false, // 禁用动态颜色以支持自定义主题
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        "dark" -> DarkColorScheme
        "eye" -> EyeCareColorScheme
        else -> LightColorScheme
    }
    
    val isDark = themeMode == "dark"
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            
            // 使用主题的背景色作为状态栏颜色，实现无缝融合
            val statusBarColor = colorScheme.background
            
            // 设置状态栏颜色（与页面背景色一致）
            window.statusBarColor = statusBarColor.toArgb()
            
            // 设置系统栏样式（状态栏图标颜色）
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = themeMode != "dark"
            
            // 启用全屏布局（让内容延伸到状态栏下方）
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

