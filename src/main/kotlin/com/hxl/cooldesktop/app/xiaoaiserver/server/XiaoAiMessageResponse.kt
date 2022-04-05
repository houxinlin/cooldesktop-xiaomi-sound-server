package com.hxl.cooldesktop.app.xiaoaiserver.server

import com.hxl.cooldesktop.app.xiaoaiserver.response.XiaoAiResponse

class XiaoAiMessageResponse {
    companion object {
        private val CLOSE_SESSION_VALUE = arrayOf("退出", "关闭")
    }

    fun hasDefault(query: String): Any? {
        if (query.isBlank()) {
            return XiaoAiResponse().apply {
                this.response.not_understand = false
                this.response.to_speak.text = "你想说什么呢"
            }
        }
        if (CLOSE_SESSION_VALUE.contains(query)) {
            return XiaoAiResponse().apply {
                this.is_session_end=true
                this.response.to_speak.text = "下次见"
            }
        }
        return null
    }

    fun noConnection(): XiaoAiResponse {
        return XiaoAiResponse().apply {
            this.response.to_speak.text = "没有客户端链接，操作失败"
        }
    }

    fun makeResponse(message: String): XiaoAiResponse {
        return XiaoAiResponse().apply {
            this.response.not_understand = false
            this.response.to_speak.text = message
        }
    }

    fun failResponse(): XiaoAiResponse {
        return XiaoAiResponse().apply {
            this.response.to_speak.text = "请求失败"
        }
    }
}