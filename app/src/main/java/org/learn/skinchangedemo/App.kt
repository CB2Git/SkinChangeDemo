package org.learn.skinchangedemo

import android.app.Application
import android.widget.Toast
import java.io.File

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val file = File(getExternalFilesDir(null), "skin.apk")
        if (!file.exists()) {
            val open = resources.assets.open("skin.apk")
            val outputStream = file.outputStream()
            open.copyTo(outputStream)
            outputStream.close()
            open.close()
        }

        Toast.makeText(this, "解压完成", Toast.LENGTH_SHORT).show()
    }
}