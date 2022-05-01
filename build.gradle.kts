plugins {
    val kotlinVersion = "1.6.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.10.1"
}

group = "io.tiangou"
version = "0.0.1"

dependencies {
    implementation("ch.qos.logback:logback-parent:1.2.11")
}

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
