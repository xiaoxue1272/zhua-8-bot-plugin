
plugins {
    val kotlinVersion = "1.6.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.11.1"
}

group = "io.tiangou"
version = "0.0.3-release"

repositories {
    repositories {
        maven("https://maven.aliyun.com/repository/public/")
        mavenLocal()
        mavenCentral()
    }
}

dependencies {
    val hutoolVersion = "5.8.0.M4"
    val logbackVersion = "1.2.11"
    implementation("ch.qos.logback:logback-core:$logbackVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.jcraft:jsch:0.1.54")
}
