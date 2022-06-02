package org.learn.skinchangedemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun demo1Theme(view: View) {
        startActivity(Intent(this, Demo1ThemeActivity::class.java))
    }

    fun demo2Theme(view: View) {
        startActivity(Intent(this, Demo2ThemeActivity::class.java))
    }

    fun demo3Theme(view: View) {
        startActivity(Intent(this, Demo3ThemeActivity::class.java))
    }
}