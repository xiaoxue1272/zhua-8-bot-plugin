package io.tiangou.constants
class Constants {
    companion object {

        const val EMPTY_STRING: String = ""

        @JvmStatic
        val OPERATION_REGEX = Regex("/\\w*")

        /**
         * shell连接的声明周期前缀
         * 值true false
         */
        @JvmStatic
        val LIFE_CYCLE_FINISH_PREFIX = Regex("-d\\w*")

        /**
         * shell连接的引擎执行方式
         * 值sync Async
         * 异步执行时,指定的shell声明周期永远为一次性 也就是true
         */
        @JvmStatic
        val ENGINE_EXECUTE_TYPE_PREFIX = Regex("-e\\w*")

        /**
         * shell连接使用的客户端标识
         */
        @JvmStatic
        val USE_CLIENT_PREFIX = Regex("-c\\w*")

        /**
         * 前半个方括号
         */
        const val SQUARE_BRACKET_PREFIX : String = "["

        /**
         * 后半个方括号
         */
        const val SQUARE_BRACKET_SUFFIX : String = "]"

        /**
         * 后半个方括号
         */
        const val COMMA : String = ","

        /**
         * ssh客户端未指定时的默认名称
         */
        const val DEFAULT_CLIENT_FLAG : String = "default"

        /**
         * 换行符
         */
        const val NEW_LINE_DELIMITER : String = "\n"

        /**
         * 回车符
         */
        const val ENTER_DELIMITER : String = "\r"

        /**
         * 默认工作目录
         */
        @JvmStatic
        val USER_WORK_DIR : String = System.getProperty("user.dir")
    }
}