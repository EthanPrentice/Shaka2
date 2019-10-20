package com.ethanprentice.shaka.message_manager

import com.ethanprentice.shaka.adt.Message


/**
 * Represents a receiver / handler that MessageManager can send messages to
 * This keeps the message logic within components and allows the MessageManager to dynamically register handlers / endpoints
 *
 * @property handlerName Used to compare handlers to prevent registering duplicates and for processing endpointName strings
 */
abstract class MessageHandler(protected val msgManager: MessageManager) : Comparable<MessageHandler> {

    abstract val handlerName: String

    /**
     * Registers the MessageHandler and it's Endpoints with MessageManager
     */
    abstract fun register()

    /**
     * Receives messages from the MessageManager to be processed by the component
     * @param _message The message sent to the handler by MessageManager
     */
    abstract fun handleMessage(_message: Message)

    /**
     * Compares the [MessageHandler]'s based on [handlerName] equivalence
     */
    override fun compareTo(other: MessageHandler): Int {
        return handlerName.compareTo(other.handlerName)
    }

    /**
     * @return true if [handlerName]'s are equal, false otherwise
     */
    override fun equals(other: Any?): Boolean {
        return if (other is MessageHandler) {
            handlerName == other.handlerName
        }
        else {
            false
        }
    }

}