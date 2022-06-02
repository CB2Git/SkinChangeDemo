package org.learn.skinchangedemo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

private class AttrItem2(val attr: String, val attrName: String, val attrType: String)

private class AttrView2(val view: View, val attrs: MutableList<AttrItem2> = mutableListOf()) {


}

class Demo3ThemeActivity : AppCompatActivity() {

    private lateinit var mLL: LinearLayout
    private val mChangSkinViews = mutableListOf<AttrView2>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo3_theme)
        mLL = findViewById(R.id.ll_add_view)
    }

    fun onChangeSkin(view: View) {
        //io操作，需要异步，这里省略了
        val file = File(getExternalFilesDir(null), "skin.apk")
        SkinLoader.instance.loadResource(this, file.absolutePath)
        refreshUI()
    }

    fun resetSkin(view: View) {
        SkinLoader.instance.reset()
        refreshUI()
    }

    fun refreshUI() {
        mChangSkinViews.clear()
        look(findViewById(R.id.root_view))
        mChangSkinViews.forEach {
            if (it.view is TextView) {
                it.attrs.forEach { attr ->
                    if (attr.attr == "textColor") {
                        //去皮肤包中寻找对应的资源
                        it.view.setTextColor(SkinLoader.instance.getTextColor(this, attr.attrName, attr.attrType))
                    } else if (attr.attr == "text") {
                        //去皮肤包中寻找对应的资源
                        it.view.text = SkinLoader.instance.getText(this, attr.attrName, attr.attrType)
                    }
                }
            }
        }
    }

    fun onAddView(view: View) {
        val textView = TextView(this)
        textView.setText(getString(R.string.test_string))
        textView.setTextColor(resources.getColor(R.color.skin_test_color))
        textView.setTag(R.id.skin_tag, "text=string/test_string|textColor=color/skin_test_color")

        mLL.addView(textView, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))
        textView.setOnClickListener {
            mLL.removeView(it)
        }
        refreshUI()
    }

    private fun look(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                look(view.getChildAt(i))
            }
        }
        var tag = view.tag
        if (tag == null) {
            tag = view.getTag(R.id.skin_tag)
        }
        if (tag == null || tag !is String) {
            return
        }
        val attrView2 = AttrView2(view)

        val attrItem = tag.split("|")
        attrItem.forEach {
            val attrInfo = it.split("=")
            val kvAttr = attrInfo[1].split("/")
            attrView2.attrs.add(AttrItem2(attrInfo[0], kvAttr[1], kvAttr[0]))
        }
        mChangSkinViews.add(attrView2)
    }
}