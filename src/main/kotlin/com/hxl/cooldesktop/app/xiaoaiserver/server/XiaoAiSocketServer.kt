package com.hxl.cooldesktop.app.xiaoaiserver.server

import com.hxl.cooldesktop.app.xiaoaiserver.utils.FileUtils
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.util.concurrent.SynchronousQueue

class XiaoAiSocketServer : Server {
    companion object {
        private val log = LoggerFactory.getLogger(XiaoAiSocketServer::class.java)
        private const val RUNNING_STATE = "运行中,等待推送语音"
        private const val WAIT_CLIENT_STATE = "等待客户的连接"
        private const val NO_RUNNING_STATE = "未运行，出现错误"
    }

    private var serverSocket: ServerSocketChannel? = null
    private var isConnectioned: Boolean = false
    private var isRunning: Boolean = false
    private var serverState = NO_RUNNING_STATE


    private val xiaoAiClient: XiaoAiClient = XiaoAiClient()

    @Volatile
    private var acceptClientRequest: Boolean = true


    private fun createServerSocket(port: Int) {
        try {
            serverSocket?.close()
            serverSocket = ServerSocketChannel.open()
            log.info("小爱服务将绑定端口:{}", port)
            serverSocket!!.bind(InetSocketAddress(port), 1)
            Thread(this::acceptClient).start()
        } catch (e: Exception) {
            log.info(e.message)
        }
    }

    private fun startServerSocket() {
        createServerSocket(FileUtils.loadLocalPort())
    }

    private fun stopServerSocket() {
        try {
            acceptClientRequest = false
            serverSocket?.run { this.close(); }
        } catch (e: Exception) {
        }
    }

    /**
     * 准备接收客户端链接
     */
    private fun acceptClient() {
        while (true) {
            try {
                serverState = WAIT_CLIENT_STATE
                val client = serverSocket!!.accept()
                serverState = RUNNING_STATE
                xiaoAiClient.runClient(client)
            } catch (e: Exception) {
                log.info("读取数据错误{}", e.message)
                stopServerSocket()
            }
        }
    }

    fun writeMessageToClient(msg: String): Boolean {
        return xiaoAiClient.writeMessageToClient(msg)
    }


    override fun getState(): String {
        return serverState
    }

    fun getPort(): String {
        return FileUtils.loadLocalPort().toString()
    }


    fun isConnection(): Boolean {
        return xiaoAiClient.isConnection()
    }

    fun setMessageQueue(messageQueue: SynchronousQueue<String>) {
        this.xiaoAiClient.messageQueue = messageQueue
    }

    override fun resetPort(port: Int) {
        FileUtils.setLocalPort(port)
        stopServerSocket()
        startServerSocket()
    }

    override fun start() {
        startServerSocket()
    }

    override fun stop() {
        stopServerSocket()
    }
//    enum class SocketState(desc: String) {
//        RUNNING("运行中"),
//        WAIT_CLIENT_CONNECTION("等待客户端连接")
//    }

}