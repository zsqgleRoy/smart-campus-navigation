package com.szcx.util

import com.szcx.BuildConfig

object VersionUtils {
    /**
     * 获取应用版本名称（如 "3.5.13"）
     */
    val versionName: String
        get() = BuildConfig.VERSION_NAME
    
    /**
     * 获取应用版本代码（如 1）
     */
    val versionCode: Int
        get() = BuildConfig.VERSION_CODE
    
    /**
     * 获取格式化的版本字符串（如 "version 3.5.13"）
     */
    val formattedVersion: String
        get() = "version $versionName"
    
    /**
     * 获取完整的版本信息（如 "3.5.13 (1)"）
     */
    val fullVersion: String
        get() = "$versionName ($versionCode)"
}



