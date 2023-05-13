package io.github.jminchew97

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.jminchew97.config.RestraConfig
import io.github.jminchew97.models.RestaurantId
import io.ktor.server.application.*
import io.github.jminchew97.plugins.configureRouting
import io.github.jminchew97.plugins.configureSerialization
import io.ktor.http.*
import io.ktor.server.response.*
import io.github.jminchew97.storage.PostgresRestaurantStore

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSerialization()
    configureRouting()
}
