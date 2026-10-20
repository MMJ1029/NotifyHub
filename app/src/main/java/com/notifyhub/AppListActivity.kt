package com.notifyhub

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

/**
 * 应用管理：列出所有出现过的 App，每个一个开关。
 * 打开=接收并固定；关闭=忽略新通知并清除历史。选择持久化。
 */
class AppListActivity : AppCompatActivity() {

    private lateinit var db: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applist)
        db = DbHelper(this)

        val list = findViewById<LinearLayout>(R.id.appList)
        val apps = db.apps()

        if (apps.isEmpty()) {
            val tip = TextView(this).apply {
                text = "还没有抓到任何通知。\n先回到首页，授权通知权限，等手机来几条通知后这里就会出现应用列表。"
                setPadding(24, 24, 24, 24)
            }
            list.addView(tip)
            return
        }

        for ((name, count) in apps) {
            val row = layoutInflater.inflate(R.layout.item_app_toggle, list, false)
            row.findViewById<TextView>(R.id.appName).text = "$name（$count 条）"
            val sw = row.findViewById<Switch>(R.id.appSwitch)
            sw.isChecked = db.isEnabled(name)
            sw.setOnCheckedChangeListener { _, checked ->
                db.setEnabled(name, checked)
                if (!checked) {
                    Toast.makeText(this, "已关闭 $name 并清除其历史", Toast.LENGTH_SHORT).show()
                }
            }
            list.addView(row)
        }
    }
}
