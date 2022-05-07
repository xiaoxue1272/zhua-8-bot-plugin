package io.tiangou.utils

import java.io.File
import java.io.FileInputStream
import java.util.Properties

object Zhua8PropertiesUtils {
    private val propertiesBasePath : String = System.getProperty("user.dir") + File.pathSeparator + "zhua8bot" + File.pathSeparator

    fun loadProperties(childPath: String) : Properties {
        val properties = Properties();
        properties.load(FileInputStream(propertiesBasePath + childPath))
        return properties
    }
}