
plugins {
    val kotlinVersion = "1.6.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.10.1"
}

group = "io.tiangou"
version = "0.0.3-release"


dependencies {
    val hutoolVersion = "5.8.0.M4"
    implementation("ch.qos.logback:logback-core:1.2.11")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("cn.hutool:hutool-cron:$hutoolVersion")
    implementation("com.jcraft:jsch:0.1.54")
    implementation("org.apache.commons:commons-compress:1.9")
}
