package com.ethanprentice.shaka.connection_manager

import com.ethanprentice.shaka.adt.Endpoint
import com.ethanprentice.shaka.adt.Message
import com.ethanprentice.shaka.connection_manager.messages.ConnectionRequest
import com.ethanprentice.shaka.connection_manager.messages.ConnectionResponse
import com.ethanprentice.shaka.connection_manager.messages.InfoRequest

/**
 * Used to dynamically be able to change configuration information related to [ConnectionManager]
 */
object CmConfig {

    /* MISC */
    val SERVER_PORT_RANGE = 8000..8100


    /* ENDPOINTS */
    val CONN_REQ_ENDPOINT = Endpoint("com.ethanprentice.shaka/connection-manager/connection-request",  ConnectionRequest::class)
    val CONN_RSP_ENDPOINT = Endpoint("com.ethanprentice.shaka/connection-manager/connection-response", ConnectionResponse::class)
    val INFO_REQ_ENDPOINT = Endpoint("com.ethanprentice.shaka/connection-manager/info-request",        InfoRequest::class)

    val SEND_CONN_REQ_EP = Endpoint("com.ethanprentice.shaka/connection-manager/send-message/connection-request", ConnectionRequest::class)

}