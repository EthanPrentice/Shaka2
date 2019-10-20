package com.ethanprentice.shaka.connection_manager

import android.util.Log
import com.ethanprentice.shaka.adt.Message
import com.ethanprentice.shaka.adt.ShakaServerSocket
import com.ethanprentice.shaka.adt.enums.ConnType
import com.ethanprentice.shaka.connection_manager.messages.ConnectionRequest
import com.ethanprentice.shaka.connection_manager.messages.ConnectionResponse
import com.ethanprentice.shaka.connection_manager.messages.InfoRequest
import com.ethanprentice.shaka.connection_manager.messages.InfoResponse
import com.ethanprentice.shaka.information_manager.InfoManager
import com.ethanprentice.shaka.message_manager.MessageHandler
import com.ethanprentice.shaka.message_manager.MessageManager
import com.ethanprentice.shaka.tasks.SendUdpMessage
import java.net.InetAddress

/**
 * Handles messages that are redirected to ConnectionManager by MessageManager
 *
 * @author Ethan Prentice
 */
class CmMessageHandler(msgManager: MessageManager) : MessageHandler(msgManager) {

    override val handlerName = "connection-manager"

    /**
     * Registers all [ConnectionManager] related endpoints with the [MessageManager] so [Message]s can be routed here
     */
    override fun register() {
        msgManager.endpointManager.registerHandler(this)
        msgManager.endpointManager.registerEndpoint(CmConfig.CONN_REQ_ENDPOINT)
        msgManager.endpointManager.registerEndpoint(CmConfig.CONN_RSP_ENDPOINT)
        msgManager.endpointManager.registerEndpoint(CmConfig.INFO_REQ_ENDPOINT)
        msgManager.endpointManager.registerEndpoint(CmConfig.SEND_CONN_REQ_EP)
    }


    override fun handleMessage(_message: Message) {
        when (_message.endpointName) {
            CmConfig.INFO_REQ_ENDPOINT.name -> handleInfoReq(_message as InfoRequest)
            CmConfig.CONN_REQ_ENDPOINT.name -> handleConnReq(_message as ConnectionRequest)
            CmConfig.CONN_RSP_ENDPOINT.name -> handleConnRsp(_message as ConnectionResponse)
            CmConfig.SEND_CONN_REQ_EP.name  -> sendConnReq(_message as ConnectionRequest)
        }
    }

    /**
     * Sends an InfoResponse to let the requester know that this device is also running Shaka on the same network
     */
    private fun handleInfoReq(infoReq: InfoRequest) {
        val address = ConnectionManager.getDeviceIp().hostAddress
        val port = UdpListenerService.port

        if (port == null) {
            Log.e(TAG, "Could not send the InfoResponse, UdpListenerService must be running!")
            return
        }

        // TODO: use Spotify name instead of device name
        val deviceName = android.os.Build.MANUFACTURER + " - " + android.os.Build.MODEL

        val displayName = InfoManager.userInfo?.displayName

        val message = InfoResponse(address, port, displayName ?: deviceName , "placeholder_url.png")
        SendUdpMessage(InetAddress.getByName(infoReq.ip), infoReq.port, message).execute()
    }

    /**
     * Receives a ConnectionRequest internally to be redirected externally by ConnectionManager
     */
    private fun sendConnReq(connReq: ConnectionRequest) {
        val targetAddr = connReq.ip
        val targetPort = connReq.port

        val message = ConnectionRequest(ConnectionManager.getDeviceIp().hostAddress, UdpListenerService.port!!, ConnType.CLIENT.name)

        // Device is requesting to connect to the server, so it is not a server
        ConnectionManager.isServer = false

        SendUdpMessage(InetAddress.getByName(targetAddr), targetPort, message).execute()
    }

    /**
     * Receives a ConnectionRequest externally and sends back a ConnectionResponse
     * Later we need to implement a user prompt so connection is accepted by both parties
     */
    private fun handleConnReq(connReq: ConnectionRequest) {
        // TODO: Authenticate / prompt user to accept the connection request instead of automatically accepting
        val address = ConnectionManager.getDeviceIp().hostAddress
        val port = UdpListenerService.port

        if (port == null) {
            Log.e(TAG, "Could not send the ConnectionResponse, UdpListenerService must be running!")
            return
        }

        val socket: ShakaServerSocket = ConnectionManager.openTcpSocket()
        socket.accept()

        val message = ConnectionResponse(address, port, true, socket.localPort)
        SendUdpMessage(InetAddress.getByName(connReq.ip), connReq.port, message).execute()
    }


    /**
     *
     */
    private fun handleConnRsp(connRsp: ConnectionResponse) {
        val port = UdpListenerService.port

        if (port == null) {
            Log.e(TAG, "Could not send the ConnectionResponse, UdpListenerService must be running!")
            return
        }

        if (connRsp.accepted) {
            if (connRsp.tcpPort == null) {
                Log.e(TAG, "ConnectionResponse is invalid.  Cannot have a null tcpPort when accepted is true.")
            }
            else {
              ConnectionManager.openClientSocket(connRsp.ip, connRsp.tcpPort)
            }
        }
        else {
            // TODO: Show user connection rejection with a ui prompt
        }
    }

    companion object {
        private val TAG = this::class.java.canonicalName
    }

}