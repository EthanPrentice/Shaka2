package com.ethanprentice.shaka

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ethanprentice.shaka.connection_manager.ConnectionManager
import com.ethanprentice.shaka.ui.ActivityManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityManager.activityQueue.push(this)


        // also opens udp socket
        ConnectionManager.isServer = true

        val intent = Intent(this, TestActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        ConnectionManager.closeAll()
        ActivityManager.activityQueue.pop()

        super.onDestroy()
    }
}
