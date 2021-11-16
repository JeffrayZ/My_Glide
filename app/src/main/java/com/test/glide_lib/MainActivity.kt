package com.test.glide_lib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun glide_load(view: android.view.View) {
        findViewById<ImageView>(R.id.iv_glide).apply {
            // 官方的
            Glide.with(this@MainActivity).load("https://cn.bing.com/sa/simg/hpb/LaDigue_EN-CA1115245085_1920x1080.jpg").into(this);
        }
    }
    fun my_load(view: android.view.View) {
        findViewById<ImageView>(R.id.iv_my).apply {
            // 自己的
            com.glide.my.Glide.with(this@MainActivity).load("https://cn.bing.com/sa/simg/hpb/LaDigue_EN-CA1115245085_1920x1080.jpg").into(this)
        }

    }
}