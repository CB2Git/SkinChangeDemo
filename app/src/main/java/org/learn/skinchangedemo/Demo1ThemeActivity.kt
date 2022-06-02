package org.learn.skinchangedemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.learn.skinchangedemo.utils.getSp
import org.learn.skinchangedemo.utils.getThemeColor
import org.learn.skinchangedemo.utils.getThemeColor2
import org.learn.skinchangedemo.utils.putSp


class Demo1ThemeActivity : AppCompatActivity() {

    private lateinit var mLL: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if ("default" != getSp(this, "theme")) {
            setTheme(R.style.Theme_Style1)
        }
        setContentView(R.layout.activity_demo1_theme)
        mLL = findViewById(R.id.ll_add_view)
    }

    fun onChangeTheme(view: View) {
        toggleTheme()
        recreate()
    }

    private fun toggleTheme() {
        if ("default" == getSp(this, "theme")) {
            putSp(this, "theme", "")
        } else {
            putSp(this, "theme", "default")
        }
    }

    fun onChangeTheme2(view: View) {
        toggleTheme()
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        overridePendingTransition(0, 0) //不设置进入退出动画
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private var plan = 0

    fun onAddView(view: View) {

        val textView = TextView(this)
        textView.text = "动态添加的控件"
        if (plan % 2 == 0) {
            textView.setTextColor(
                getThemeColor(
                    this,
                    R.attr.theme_sub_color,
                    Color.BLACK
                )
            )
        } else {
            textView.setTextColor(
                getThemeColor2(
                    this,
                    R.attr.theme_sub_color,
                    Color.BLACK
                )
            )
        }
        plan++
        mLL.addView(
            textView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }
}