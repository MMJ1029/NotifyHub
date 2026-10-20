package com.notifyhub

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var db: DbHelper
    private lateinit var adapter: NotiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DbHelper(this)
        adapter = NotiAdapter()
        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        findViewById<ImageButton>(R.id.btnApps).setOnClickListener {
            startActivity(Intent(this, AppListActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        val search = findViewById<EditText>(R.id.search)
        search.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                load(s.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        checkListenerPermission()
        load("")
    }

    private fun checkListenerPermission() {
        val enabledListeners = try {
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        } catch (e: Exception) {
            ""
        }
        if (enabledListeners.isNullOrEmpty() || !enabledListeners.contains(packageName)) {
            Toast.makeText(this, "请先授权「通知访问权限」才能抓取通知", Toast.LENGTH_LONG).show()
            try {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            } catch (e: Exception) {
                // 某些 ROM 入口不同，忽略
            }
        }
    }

    private fun load(q: String?) {
        val items = db.list(300, null, q)
        adapter.setItems(items)
        val stats = db.stats()
        val catText = if (stats.isEmpty()) "暂无数据" else
            stats.entries.joinToString("   ") { "${it.key}:${it.value}" }
        findViewById<TextView>(R.id.stats).text = "共 ${items.size} 条   |   $catText"
    }

    override fun onResume() {
        super.onResume()
        val s = findViewById<EditText>(R.id.search).text.toString()
        load(s)
    }
}

class NotiAdapter : RecyclerView.Adapter<NotiAdapter.VH>() {

    private var items = listOf<Noti>()

    fun setItems(l: List<Noti>) {
        items = l
        notifyDataSetChanged()
    }

    class VH(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val app: TextView = view.findViewById(R.id.itemApp)
        val title: TextView = view.findViewById(R.id.itemTitle)
        val text: TextView = view.findViewById(R.id.itemText)
        val cat: TextView = view.findViewById(R.id.itemCat)
        val time: TextView = view.findViewById(R.id.itemTime)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): VH {
        val v = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_noti, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val n = items[position]
        holder.app.text = n.app
        holder.title.text = n.title
        holder.text.text = n.text
        holder.cat.text = n.category
        holder.cat.setBackgroundColor(categoryColor(n.category))
        holder.time.text = n.time
    }

    override fun getItemCount(): Int = items.size

    companion object {
        fun categoryColor(cat: String): Int {
            val map = mapOf(
                "验证码" to 0xFFE60012.toInt(),
                "快递" to 0xFF0091FF.toInt(),
                "账单/财务" to 0xFF00A86B.toInt(),
                "聊天/社交" to 0xFF7B61FF.toInt(),
                "促销" to 0xFFFF9500.toInt(),
                "日程/提醒" to 0xFF34C759.toInt(),
                "新闻" to 0xFF5856D6.toInt(),
                "系统" to 0xFF8E8E93.toInt()
            )
            return map[cat] ?: 0xFF8E8E93.toInt()
        }
    }
}
