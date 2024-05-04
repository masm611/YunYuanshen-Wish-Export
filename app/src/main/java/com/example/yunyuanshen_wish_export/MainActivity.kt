package com.example.yunyuanshen_wish_export

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.yunyuanshen_wish_export.ui.theme.YunYuanshenWishExportTheme

class YunYuanShenWebviewClient(private val context: MainActivity) : WebViewClient() {
    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        return super.shouldInterceptRequest(view, request)
    }

    @Deprecated("Deprecated in Kotlin")
    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        if (url.startsWith("https://webstatic.mihoyo.com/hk4e/event/e20190909gacha-v3/index.html")) { //判断地址是否是抽卡地址
            Looper.prepare()
            AlertDialog.Builder(context)
                .setTitle("截获到的地址")
                .setMessage(url)
                .setNegativeButton("复制到剪贴板") { _, _: Int ->
                    // 获取剪贴板管理器
                    val clipboardManager =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    // 创建一个剪贴板数据对象
                    val clipData = ClipData.newPlainText("label", url)
                    clipboardManager.setPrimaryClip(clipData)
                    //提示成功
                    //Looper.prepare()
                    Toast.makeText(context, "成功复制到剪贴板", Toast.LENGTH_SHORT).show()
                    //Looper.loop()
                }
                .setPositiveButton("OK") { dialog, _: Int ->
                    // 用户点击 OK 后的操作
                    dialog.dismiss() //关闭对话框
                }
                .show()

            Looper.loop()

        }
        return super.shouldInterceptRequest(view, url)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //保持竖屏，防止webview重置
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //提示用户正确使用方法，并叠甲
        AlertDialog.Builder(this)
            .setTitle("本工具使用方法")
            .setMessage("登录云原神并打开游戏，然后在抽卡祈福界面打开历史记录，程序就会自动推断出抽卡记录分析地址了\n\n注：\n本工具属于第三方工具，但是访问的是官方云原神网站，不采集任何信息\n仅供学习和交流使用，请在24小时内删除\n开发者:masm611@github.com")
            .setPositiveButton("OK") { dialog, _: Int ->
                // 用户点击 OK 后的操作
                dialog.dismiss() // 关闭对话框
            }
            .show()

        //设置webview
        val web = WebView(this)
        setContentView(web)
        web.webViewClient = YunYuanShenWebviewClient(this)
        web.loadUrl("https://ys.mihoyo.com/cloud/m/#/")
        //设置基本配置
        val settings = web.settings
        settings.javaScriptEnabled = true//允许js
        //settings.javaScriptCanOpenWindowsAutomatically = true//js和android交互，开了的话会有安全问题
        settings.allowFileAccess = true // 允许访问文件
        //settings.setAppCacheEnabled(true) //设置H5的缓存打开,默认关闭
        settings.domStorageEnabled = true//设置可以使用localStorage
    }
}
