package com.hxl.cooldesktop.app.xiaoaiserver.msg

enum class MessageType(var typeValue: Byte) {
    VOICE(1),
    HEARTBEAT(2),
    HEARTBEAT_REP(3)
}