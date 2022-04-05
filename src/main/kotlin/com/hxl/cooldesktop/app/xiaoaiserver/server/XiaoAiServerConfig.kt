package com.hxl.cooldesktop.app.xiaoaiserver.server

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.TimeUnit

@Service
class XiaoAiServerConfig : InitializingBean {
    val xiaoAiSocketServer: XiaoAiSocketServer = XiaoAiSocketServer()

    companion object {
        private val log = LoggerFactory.getLogger(XiaoAiServerConfig::class.java)
    }

    private var messageQueue: SynchronousQueue<String> = SynchronousQueue()

    override fun afterPropertiesSet() {
        xiaoAiSocketServer.setMessageQueue(messageQueue)
        xiaoAiSocketServer.start()
    }

    fun resetPort(port: Int) {
        xiaoAiSocketServer.resetPort(port)
    }

    /**
     * 应用卸载
     */
    fun uninstall() {
        xiaoAiSocketServer.stop()
    }

    /**
     * 小米服务调用
     */
    fun sendMessage(query: String): Any {
        log.info("向客户端发送语音{}", query)
        val messageResponse = XiaoAiMessageResponse()
        val default = messageResponse.hasDefault(query)
        if (default != null) {
            return default
        }
        if (!xiaoAiSocketServer.isConnection()) {
            return messageResponse.noConnection()
        }
        if (xiaoAiSocketServer.writeMessageToClient(query)) {
            try {
                var message = messageQueue.poll(3, TimeUnit.SECONDS)
                log.info("用户响应{}", message)
                return messageResponse.makeResponse(message)
            } catch (e: Exception) {
                log.info(e.message)
            }
        }
        return messageResponse.failResponse()
    }
}