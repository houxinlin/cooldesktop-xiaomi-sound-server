package com.hxl.cooldesktop.app.xiaoaiserver.msg


class Message {
    var messageType: Byte = MessageType.HEARTBEAT.typeValue
    var msgSize: Int = 0
    var msg: String = ""
    var timer: Long = 0L
}