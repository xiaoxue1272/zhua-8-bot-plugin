package io.tiangou.utils

import io.tiangou.*
import io.tiangou.constants.Constants
import io.tiangou.enums.ErrorCodeEnum
import io.tiangou.expection.Zhua8BotException
import java.io.File
import java.io.FileInputStream
import java.io.Reader
import java.util.Properties

object Zhua8PropertiesUtils {
    private val configDir: String = "${Constants.USER_WORK_DIR}${File.separator}config${File.separator}zhua8bot"

    fun loadProperties(childPath: String): Map<String, String> {
        check(childPath.isNotEmpty())
        return getReader("$configDir${File.separator}$childPath")
            .readLines()
            .map {
                it.takeIf {
                    it.contains('=')
                } ?: throw Zhua8BotException(ErrorCodeEnum.SYSTEM_ERROR, "${childPath}配置文件格式不正确")
                it.split('=', ignoreCase = true, limit = 2).let {
                    it[0] to it[1]
                }
            }.toMap()
    }

    fun loadYaml(childPath: String): Properties {
        val properties = Properties()
        properties.load(FileInputStream(configDir + childPath))
        return properties
    }

    private fun getReader(path: String): Reader {
        try {
            return File(path).reader()
        } catch (e: Exception) {
            throw Zhua8BotException(ErrorCodeEnum.STREAM_ERROR, errorMessage = null, e)
        }
    }
}