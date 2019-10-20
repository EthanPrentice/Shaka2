package com.ethanprentice.shaka.ui

import com.ethanprentice.shaka.adt.Endpoint
import com.ethanprentice.shaka.connection_manager.messages.InfoResponse

object UiConfig {


    /* ENDPOINTS */
    val INFO_RSP_ENDPOINT = Endpoint("com.ethanprentice.shaka/ui/info-response", InfoResponse::class)

}