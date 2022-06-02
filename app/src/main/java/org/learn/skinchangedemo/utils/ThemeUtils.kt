package org.learn.skinchangedemo.utils

import android.content.Context
import android.util.TypedValue

/**
 * 获取主题属性的资源id
 */
fun getThemeColor(context: Context, attr: Int, defaultColor: Int): Int {
    val obtainStyledAttributes = context.theme.obtainStyledAttributes(intArrayOf(attr))
    val redIds = IntArray(obtainStyledAttributes.indexCount)
    for (i in 0 until obtainStyledAttributes.indexCount) {
        val type = obtainStyledAttributes.getType(i)
        redIds[i] =
                //这个用来保证获取到的资源是颜色
            if (type >= TypedValue.TYPE_FIRST_COLOR_INT && type <= TypedValue.TYPE_LAST_COLOR_INT) {
                obtainStyledAttributes.getColor(i, defaultColor)
            } else {
                defaultColor
            }
    }
    obtainStyledAttributes.recycle()
    return redIds[0]
}

//# 直接写死的颜色 不处理
//?2130903258 ?colorPrimary 这样的 解析主题，找到id，再去找资源名称和类型
//@2131231208 @color/red 直接就是id，根据id找到资源名称和类型
//Log.i(TAG, "${it.getAttributeName(i)} = ${it.getAttributeValue(i)}")

/**
 * 获取主题属性的资源id，方案二
 */
fun getThemeColor2(context: Context, attr: Int, defaultColor: Int): Int {
    val typedValue = TypedValue()
    val success = context.theme.resolveAttribute(
        attr,
        typedValue,
        true
    )

    //TypedValue详解
    //针对#ffffff 这种制定值，data就位这个值，resourceId为0
    //针对@color/black，data为这个值，resourceId为 R.color.black
    //针对@drawable/XXX,data不能直接用，resourceId为 R.drawable.XXX，type为TypedValue.TYPE_STRING,string字段为文件名

    return if (success) {
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT
            && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT
        ) {
            typedValue.data
        } else {
            defaultColor
        }
    } else {
        defaultColor
    }
}