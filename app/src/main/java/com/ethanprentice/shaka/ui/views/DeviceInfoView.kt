package com.ethanprentice.shaka.ui.views

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.*
import com.ethanprentice.shaka.adt.enums.ConnType
import com.ethanprentice.shaka.connection_manager.CmConfig
import com.ethanprentice.shaka.connection_manager.messages.ConnectionRequest
import com.ethanprentice.shaka.connection_manager.messages.InfoResponse
import com.ethanprentice.shaka.message_manager.MessageManager

/**
 * @author Ethan Prentice
 *
 * Displays device info that is received from an InfoResponse after scanning the network
 * @param context Activity context
 * @param infoRsp The response received by CM to be displayed
 */
class DeviceInfoView(context: Context, private val infoRsp: InfoResponse) : RelativeLayout(context) {

    // TODO: once we implement Spotify capabilities use this imageView to display the user's profile picture
    private val imageView = ImageView(context)
    private val textView = TextView(context)
    private val connBtnView = Button(context)


    init {
        setLayoutParams()

        // initialize textView
        val textViewParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textViewParams.addRule(ALIGN_PARENT_LEFT)
        textViewParams.addRule(CENTER_VERTICAL)

        textView.text = infoRsp.name
        textView.textSize = 18f
        textView.setTypeface(null, Typeface.BOLD)
        textView.layoutParams = textViewParams


        // initialize connBtn
        val btnParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 150)
        btnParams.addRule(ALIGN_PARENT_RIGHT)
        btnParams.addRule(CENTER_VERTICAL)

        connBtnView.text = "Connect"
        connBtnView.textSize = 16f
        connBtnView.layoutParams = btnParams
        connBtnView.setOnClickListener {
            // Connection type doesn't matter here since we're only sending it internally to CM to be resent to the target device
            // CM will decide the connection type
            val connReq = ConnectionRequest(infoRsp.ip, infoRsp.port, ConnType.CLIENT.name)
            connReq.endpointName = CmConfig.SEND_CONN_REQ_EP.name

            MessageManager.handleMessage(connReq)
        }

        // add subviews to view
        addView(textView)
        addView(connBtnView)

    }

    private fun setLayoutParams() {
        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(15, 15, 15, 15)

        layoutParams = params
    }

}