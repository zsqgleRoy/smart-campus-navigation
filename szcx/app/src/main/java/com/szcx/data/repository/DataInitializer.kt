package com.szcx.data.repository

import android.content.Context
import com.szcx.data.database.SzcxDatabase
import com.szcx.data.model.Category
import com.szcx.data.model.Website
import com.szcx.util.FormatUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DataInitializer {
    suspend fun initializeData(context: Context) = withContext(Dispatchers.IO) {
        val database = SzcxDatabase.getDatabase(context)
        val websiteDao = database.websiteDao()
        val categoryDao = database.categoryDao()
        
        // 检查数据是否已存在（简化检查，直接插入，Room 会处理冲突）
        
        // 初始化分类数据
        val categories = listOf(
            Category("all", "全部", "apps", "#1989fa"),
            Category("academic", "教务教学", "description", "#07c160"),
            Category("library", "图书馆", "orders", "#ff976a"),
            Category("campus", "校园生活", "home", "#ee0a24"),
            Category("service", "公共服务", "service", "#ff976a"),
            Category("news", "新闻资讯", "newspaper", "#1989fa"),
            Category("office", "办公系统", "setting", "#07c160")
        )
        categoryDao.insertCategories(categories)
        
        // 初始化网站数据（自动生成图标URL）
        val websites = listOf(
            // 教务教学
            Website("1", "奥兰教务系统", "http://my.cxxy.seu.edu.cn/", FormatUtils.getWebsiteIcon("http://my.cxxy.seu.edu.cn/"), "academic", "学生选课、成绩查询、课表查看", true),
            Website("2", "选课系统", "http://xk.cxxy.seu.edu.cn/xsxk/logout.xk", FormatUtils.getWebsiteIcon("http://xk.cxxy.seu.edu.cn/xsxk/logout.xk"), "academic", "在线选课、课程资源下载", true),
            Website("3", "缴费平台", "http://upay.cxxy.seu.edu.cn/payment/", FormatUtils.getWebsiteIcon("http://upay.cxxy.seu.edu.cn/payment/"), "academic", "学费缴纳、费用查询", true),
            Website("4", "学信网", "https://www.chsi.com.cn/", FormatUtils.getWebsiteIcon("https://www.chsi.com.cn/"), "academic", "教育部学生信息网 - 学信网", true),
            Website("5", "CET报名/查询", "https://cet-bm.neea.edu.cn/", FormatUtils.getWebsiteIcon("https://cet-bm.neea.edu.cn/"), "academic", "四、六级考试报名、成绩查询", true),
            Website("6", "毕设系统", "http://sy.cxxy.seu.edu.cn/bylw/Index.aspx", FormatUtils.getWebsiteIcon("http://sy.cxxy.seu.edu.cn/bylw/Index.aspx"), "academic", "毕业设计管理、论文提交", true),
            Website("7", "综合实践教学系统", "http://sy.cxxy.seu.edu.cn/sjpt/", FormatUtils.getWebsiteIcon("http://sy.cxxy.seu.edu.cn/sjpt/"), "academic", "实践教学管理、实习报告提交"),
            Website("8", "实验系统", "http://sy.cxxy.seu.edu.cn/syjx/index2.aspx", FormatUtils.getWebsiteIcon("http://sy.cxxy.seu.edu.cn/syjx/index2.aspx"), "academic", "实验课程管理、实验报告提交"),
            Website("9", "拓展学分系统", "http://sy.cxxy.seu.edu.cn/sztz/Main/Main.aspx", FormatUtils.getWebsiteIcon("http://sy.cxxy.seu.edu.cn/sztz/Main/Main.aspx"), "academic", "拓展学分申请、审核"),
            Website("10", "研招网", "https://yz.chsi.com.cn/", FormatUtils.getWebsiteIcon("https://yz.chsi.com.cn/"), "academic", "中国研究生招生信息网 - 研招网", true),
            Website("11", "乐课教学云平台", "http://cxxy.seu.edu.cn/2019/0918/c127a8710/page.htm", FormatUtils.getWebsiteIcon("http://cxxy.seu.edu.cn/2019/0918/c127a8710/page.htm"), "office", "教学云平台、在线课堂"),
            Website("17", "竞赛系统", "http://sy.cxxy.seu.edu.cn/js/Index.aspx", FormatUtils.getWebsiteIcon("http://sy.cxxy.seu.edu.cn/js/Index.aspx"), "academic", "学科竞赛报名、成绩查询"),
            
            // 图书馆
            Website("12", "图书馆", "http://lib.cxxy.seu.edu.cn/", FormatUtils.getWebsiteIcon("http://lib.cxxy.seu.edu.cn/"), "library", "图书检索、借阅查询、座位预约"),
            Website("13", "数字资源", "http://szjx.cxxy.seu.edu.cn/umse/login/", FormatUtils.getWebsiteIcon("http://szjx.cxxy.seu.edu.cn/umse/login/"), "library", "电子图书、期刊数据库"),
            
            // 校园生活
            Website("14", "校历", "http://cxxy.seu.edu.cn/2019/0918/c133a2410/page.htm", FormatUtils.getWebsiteIcon("http://cxxy.seu.edu.cn/2019/0918/c133a2410/page.htm"), "campus", "校历查询、学期安排", true),
            Website("15", "晨跑系统", "https://www.fntiyu.com/college/mobile/attendanceDataQuery.jsp?school_id=S1050", FormatUtils.getWebsiteIcon("https://www.fntiyu.com/college/mobile/attendanceDataQuery.jsp?school_id=S1050"), "campus", "晨跑打卡、体育成绩查询"),
            Website("16", "自助购电", "http://goudian.cxxy.seu.edu.cn/charge-app/#/pays?id=428", FormatUtils.getWebsiteIcon("http://goudian.cxxy.seu.edu.cn/charge-app/#/pays?id=428"), "campus", "自助购电、用电查询"),
            Website("18", "校友会", "http://alum.cxxy.seu.edu.cn/alumniLogin", FormatUtils.getWebsiteIcon("http://alum.cxxy.seu.edu.cn/alumniLogin"), "campus", "校友信息、校友活动"),
            Website("22", "学生处", "http://xsc.cxxy.seu.edu.cn/", FormatUtils.getWebsiteIcon("http://xsc.cxxy.seu.edu.cn/"), "campus", "学生活动、通知公告"),
            Website("25", "检查更新", "https://pan.baidu.com/s/1RCq6gG20WPMlKgQi_U2imA?pwd=cxxy", FormatUtils.getWebsiteIcon("https://pan.baidu.com/s/1RCq6gG20WPMlKgQi_U2imA?pwd=cxxy"), "campus", "校园网客户端、VPN客户端下载"),
            
            // 公共服务
            Website("19", "网络服务", "https://nic.seu.edu.cn/", FormatUtils.getWebsiteIcon("https://nic.seu.edu.cn/"), "service", "校园网认证、流量查询"),
            Website("20", "后勤服务", "http://hqgl.cxxy.seu.edu.cn/main.htm", FormatUtils.getWebsiteIcon("http://hqgl.cxxy.seu.edu.cn/main.htm"), "service", "报修、投诉建议"),
            
            // 新闻资讯
            Website("21", "学校官网", "http://cxxy.seu.edu.cn/", FormatUtils.getWebsiteIcon("http://cxxy.seu.edu.cn/"), "news", "学校新闻、通知公告"),
            Website("23", "党建", "http://dangshi.cxxy.seu.edu.cn/main.htm", FormatUtils.getWebsiteIcon("http://dangshi.cxxy.seu.edu.cn/main.htm"), "news", "党建资讯、党员活动"),
            
            // 办公系统
            Website("24", "人事系统", "http://zp.cxxy.seu.edu.cn/manage/login", FormatUtils.getWebsiteIcon("http://zp.cxxy.seu.edu.cn/manage/login"), "office", "人事信息、工资查询")
        )
        websiteDao.insertWebsites(websites)
    }
}

