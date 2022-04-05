package com.hxl.cooldesktop.app.xiaoaiserver.response

class XiaoAiResponse {
    val version: String = "1.0"

    var is_session_end: Boolean = false

    val response: Response = Response()

    val session_attributes: SessionAttributes = SessionAttributes()
}