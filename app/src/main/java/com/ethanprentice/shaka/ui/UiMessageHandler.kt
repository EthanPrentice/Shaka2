package com.ethanprentice.shaka.ui

import com.ethanprentice.shaka.TestActivity
import com.ethanprentice.shaka.adt.Message
import com.ethanprentice.shaka.connection_manager.messages.InfoResponse
import com.ethanprentice.shaka.message_manager.MessageHandler
import com.ethanprentice.shaka.message_manager.MessageManager

class UiMessageHandler(msgManager: MessageManager) : MessageHandler(msgManager) {

    override val handlerName = "ui"

    override fun register() {
        msgManager.endpointManager.registerHandler(this)
        msgManager.endpointManager.registerEndpoint(UiConfig.INFO_RSP_ENDPOINT)
    }

    override fun handleMessage(_message: Message) {
        when (_message.endpointName) {
            UiConfig.INFO_RSP_ENDPOINT.name -> handleInfoResponse(_message as InfoResponse)
        }
    }

    fun handleInfoResponse(infoRsp: InfoResponse) {
        val activity = ActivityManager.activityQueue.peek()
        if (activity is TestActivity) {
            activity.addDeviceInfo(infoRsp)
        }
    }
}