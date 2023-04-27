package io.github.jminchew97

import com.typesafe.config.ConfigFactory
import io.github.jminchew97.config.PostgresConfig
import io.github.jminchew97.config.RestraConfig
import io.ktor.server.application.*
import io.github.jminchew97.plugins.configureRouting
import io.github.jminchew97.plugins.configureSerialization
import io.github.config4k.extract
import io.ktor.network.sockets.*
import java.sql.Connection

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val config = ConfigFactory.load().extract<RestraConfig>()
    val hks: HikariService = HikariService(config.postgres)
    val conn: Connection = hks.getConnection()
    println(conn.isValid(0))
    configureSerialization()
    configureRouting()
}
