package com.hxl.cooldesktop.app.xiaoaiserver.utils

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.io.path.notExists

object FileUtils {
    private const val USER_DIRECTORY_NAME = "xiaoai-config"
    private const val PORT_FILE_NAME = "port"
    private const val PASSWORD_FILE_NAME = "password"
    private const val DEFAULT_PORT = 8086
    fun loadLocalPort(): Int {
        createIfNotExists()
        try {
            return Files.readAllBytes(Paths.get(getUserHome(), USER_DIRECTORY_NAME, PORT_FILE_NAME)).decodeToString()
                .toInt()
        } catch (e: Exception) {

        }
        return DEFAULT_PORT
    }

    fun setLocalPort(port: Int): Int {
        createIfNotExists()
        Files.write(
            Paths.get(getUserHome(), USER_DIRECTORY_NAME, PORT_FILE_NAME),
            port.toString().toByteArray()
        )
        return port
    }

    private fun getUserHome(): String {
        return System.getProperty("user.home")
    }

    fun getPassword(): String {
        createIfNotExists()
        return Files.readAllBytes(Paths.get(getUserHome(), USER_DIRECTORY_NAME, PASSWORD_FILE_NAME)).decodeToString()
    }

    private fun randomPassword(): String {
        val list = "qwertyuiopasdfghjklzxcvbnm123456789QWERTYUIOPASDFGHJKLZXCVNM".toList()
        Collections.shuffle(list)
        return list.subList(0, 5).joinToString(separator = "")
    }

    fun resetPassword() {
        createIfNotExists()
        Files.write(Paths.get(getUserHome(), USER_DIRECTORY_NAME, PASSWORD_FILE_NAME), randomPassword().toByteArray())

    }


    private fun createIfNotExists() {
        val userHome = getUserHome()
        if (Paths.get(userHome, USER_DIRECTORY_NAME).notExists()) {
            Files.createDirectories(Paths.get(userHome, USER_DIRECTORY_NAME))
        }

        if (Paths.get(userHome, USER_DIRECTORY_NAME, PORT_FILE_NAME).notExists()) {
            Files.write(Paths.get(userHome, USER_DIRECTORY_NAME, PORT_FILE_NAME), DEFAULT_PORT.toString().toByteArray())
        }
        if (Paths.get(userHome, USER_DIRECTORY_NAME, PASSWORD_FILE_NAME).notExists()) {
            Files.write(Paths.get(userHome, USER_DIRECTORY_NAME, PASSWORD_FILE_NAME), randomPassword().toByteArray())
        }

    }

}