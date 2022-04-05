package com.hxl.cooldesktop.app.xiaoaiserver.controller

import com.hxl.cooldesktop.app.xiaoaiserver.request.XiaoAiRequest
import com.hxl.cooldesktop.app.xiaoaiserver.server.XiaoAiServerConfig
import com.hxl.cooldesktop.app.xiaoaiserver.server.XiaoAiSocketServer
import com.hxl.cooldesktop.app.xiaoaiserver.utils.FileUtils
import org.springframework.web.bind.annotation.*
import javax.annotation.Resource


@RestController
@RequestMapping("/xiaoai")
class XiaoAiIndexController {
    @Resource
    lateinit var xiaoAiServerConfig: XiaoAiServerConfig

    /**
     * 小米服务推动
     */
    @PostMapping("/push")
    fun eventPush(@RequestBody body: XiaoAiRequest): Any {
        return xiaoAiServerConfig.sendMessage(body.query!!)
    }

    @GetMapping("/test/{n}")
    fun test(@PathVariable("n") value: String): Any {
        return xiaoAiServerConfig.sendMessage(value)
    }

    @GetMapping("/reset/port/{port}")
    fun resetPort(@PathVariable("port") port: Int) {
        xiaoAiServerConfig.resetPort(port)
    }

    @GetMapping("/reset/password")
    fun resetPassword() {
        FileUtils.resetPassword()
    }
    @GetMapping("/status")
    fun status(): Any {
        var xiaoAiSocketServer = xiaoAiServerConfig.xiaoAiSocketServer
        return mutableMapOf(
            "port" to xiaoAiSocketServer.getPort(),
            "state" to xiaoAiSocketServer.getState(),
            "password" to FileUtils.getPassword()
        )
    }
}

