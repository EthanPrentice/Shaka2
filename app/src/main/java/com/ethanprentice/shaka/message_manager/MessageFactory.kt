package com.ethanprentice.shaka.message_manager

import com.ethanprentice.shaka.adt.Message
import com.ethanprentice.shaka.adt.EndpointInfo
import com.ethanprentice.shaka.adt.Endpoint
import com.ethanprentice.shaka.connection_manager.messages.ConnectionRequest
import com.ethanprentice.shaka.connection_manager.messages.ConnectionResponse
import com.ethanprentice.shaka.connection_manager.messages.InfoRequest
import com.ethanprentice.shaka.connection_manager.messages.InfoResponse
import org.json.JSONObject


class MessageFactory(private val msgManager: MessageManager) {

    /**
     * Gets a message based on the [Endpoint] it was sent to. ([Endpoint]s have a N:1 relationship to [Message] subclasses)
     * @return null if no [Message] subclass can be decided on, otherwise returns a [Message]
     */
    fun getMessage(messageString: String): Message? {
        val jsonMsg = JSONObject(messageString)
        if (!jsonMsg.has("endpointName")) {
            return null
        }
        val eInfo = EndpointInfo.fromString(jsonMsg.getString("endpointName"))

        val endpoint = msgManager.endpointManager.getEndpoint(eInfo.fullName)

        return when (endpoint?.type) {
            InfoRequest::class          -> InfoRequest.getFromJsonString(messageString)
            InfoResponse::class         -> InfoResponse.getFromJsonString(messageString)
            ConnectionRequest::class    -> ConnectionRequest.getFromJsonString(messageString)
            ConnectionResponse::class   -> ConnectionResponse.getFromJsonString(messageString)
            else -> null
        }
    }


}