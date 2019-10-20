package com.ethanprentice.shaka.adt

import android.util.Log
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.Socket
import kotlin.concurrent.thread

class ShakaSocket: Socket {

    constructor(): super()
    constructor(host: String, port: Int): super(host, port)


    fun read() {
        thread(start = true) {
            val reader = InputStreamReader(inputStream)

            try {
                val socketData = reader.readText()
                Log.v(TAG, "Received data: $socketData (port $localPort)")
            }
            catch(e: Exception) {
                Log.e(TAG, e.message, e)
            }

        }
    }

    private fun write(str: String) {
        thread(start = true) {
            val writer = OutputStreamWriter(outputStream)

            try {
                writer.write(str)
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
    }

}