
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
    val logbackVersion = "1.2.11"
    implementation("ch.qos.logback:logback-core:$logbackVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("cn.hutool:hutool-cron:$hutoolVersion")
    implementation("com.jcraft:jsch:0.1.54")
}
