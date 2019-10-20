package com.ethanprentice.shaka

import android.app.Application
import android.content.Context
import com.ethanprentice.shaka.connection_manager.ConnectionManager
import com.ethanprentice.shaka.information_manager.InfoManager
import com.ethanprentice.shaka.message_manager.MessageManager
import com.ethanprentice.shaka.ui.UiMessageHandler

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        var application: Application? = null
            private set

        val context: Context
            get() = application!!.applicationContext
    }
}