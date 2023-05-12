package io.github.jminchew97

import com.typesafe.config.ConfigFactory
import io.github.jminchew97.config.RestraConfig
import io.ktor.server.application.*
import io.github.jminchew97.plugins.configureRouting
import io.github.jminchew97.plugins.configureSerialization



fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {


    configureSerialization()
    configureRouting()
}
