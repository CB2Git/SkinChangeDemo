package org.learn.skinchangedemo

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.util.Log
import androidx.core.content.ContextCompat

private const val TAG = "SkinLoader"

class SkinLoader {

    companion object {
        val instance = SkinLoader()
    }

    private var resource: Resources? = null

    private var skinPkgName: String? = null

    fun reset() {
        resource = null
        skinPkgName = null
    }

    fun loadResource(context: Context, skinPath: String) {
        try {
            val packageArchiveInfo = context.packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
            if (packageArchiveInfo == null) {
                Log.w(TAG, "loadResource: app load fail")
                return
            }
            skinPkgName = packageArchiveInfo.packageName


            val assetManager = AssetManager::class.java.newInstance()
            val method = AssetManager::class.java.getMethod("addAssetPath", String::class.java)
            method.invoke(assetManager, skinPath)

            resource = Resources(assetManager, context.resources.displayMetrics, context.resources.configuration)
        } catch (e: Exception) {
            Log.e(TAG, "loadResource: ", e)
        }
    }

    fun getTextColor(context: Context, attrName: String, attrType: String): Int {
        val resourceId = resource?.getIdentifier(attrName, attrType, skinPkgName) ?: 0
        if (resourceId <= 0) {
            return ContextCompat.getColor(context, context.resources.getIdentifier(attrName, attrType, context.packageName))
        }
        //获取插件工程的资源
        return resource!!.getColor(resourceId)
    }

    fun getText(context: Context, attrName: String, attrType: String): String {
        val resourceId = resource?.getIdentifier(attrName, attrType, skinPkgName) ?: 0
        if (resourceId <= 0) {
            return context.getString(context.resources.getIdentifier(attrName, attrType, context.packageName))
        }
        //获取插件工程的资源
        return resource!!.getString(resourceId)
    }

    fun getTextColor(context: Context, redId: Int): Int {
        val identifier = getIdentifier(context, redId)
        if (resource == null || identifier <= 0) {
            return ContextCompat.getColor(context, redId)
        }
        return resource!!.getColor(identifier)
    }

    fun getText(context: Context, redId: Int): String {
        //找到插件工程的对应资源id
        val identifier = getIdentifier(context, redId)

        if (resource == null || identifier <= 0) {
            return context.getString(redId)
        }
        //获取插件工程的资源
        return resource!!.getString(identifier)
    }

    private fun getIdentifier(context: Context, redId: Int): Int {
        //主工程资源id->资源名字、类型->插件包中的资源id
        //R.color.black
        //black
        val resourceEntryName = context.resources.getResourceEntryName(redId)
        //color
        val resourceTypeName = context.resources.getResourceTypeName(redId)
        return resource?.getIdentifier(resourceEntryName, resourceTypeName, skinPkgName) ?: 0
    }
}