package io.github.jminchew97.plugins


import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.jminchew97.HikariService
import io.github.jminchew97.config.RestraConfig
import io.github.jminchew97.routes.restaurantRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



// call routes here
fun Application.configureRouting() {
    val config = ConfigFactory.load().extract<RestraConfig>()

    routing {
        restaurantRouting(config.postgres)
        route("/"){
            get{
                call.respond("test worked")
            }
        }
    }

}
