package com.notifyhub

import java.util.regex.Pattern

/**
 * 应用包名 -> 中文名 映射，以及本地通知分类规则。
 * 不依赖任何网络/AI，纯规则，零成本覆盖常见类型。
 */
object Classifier {

    private val PKG_NAMES = mapOf(
        "com.tencent.mm" to "微信",
        "com.tencent.mobileqq" to "QQ",
        "com.tencent.mtt" to "QQ浏览器",
        "com.eg.android.AlipayGphone" to "支付宝",
        "com.taobao.taobao" to "淘宝",
        "com.taobao.idlefish" to "闲鱼",
        "com.jingdong.app.mall" to "京东",
        "com.sina.weibo" to "微博",
        "com.ss.android.ugc.aweme" to "抖音",
        "com.ss.android.article.news" to "今日头条",
        "com.netease.newsreader" to "网易新闻",
        "com.baidu.searchbox" to "百度",
        "com.meituan.android" to "美团",
        "com.sankuai.meituan.takeoutnew" to "美团外卖",
        "com.didi.passenger" to "滴滴出行",
        "com.tencent.wework" to "企业微信",
        "com.alibaba.android.rimet" to "钉钉",
        "com.cmcc.cmccapp" to "中国移动",
        "com.tencent.androidqqmail" to "QQ邮箱",
        "com.netease.mail" to "网易邮箱",
        "com.icbc" to "工商银行",
        "com.chinamworld.bocmbci" to "中国银行",
        "cmb.pb" to "招商银行",
        "com.tencent.tmgp.sgame" to "王者荣耀",
        "com.miui.securitycenter" to "系统安全",
        "com.coloros.notification" to "系统通知",
        "com.android.systemui" to "系统界面"
    )

    fun appName(pkg: String?): String {
        if (pkg != null && PKG_NAMES.containsKey(pkg)) return PKG_NAMES[pkg]!!
        return pkg ?: "未知"
    }

    fun classify(pkg: String?, app: String?, title: String?, text: String?): String {
        val a = (app ?: "").lowercase()
        val t = ((title ?: "") + " " + (text ?: "")).lowercase()
        val s = "$a $t"

        return when {
            Pattern.compile("验证码|校验码|动态码|登录码|code|验证码|otp").matcher(s).find() -> "验证码"
            Pattern.compile("快递|顺丰|圆通|中通|申通|韵达|京东物流|菜鸟|取件|派送|运单|ems").matcher(s).find() -> "快递"
            Pattern.compile("到账|收款|转账|还款|账单|余额|工资|红包|支付成功|扣款|信用卡|分期").matcher(s).find() -> "账单/财务"
            Pattern.compile("点赞|评论|关注|转发|@|新消息|好友|聊天|群|微信团队|qq").matcher(s).find() -> "聊天/社交"
            Pattern.compile("促销|优惠|折扣|满减|秒杀|双1?1|618|降价|领券|活动|限时|特价|省钱").matcher(s).find() -> "促销"
            Pattern.compile("会议|日程|提醒|待办|闹钟|课程|预约|签到").matcher(s).find() -> "日程/提醒"
            Pattern.compile("新闻|头条|热点|快讯|要闻|暴|灾|地震|官方通报").matcher(s).find() -> "新闻"
            Pattern.compile("系统|更新|升级|版本|内存|电量|权限|安全|病毒|清理|优化").matcher(s).find() -> "系统"
            else -> "其他"
        }
    }
}
