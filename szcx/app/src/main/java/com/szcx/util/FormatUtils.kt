package com.szcx.util

import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FormatUtils {
    fun formatTime(timestamp: Long): String {
        val date = Date(timestamp)
        val now = Date()
        val diff = now.time - date.time
        
        val minute = 60 * 1000L
        val hour = 60 * minute
        val day = 24 * hour
        
        return when {
            diff < minute -> "刚刚"
            diff < hour -> "${diff / minute}分钟前"
            diff < day -> "${diff / hour}小时前"
            diff < 7 * day -> "${diff / day}天前"
            else -> SimpleDateFormat("MM月dd日", Locale.getDefault()).format(date)
        }
    }
    
    fun getWebsiteIcon(url: String): String {
        return try {
            val domain = URL(url).host
            "https://www.google.com/s2/favicons?domain=$domain&sz=64"
        } catch (e: Exception) {
            ""
        }
    }
    
    fun isValidUrl(url: String): Boolean {
        return try {
            URL(url)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun isUrlFormat(input: String): Boolean {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return false
        
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("ftp://")) {
            return isValidUrl(trimmed)
        }
        
        if (trimmed.contains(".") && 
            (trimmed.startsWith("www.") || 
             trimmed.matches(Regex(".*\\.(com|cn|net|org|edu|gov|io|co|tv|me|cc|info|xyz|top|site|online|tech|app|dev|pro|biz|name|mobi|asia|tel|travel|jobs|xxx|ac|ad|ae|af|ag|ai|al|am|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cw|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tr|tt|tv|tw|tz|ua|ug|uk|um|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|za|zm|zw)(/.*)?$", RegexOption.IGNORE_CASE)))) {
            return true
        }
        
        return false
    }
    
    fun normalizeUrl(input: String, protocol: String = "https"): String {
        val trimmed = input.trim()
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("ftp://")) {
            return trimmed
        }
        return "$protocol://$trimmed"
    }
    
    fun extractProtocol(url: String): String? {
        return when {
            url.startsWith("https://") -> "https"
            url.startsWith("http://") -> "http"
            url.startsWith("ftp://") -> "ftp"
            else -> null
        }
    }
    
    fun getBaiduSearchUrl(query: String): String {
        val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
        return "https://www.baidu.com/baidu?ie=utf-8&wd=$encodedQuery"
    }
}

