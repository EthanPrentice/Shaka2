package com.ethanprentice.shaka.adt

import android.util.Log
import java.io.InputStreamReader
import java.io.InterruptedIOException
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import kotlin.concurrent.thread
import java.lang.Exception


/**
 * Class to handle operations and multithreading for [ServerSocket]s and their client [Socket]s
 * @param port The port to open the ServerSocket on
 *
 * @author Ethan Prentice
 */
class ShakaServerSocket(port: Int) : ServerSocket(port) {

    private var clientSocket: Socket? = null
    private val acceptLock = Object()

    override fun accept(): Socket? {
        soTimeout = ACCEPT_TIMEOUT

        try {

            thread(start = true) {
                try {
                    clientSocket = super.accept()
                    Log.i(TAG, "Accepted TCP connection on port $localPort with remote address ${clientSocket?.remoteSocketAddress}:${clientSocket?.port}")
                    synchronized(acceptLock) {
                        acceptLock.notifyAll()
                    }
                } catch (e: SocketException) {
                    clientSocket = null
                    Log.e(TAG, "SocketException: closing ServerSocket on port $localPort", e)
                    close()
                } catch (e: InterruptedIOException) {
                    clientSocket = null
                    Log.w(TAG, "No socket accepted in ${ACCEPT_TIMEOUT}ms.  Closing ServerSocket on port $localPort")
                    close()
                }
            }

        }
        catch(e: Exception) {
            Log.e(TAG, e.message, e)
        }

        return clientSocket
    }

    fun read() {
        thread(start = true) {

            synchronized(acceptLock) {
                if (clientSocket == null) {
                    Log.d(TAG, "Client has not been accepted. Waiting to read. (port: $localPort)")
                    acceptLock.wait()
                    Log.d(TAG, "Client has been accepted. Now reading from socket. (port: $localPort)")
                }
            }

            val inStream = InputStreamReader(clientSocket!!.getInputStream())

            try {
                val socketData = inStream.readText()
                Log.v(TAG, "Received data: $socketData (port $localPort)")
            }
            catch(e: Exception) {
                Log.e(TAG, e.message, e)
            }

        }
    }

    private fun write(str: String) {
        thread(start = true) {
            synchronized(acceptLock) {
                if (clientSocket == null) {
                    Log.d(TAG, "Client has not been accepted. Waiting to write. (port: $localPort)")
                    acceptLock.wait()
                    Log.d(TAG, "Client has been accepted. Now writing to socket. (port: $localPort)")
                }
            }

            val oStream = OutputStreamWriter(clientSocket!!.getOutputStream())

            try {
                oStream.write(str)
            }
            catch(e: Exception) {
                Log.e(TAG, e.message, e)
            }

        }
    }

    fun write(msg: SerializableMessage) {
        write(msg.toJsonString())
    }


    companion object {
        private val TAG = this::class.java.canonicalName

        private const val ACCEPT_TIMEOUT = 2000
    }

}