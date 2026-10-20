# 通知汇 NotifyHub

> 把手机通知聚合、分类、汇总 —— **数据全部存本地，不上传任何服务器**。

**NotifyHub** is a privacy-first Android notification aggregator. It listens to system notifications *locally*, filters them by app, auto-categorizes them, and summarizes everything **on-device** — no cloud, no account, no data upload.

![License](https://img.shields.io/badge/license-MIT-green)
![Platform](https://img.shields.io/badge/platform-Android-blue)
![minSdk](https://img.shields.io/badge/minSdk-24%20(Android%207.0%2B)-brightgreen)
![Language](https://img.shields.io/badge/language-Kotlin-purple)

- 语言 / Language：中文（[English abstract](#english) below）
- 🚀 **在线下载页**（推荐）：<https://mmj1029.github.io/NotifyHub/>
- 🧪 **在线演示**（无需安装）：<https://mmj1029.github.io/NotifyHub/demo.html>

---

## 这是什么

手机通知太多、太杂？「通知汇」帮你把系统通知抓下来，按应用筛选、自动分类，还能自建分类、自定义颜色字体、单独提醒、按群/好友屏蔽，随时一键汇总。

**核心原则：数据 100% 存在手机本地私有数据库里，绝不联网上传。**

## 功能特性

- 🔌 **系统级通知监听**：基于 `NotificationListenerService`，不依赖 Termux / Tasker / 无障碍。
- 🎯 **应用白名单筛选**：只收你选的 App，选择持久化保存；新出现的 App 默认接收，可随时关。
- 🗂️ **智能自动分类**：验证码 / 快递 / 账单财务 / 聊天社交 / 促销 / 日程提醒 / 新闻 / 系统 / 其他。
- ✨ **自建分类**：把「微信 + QQ」等任意应用归到你自己命名的分类（如「家人消息」「工作群」）。
- 🎨 **自定义外观**：每个分类卡片边框颜色随你挑；字体大小 / 等宽 / 衬线可选。
- 🔔 **单独提醒**：指定分类来新消息立刻单独弹提醒。
- 🔕 **未读提醒**：有没查看的通知，按间隔提醒你。
- 🧩 **按来源细分屏蔽**：微信 A群 / B群 / 某个好友分开显示，可单独屏蔽某个群或好友。
- 📋 **实时列表 + 搜索 + 一键汇总**：按分类、按来源统计数量，汇总里也显示你自建的分类。

## 快速开始

### 1. 下载安装包

| 版本 | 说明 |
| --- | --- |
| [notifyhub-v1.3.apk](https://mmj1029.github.io/NotifyHub/notifyhub-v1.3.apk) | ✅ 最新版（推荐） |
| [notifyhub-v1.2.apk](https://mmj1029.github.io/NotifyHub/notifyhub-v1.2.apk) | 旧版（历史保留） |

也可以在 [Releases 页面](https://github.com/MMJ1029/NotifyHub/releases) 下载（带版本更新日志）。

> v1.3 重大修复：解决「通知使用权已开、却一条通知都收不到」的 bug；并针对真我（ColorOS）机型加入授权后自动重连。

### 2. 安装到手机

1. 把 APK 传到手机（数据线 / 微信文件传输助手 / 网盘），点击安装。
2. 手机可能拦截「未知来源」安装：
   - 弹窗点「设置」→ 允许该来源；或
   - 设置 → 密码与安全 → 系统安全 → 安装未知应用 → 允许对应应用。
3. 桌面出现「通知汇」图标即完成。
4. 若之前装过旧版，**直接覆盖安装即可，数据保留**。

### 3. 首次授权

1. 打开「通知汇」→ 会自动跳到「通知访问权限」设置页，找到「通知汇」并开启。
   - 若没跳：系统设置 → 通知 → 通知访问权限（真我：设置 → 通知 → 更多通知设置 → 通知使用权）→ 开启「通知汇」。
2. 回到 App，等手机来几条通知，首页就会出现内容。
3. **选应用**：点右上角「应用」图标 → 列出所有出现过的 App，每个一个开关（开=接收并固定，关=忽略并清除历史）。
4. **汇总**：右上角「设置」→「生成通知汇总」，本地统计各类数量。

## 使用说明

| 操作 | 位置 |
| --- | --- |
| 自建分类（微信+QQ 归一类） | 首页右上角「＋（分类）」→ 命名 → 勾选应用 → 选颜色/字体/是否单独提醒 |
| 自定义字体 | 「设置」→ 字体大小 / 等宽 / 衬线 |
| 未读提醒 | 「设置」→ 开未读提醒（按间隔提醒未读） |
| 屏蔽某个群/好友 | 通知卡片右上角「🚫 屏蔽」；「设置」里可恢复 |
| 生成汇总 | 「设置」→「生成通知汇总」 |

## 保活（重要）

锁屏后系统可能杀掉后台导致收不到新通知，请做以下设置（真我 ColorOS 为例）：

- 手机管家 → 应用管理 → **通知汇** → 自启动「允许」+ 电池/耗电保护设为「无限制 / 允许后台」。
- 建议把「通知汇」加入省电白名单。
- 首次打开若弹「允许后台运行」→ 点「去允许」。

## 从源码编译

### 方式 A：GitHub Actions 一键编译（推荐）

1. Fork 本仓库。
2. 进入你的仓库 → **Actions** 标签页 → `Build APK` → `Run workflow` → 确认。
3. 等 3~6 分钟变绿后，下载名为 `notifyhub-apk` 的压缩包 → 解压得到 `app-debug.apk`。

### 方式 B：Android Studio 本地编译

- 环境：JDK 17 + Android Studio（AGP 8.2.2 / Gradle 8.2 / Kotlin 1.9.22）
- 打开本仓库 → `Build` → `Build APK(s)` → 产物在 `app/build/outputs/apk/debug/`。

## 技术栈

- **语言**：Kotlin
- **架构**：原生 Android（单模块）
- **关键组件**：`NotificationListenerService`（通知监听）、`SQLite`（本地存储）
- **SDK**：minSdk 24（Android 7.0+） / targetSdk 34（Android 14）
- **权限**：仅 `INTERNET`（未做任何网络上传，预留字段）

## 项目结构

```
NotifyHub/
├── app/                         # 应用模块（Kotlin 源码 + 布局 + 资源）
│   └── src/main/java/com/notifyhub/
│       ├── NotificationListenerService.kt   # 通知监听
│       ├── Classifier.kt                    # 分类规则引擎
│       ├── DbHelper.kt                      # 本地 SQLite
│       └── MainActivity.kt / AppListActivity.kt / SettingsActivity.kt
├── docs/                        # GitHub Pages：下载页 + 演示页 + APK
│   ├── index.html               # 下载落地页
│   ├── demo.html                # 功能预演台（浏览器即可体验）
│   ├── notifyhub-v1.3.apk       # 最新安装包
│   └── ...
├── .github/workflows/build.yml  # 自动编译 APK
├── build.gradle / settings.gradle / gradle.properties
└── LICENSE
```

## 常见问题

- **收不到通知？** ①确认「通知访问权限」已开启；②「应用」页里该 App 开关是开的；③确认 App 没被系统杀掉（见「保活」）。
- **首页显示「服务没连上」？** 到 设置 → 通知 → 通知使用权，把「通知汇」关掉再打开一次，App 会自动重连。
- **想重置数据？** 系统设置 → 应用 → 通知汇 → 清除数据。
- **数据去哪了？** 全部在 App 私有本地数据库里，不上传网络。

## 许可证

本项目采用 [MIT License](LICENSE)。欢迎 Fork、Star、提 Issue / PR。

---

## English

**NotifyHub** is a privacy-first Android notification aggregator. It captures system notifications locally, filters them by app, auto-categorizes them (OTP / delivery / finance / social / promo / schedule / news / system), and lets you build custom categories with custom colors & fonts, per-category alerts, unread reminders, and per-source muting (mute a single group chat or friend). All data stays in the phone's local SQLite database — **nothing is uploaded**.

- **Download**: [Releases](https://github.com/MMJ1029/NotifyHub/releases) or <https://mmj1029.github.io/NotifyHub/>.
- **Try online** (no install): <https://mmj1029.github.io/NotifyHub/demo.html>.
- **Stack**: Kotlin · minSdk 24 · targetSdk 34 · `NotificationListenerService`.
