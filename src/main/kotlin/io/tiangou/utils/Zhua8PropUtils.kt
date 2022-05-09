package io.tiangou.utils

import io.tiangou.constants.Constants
import java.io.File
import java.io.FileInputStream
import java.util.Properties

object Zhua8PropertiesUtils {
    private val propertiesBasePath : String = Constants.USER_WORK_DIR + File.separator + "config" + File.separator + "zhua8bot" + File.separator

    fun loadProperties(childPath: String) : Properties {
        val properties = Properties()
        properties.load(FileInputStream(propertiesBasePath + childPath))
        return properties
    }
}