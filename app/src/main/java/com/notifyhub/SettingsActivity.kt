package com.notifyhub

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * 设置页：生成本地摘要、查看说明。
 * （AI 联网摘要为可选高级功能，本离线版用本地规则汇总，零成本。）
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var db: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        db = DbHelper(this)

        findViewById<Button>(R.id.btnSummary).setOnClickListener {
            val text = db.summaryText()
            AlertDialog.Builder(this)
                .setTitle("通知汇总")
                .setMessage(text)
                .setPositiveButton("好") { _, _ -> }
                .show()
        }

        findViewById<Button>(R.id.btnAbout).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("关于 通知汇")
                .setMessage(
                    "一款把手机通知聚合并分类汇总的应用。\n\n" +
                    "1. 首次打开请授权「通知访问权限」；\n" +
                    "2. 点右上角「应用」选择只想接收的 App，其余关闭；\n" +
                    "3. 首页实时显示通知，可按分类/应用筛选、搜索；\n" +
                    "4. 设置页可一键生成本地汇总。\n\n" +
                    "数据全部存在本机，不上传任何服务器。"
                )
                .setPositiveButton("知道了") { _, _ -> }
                .show()
        }
    }
}
