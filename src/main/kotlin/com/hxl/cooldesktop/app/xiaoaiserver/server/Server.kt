package com.hxl.cooldesktop.app.xiaoaiserver.server

interface Server {
    fun start()

    fun stop()

    fun resetPort(port: Int)

    fun getState(): String
}