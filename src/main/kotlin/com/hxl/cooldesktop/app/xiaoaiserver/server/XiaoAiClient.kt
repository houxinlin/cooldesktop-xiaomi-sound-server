package com.hxl.cooldesktop.app.xiaoaiserver.server

import com.hxl.cooldesktop.app.xiaoaiserver.msg.MessageEncode
import com.hxl.cooldesktop.app.xiaoaiserver.msg.MessageType
import org.slf4j.LoggerFactory
import java.nio.channels.SocketChannel
import java.util.concurrent.RunnableScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.TimeUnit

class XiaoAiClient {
    companion object {
        private const val HEARTBEAT_SEND_INTERVAL: Long = 8 * 1000
        private val log = LoggerFactory.getLogger(XiaoAiClient::class.java)
    }

    var messageQueue: SynchronousQueue<String> = SynchronousQueue()
    private val scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1)
    private val sendHeartbeatMessageTask = { sendHeartbeatMessageRep() }
    private lateinit var messageEncode: MessageEncode
    private var clientSocket: SocketChannel? = null
    private var heartbeatScheduledFutureTask: RunnableScheduledFuture<*>? = null
    private var isConnectioned: Boolean = false
    fun runClient(socketChannel: SocketChannel) {
        if (clientSocket != null) {
            if (clientSocket!!.isConnected) {
                try {
                    clientSocket?.close()
                }catch (e:Exception){}
            }
        }
        this.clientSocket = socketChannel
        this.isConnectioned = true
        messageEncode = MessageEncode(socketChannel!!)
        //验证客户端身份
        if (!messageEncode.checkClientPassword()) {
            socketChannel?.close()
            log.info("客户端身份错误{}", socketChannel)
        }
        log.info("客户端连接成功{}", socketChannel)
        startHeartbeatRep()
        beginReadMessageFromClient()
        stopHeartbeat()
        isConnectioned = false
    }

    private fun stopHeartbeat() {
        heartbeatScheduledFutureTask?.run {
            this.cancel(true)
            scheduledThreadPoolExecutor.remove(sendHeartbeatMessageTask)
        }
    }

    private fun startHeartbeatRep() {
        heartbeatScheduledFutureTask = scheduledThreadPoolExecutor
            .scheduleAtFixedRate(
                sendHeartbeatMessageTask,
                0,
                HEARTBEAT_SEND_INTERVAL,
                TimeUnit.MILLISECONDS
            ) as RunnableScheduledFuture<*>
    }

    /**
     * 心跳回复
     */
    private fun sendHeartbeatMessageRep() {
        try {
            clientSocket?.run { this.write(messageEncode.createHeartbeatRepBuffer()) }
        } catch (e: Exception) {
            clientSocket?.close()
        }
    }

    fun writeMessageToClient(msg: String): Boolean {
        try {
            clientSocket!!.write(messageEncode!!.createVoiceMessageByteBuffer(msg))
            return true
        } catch (e: Exception) {
            log.info(e.message)
            clientSocket?.close()
        }
        return false
    }

    private fun beginReadMessageFromClient() {
        try {
            while (true) {
                val message = messageEncode.getMessageFromSocket()
                message?.run {
                    //收到客户端回复
                    if (message.messageType == MessageType.VOICE.typeValue) {
                        messageQueue.put(message.msg)
                    }
                }
            }
        } catch (e: Exception) {
            log.info(e.message)
        }
    }

    fun isConnection(): Boolean {
        return isConnectioned
    }
}