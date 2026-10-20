package com.notifyhub

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Noti(
    val id: Long,
    val pkg: String,
    val app: String,
    val title: String,
    val text: String,
    val category: String,
    val time: String
)

class DbHelper(context: Context) : SQLiteOpenHelper(context, "notifyhub.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS notifications(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pkg TEXT,
                app TEXT,
                title TEXT,
                text TEXT,
                category TEXT,
                time TEXT
            )"""
        )
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS app_settings(
                app TEXT PRIMARY KEY,
                enabled INTEGER NOT NULL DEFAULT 1
            )"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    /** 写入一条通知 */
    fun insert(pkg: String, app: String, title: String, text: String, category: String, time: String): Long {
        val v = ContentValues().apply {
            put("pkg", pkg)
            put("app", app)
            put("title", title)
            put("text", text)
            put("category", category)
            put("time", time)
        }
        return writableDatabase.insert("notifications", null, v)
    }

    /** 该应用是否接收（白名单）。默认接收。 */
    fun isEnabled(app: String): Boolean {
        val c = readableDatabase.query(
            "app_settings", arrayOf("enabled"), "app=?",
            arrayOf(app), null, null, null
        )
        val enabled = if (c.moveToFirst()) c.getInt(0) == 1 else true
        c.close()
        return enabled
    }

    /** 设置应用是否接收；关闭时一并清除其历史 */
    fun setEnabled(app: String, enabled: Boolean) {
        val v = ContentValues().apply {
            put("app", app)
            put("enabled", if (enabled) 1 else 0)
        }
        writableDatabase.insertWithOnConflict("app_settings", null, v, SQLiteDatabase.CONFLICT_REPLACE)
        if (!enabled) deleteByApp(app)
    }

    /** 列出通知，支持按应用过滤与关键词搜索 */
    fun list(limit: Int = 300, filterApp: String? = null, query: String? = null): List<Noti> {
        val where = mutableListOf<String>()
        val args = mutableListOf<String>()
        if (filterApp != null) {
            where.add("app=?")
            args.add(filterApp)
        }
        if (!query.isNullOrBlank()) {
            where.add("(title LIKE ? OR text LIKE ? OR app LIKE ?)")
            args.add("%$query%")
            args.add("%$query%")
            args.add("%$query%")
        }
        val w = if (where.isEmpty()) null else where.joinToString(" AND ")
        val c = readableDatabase.query(
            "notifications", null, w, args.toTypedArray(),
            null, null, "id DESC", limit.toString()
        )
        val out = mutableListOf<Noti>()
        while (c.moveToNext()) {
            out.add(
                Noti(
                    c.getLong(0),
                    c.getString(1) ?: "",
                    c.getString(2) ?: "",
                    c.getString(3) ?: "",
                    c.getString(4) ?: "",
                    c.getString(5) ?: "",
                    c.getString(6) ?: ""
                )
            )
        }
        c.close()
        return out
    }

    /** 各应用出现次数（用于"应用管理"页） */
    fun apps(): List<Pair<String, Int>> {
        val c = readableDatabase.rawQuery(
            "SELECT app, COUNT(*) FROM notifications GROUP BY app ORDER BY COUNT(*) DESC",
            null
        )
        val out = mutableListOf<Pair<String, Int>>()
        while (c.moveToNext()) {
            out.add(Pair(c.getString(0) ?: "", c.getInt(1)))
        }
        c.close()
        return out
    }

    fun deleteByApp(app: String) {
        writableDatabase.delete("notifications", "app=?", arrayOf(app))
    }

    /** 分类统计 */
    fun stats(): Map<String, Int> {
        val c = readableDatabase.rawQuery(
            "SELECT category, COUNT(*) FROM notifications GROUP BY category",
            null
        )
        val m = linkedMapOf<String, Int>()
        while (c.moveToNext()) {
            m[c.getString(0) ?: "其他"] = c.getInt(1)
        }
        c.close()
        return m
    }

    /** 生成今日/全部本地摘要文本 */
    fun summaryText(): String {
        val stats = stats()
        if (stats.isEmpty()) return "还没有收到任何通知。"
        val sb = StringBuilder()
        sb.append("共 ${stats.values.sum()} 条通知，分类如下：\n")
        for ((k, v) in stats) {
            sb.append("· $k：$v 条\n")
        }
        return sb.toString().trimEnd()
    }
}
