package com.notifyhub

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 系统级通知监听服务。
 * 需在 系统设置 -> 通知 -> 通知访问权限 中手动授权本应用。
 */
class NotificationListenerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        try {
            val pkg = sbn.packageName ?: return
            val extras = sbn.notification.extras ?: return
            val title = extras.getCharSequence("android.title")?.toString() ?: ""
            val text = extras.getCharSequence("android.text")?.toString() ?: ""
            if (title.isBlank() && text.isBlank()) return

            val app = Classifier.appName(pkg)
            val db = DbHelper(this)

            // 白名单：关闭的应用直接忽略，不入库
            if (!db.isEnabled(app)) return

            val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val category = Classifier.classify(pkg, app, title, text)
            db.insert(pkg, app, title, text, category, time)
        } catch (e: Exception) {
            // 单条异常不影响服务
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // 暂不需要处理移除事件
    }
}
