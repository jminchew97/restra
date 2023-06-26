val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.20"
    id("io.ktor.plugin") version "2.2.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    //change this line when not in development
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

repositories {
    mavenCentral()
}
val hikaricp_version: String by project
val ehcache_version: String by project
dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.ehcache:ehcache:$ehcache_version")
    implementation("io.github.config4k:config4k:0.5.0")
    implementation("org.postgresql:postgresql:42.5.1")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.2.4")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
}
