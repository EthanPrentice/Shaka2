package com.ethanprentice.shaka.message_manager

import android.util.Log
import com.ethanprentice.shaka.MainApp
import com.ethanprentice.shaka.adt.Message
import com.ethanprentice.shaka.connection_manager.CmMessageHandler
import com.ethanprentice.shaka.ui.UiMessageHandler

/**
 * Acts as a middleware so all components send messages to a single point and receive from a single point (other than messages received from external devices)
 * This reduces interdependencies between components
 *
 * @author Ethan Prentice
 */
object MessageManager {

    // MessageHandlers
    private val cmMsgHandler = CmMessageHandler(this)
    private val uiMsgHandler = UiMessageHandler(this)

    // holds all of our registered MessageHandlers and their respective Endpoints
    val endpointManager = EndpointManager()

    val msgFactory = MessageFactory(this)


    init {
        cmMsgHandler.register()
        uiMsgHandler.register()
    }


    /**
     * Redirects messages to their endpoint's MessageHandler
     * @param _message The message to be redirected
     */
    fun handleMessage(_message: Message) {
        Log.i(TAG, "received $_message")

        val handler = endpointManager.getHandler(_message.endpointName)
        handler?.handleMessage(_message)
    }

    private val TAG = this::class.java.canonicalName

}