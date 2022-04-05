package com.hxl.cooldesktop.app.xiaoaiserver.controller

import com.hxl.cooldesktop.app.xiaoaiserver.server.XiaoAiServerConfig
import com.hxl.cooldesktop.app.xiaoaiserver.server.XiaoAiSocketServer
import com.hxl.cooldesktop.app.xiaoaiserver.utils.FileUtils
import com.hxl.cooldesktop.app.xiaoaiserver.utils.HtmlUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.thymeleaf.context.Context
import javax.annotation.Resource
import javax.servlet.http.HttpServletResponse

@RestController
class IndexController {
    @Resource
    lateinit var xiaoAiServerConfig: XiaoAiServerConfig

    @GetMapping("/")
    fun index(response: HttpServletResponse) {
        response.contentType = "text/html"
        response.characterEncoding = "utf-8"
        val context = Context()
        context.setVariable("port", xiaoAiServerConfig.xiaoAiSocketServer.getPort())
        context.setVariable("password", FileUtils.getPassword())
        response.writer.append(HtmlUtils.createIndex(context))
    }
}