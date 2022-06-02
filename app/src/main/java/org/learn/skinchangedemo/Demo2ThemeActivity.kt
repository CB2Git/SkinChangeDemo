package org.learn.skinchangedemo

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.ViewCompat
import java.io.File


private const val TAG = "Demo2ThemeActivity"


private class AttrItem(val attrName: String, val resId: Int)

private class AttrView(val view: View, val attrs: MutableList<AttrItem> = mutableListOf()) {

    fun addAttr(attrName: String, resId: Int): AttrView {
        attrs.add(AttrItem(attrName, resId))
        return this
    }
}


class Demo2ThemeActivity : AppCompatActivity() {

    private lateinit var mLL: LinearLayout

    private val layoutFactory2 = object : LayoutInflater.Factory2 {

        val attrViews: MutableList<AttrView> = mutableListOf()

        override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
            return null
        }

        override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
            val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SkinSupport)
            val isEnable = obtainStyledAttributes.getBoolean(R.styleable.SkinSupport_enableSkin, false)
            obtainStyledAttributes.recycle()
            var createView: View? = null
            //如果控件支持换肤
            if (isEnable) {
                //调用系统方法创建控件
                createView = delegate.createView(parent, name, context, attrs)
                val attrView = AttrView(createView)
                for (i in 0 until attrs.attributeCount) {
                    val attributeName = attrs.getAttributeName(i)
                    //如果是支持换肤的属性
                    if (isSupportAttr(attributeName)) {
                        val attributeValue = attrs.getAttributeValue(i)
                        //# 直接写死的颜色 不处理
                        //?2130903258 ?colorPrimary 这样的 解析主题，找到id，再去找资源名称和类型
                        //@2131231208 @color/red 直接就是id，根据id找到资源名称和类型
                        if (attributeValue.startsWith("?")) {
                            val attrId = attributeValue.substring(1)
                            val resIdFromTheme = getResIdFromTheme(context, attrId.toInt())
                            if (resIdFromTheme > 0) {
                                attrView.attrs.add(AttrItem(attributeName, resIdFromTheme))
                            }
                        } else if (attributeValue.startsWith("@")) {
                            attrView.attrs.add(AttrItem(attributeName, attributeValue.substring(1).toInt()))
                        }
                    }
                }
                attrViews.add(attrView)
            }
            return createView
        }

        /**
         * 解析主题，找到资源id，其实就是方案一里面的方法
         */
        private fun getResIdFromTheme(context: Context, attrId: Int): Int {
            val typedValue = TypedValue()
            val success = context.theme.resolveAttribute(attrId, typedValue, true)
            //typedValue.resourceId 可能为0
            return typedValue.resourceId
        }

        private fun isSupportAttr(attrName: String): Boolean {
            if ("textColor" == attrName || "text" == attrName) {
                return true
            }
            return false
        }

        fun changeSkin(context: Context) {
            //这个是在Factory2中找到的所有支持换肤的控件
            attrViews.forEach {
                if (ViewCompat.isAttachedToWindow(it.view)) {
                    changAttrView(context, it)
                }
            }
        }

        fun changAttrView(context: Context, attrView: AttrView) {
            //将每一个换肤控件的属性进行应用
            attrView.attrs.forEach {
                if (attrView.view is TextView) {
                    if (it.attrName == "textColor") {
                        //去皮肤包中寻找对应的资源
                        attrView.view.setTextColor(SkinLoader.instance.getTextColor(context, it.resId))
                    } else if (it.attrName == "text") {
                        //去皮肤包中寻找对应的资源
                        attrView.view.text = SkinLoader.instance.getText(context, it.resId)
                    }
                }
            }
        }

        fun dynamicAddSkin(v: View): AttrView {
            val attrView = AttrView(v)
            attrViews.add(attrView)
            return attrView
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(layoutInflater, layoutFactory2)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo2_theme)
        mLL = findViewById(R.id.ll_add_view)

    }


    private fun changeSkin() {
        layoutFactory2.changeSkin(this)
    }

    fun onChangeSkin(view: View) {
        //io操作，需要异步，这里省略了
        val file = File(getExternalFilesDir(null), "skin.apk")
        SkinLoader.instance.loadResource(this, file.absolutePath)
        changeSkin()
    }

    fun resetSkin(view: View) {
        SkinLoader.instance.reset()
        changeSkin()
    }

    fun onAddView(view: View) {
        val textView = TextView(this)

        val addAttr = layoutFactory2.dynamicAddSkin(textView)
            .addAttr("text", R.string.test_string)
            .addAttr("textColor", R.color.skin_test_color)

        mLL.addView(textView, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))
        layoutFactory2.changAttrView(this, addAttr)

        textView.setOnClickListener {
            mLL.removeView(it)
        }
    }
}