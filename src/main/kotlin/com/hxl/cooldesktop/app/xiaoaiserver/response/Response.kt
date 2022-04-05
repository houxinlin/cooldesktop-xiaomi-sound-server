package com.hxl.cooldesktop.app.xiaoaiserver.response

import com.hxl.cooldesktop.app.xiaoaiserver.response.Speck

class Response {

    var to_speak: Speck = Speck()

    var action: String = "play_msg"

    var not_understand: Boolean = false

    var open_mic: Boolean = true
}