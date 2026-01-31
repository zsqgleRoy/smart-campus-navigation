import type { Website, Category } from '@/types'

// 分类数据
export const categories: Category[] = [
  { id: 'all', name: '全部', icon: 'apps-o', color: '#1989fa' },
  { id: 'academic', name: '教务教学', icon: 'description', color: '#07c160' },
  { id: 'library', name: '图书馆', icon: 'orders-o', color: '#ff976a' },
  { id: 'campus', name: '校园生活', icon: 'home-o', color: '#ee0a24' },
  { id: 'service', name: '公共服务', icon: 'service-o', color: '#ff976a' },
  { id: 'news', name: '新闻资讯', icon: 'newspaper-o', color: '#1989fa' },
  { id: 'office', name: '办公系统', icon: 'setting-o', color: '#07c160' }
]

// 网站数据（按分类排序，热门网站不超过8个）
export const websites: Website[] = [
  // ========== 教务教学 ==========
  {
    id: '1',
    name: '奥兰教务系统',
    url: 'http://my.cxxy.seu.edu.cn/',
    category: 'academic',
    description: '学生选课、成绩查询、课表查看',
    isHot: true
  },
  {
    id: '2',
    name: '选课系统',
    url: 'http://xk.cxxy.seu.edu.cn/xsxk/logout.xk',
    category: 'academic',
    description: '在线选课、课程资源下载',
    isHot: true
  },
  {
    id: '3',
    name: '缴费平台',
    url: 'http://upay.cxxy.seu.edu.cn/payment/',
    category: 'academic',
    description: '学费缴纳、费用查询',
    isHot: true
  },
  {
    id: '4',
    name: '学信网',
    url: 'https://www.chsi.com.cn/',
    category: 'academic',
    description: '教育部学生信息网 - 学信网',
    isHot: true
  },
  {
    id: '5',
    name: 'CET报名/查询',
    url: 'https://cet-bm.neea.edu.cn/',
    category: 'academic',
    description: '四、六级考试报名、成绩查询',
    isHot: true
  },
  {
    id: '6',
    name: '毕设系统',
    url: 'http://sy.cxxy.seu.edu.cn/bylw/Index.aspx',
    category: 'academic',
    description: '毕业设计管理、论文提交',
    isHot: true
  },
  {
    id: '7',
    name: '综合实践教学系统',
    url: 'http://sy.cxxy.seu.edu.cn/sjpt/',
    category: 'academic',
    description: '实践教学管理、实习报告提交'
  },
  {
    id: '8',
    name: '实验系统',
    url: 'http://sy.cxxy.seu.edu.cn/syjx/index2.aspx',
    category: 'academic',
    description: '实验课程管理、实验报告提交'
  },
  {
    id: '9',
    name: '拓展学分系统',
    url: 'http://sy.cxxy.seu.edu.cn/sztz/Main/Main.aspx',
    category: 'academic',
    description: '拓展学分申请、审核'
  },
  {
    id: '10',
    name: '研招网',
    url: 'https://yz.chsi.com.cn/',
    category: 'academic',
    description: '中国研究生招生信息网 - 研招网',
    isHot: true
  },
  {
    id: '11',
    name: '乐课教学云平台',
    url: 'http://cxxy.seu.edu.cn/2019/0918/c127a8710/page.htm',
    category: 'office',
    description: '教学云平台、在线课堂'
  },

  // ========== 图书馆 ==========
  {
    id: '12',
    name: '图书馆',
    url: 'http://lib.cxxy.seu.edu.cn/',
    category: 'library',
    description: '图书检索、借阅查询、座位预约',
  },
  {
    id: '13',
    name: '数字资源',
    url: 'http://szjx.cxxy.seu.edu.cn/umse/login/',
    category: 'library',
    description: '电子图书、期刊数据库'
  },

  // ========== 校园生活 ==========
  {
    id: '14',
    name: '校历',
    url: 'http://cxxy.seu.edu.cn/2019/0918/c133a2410/page.htm',
    category: 'campus',
    description: '校历查询、学期安排',
    isHot: true
  },
  {
    id: '15',
    name: '晨跑系统',
    url: 'https://www.fntiyu.com/college/mobile/attendanceDataQuery.jsp?school_id=S1050',
    category: 'campus',
    description: '晨跑打卡、体育成绩查询'
  },
  {
    id: '16',
    name: '自助购电',
    url: 'http://goudian.cxxy.seu.edu.cn/charge-app/#/pays?id=428',
    category: 'campus',
    description: '自助购电、用电查询'
  },
  {
    id: '17',
    name: '竞赛系统',
    url: 'http://sy.cxxy.seu.edu.cn/js/Index.aspx',
    category: 'academic',
    description: '学科竞赛报名、成绩查询'
  },
  {
    id: '18',
    name: '校友会',
    url: 'http://alum.cxxy.seu.edu.cn/alumniLogin',
    category: 'campus',
    description: '校友信息、校友活动'
  },

  // ========== 公共服务 ==========
  {
    id: '19',
    name: '网络服务',
    url: 'https://nic.seu.edu.cn/',
    category: 'service',
    description: '校园网认证、流量查询'
  },
  {
    id: '20',
    name: '后勤服务',
    url: 'http://hqgl.cxxy.seu.edu.cn/main.htm',
    category: 'service',
    description: '报修、投诉建议'
  },

  // ========== 新闻资讯 ==========
  {
    id: '21',
    name: '学校官网',
    url: 'http://cxxy.seu.edu.cn/',
    category: 'news',
    description: '学校新闻、通知公告'
  },
  {
    id: '22',
    name: '学生处',
    url: 'http://xsc.cxxy.seu.edu.cn/',
    category: 'campus',
    description: '学生活动、通知公告'
  },
  {
    id: '23',
    name: '党建',
    url: 'http://dangshi.cxxy.seu.edu.cn/main.htm',
    category: 'news',
    description: '党建资讯、党员活动'
  },

  // ========== 办公系统 ==========
  {
    id: '24',
    name: '人事系统',
    url: 'http://zp.cxxy.seu.edu.cn/manage/login',
    category: 'office',
    description: '人事信息、工资查询'
  },
  {
    id: '25',
    name: '检查更新',
    url: 'https://pan.baidu.com/s/1RCq6gG20WPMlKgQi_U2imA?pwd=cxxy ',
    category: 'campus',
    description: '校园网客户端、VPN客户端下载'
  }
]

// 根据分类获取网站
export function getWebsitesByCategory(categoryId: string): Website[] {
  if (categoryId === 'all') {
    return websites
  }
  return websites.filter(w => w.category === categoryId)
}

// 获取热门网站
export function getHotWebsites(): Website[] {
  return websites.filter(w => w.isHot).slice(0, 8)
}

// 搜索网站
export function searchWebsites(keyword: string): Website[] {
  if (!keyword.trim()) {
    return []
  }
  const lowerKeyword = keyword.toLowerCase()
  return websites.filter(
    w =>
      w.name.toLowerCase().includes(lowerKeyword) ||
      w.description?.toLowerCase().includes(lowerKeyword)
  )
}
