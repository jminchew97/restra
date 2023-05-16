package io.github.jminchew97.plugins


import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.jminchew97.HikariService
import io.github.jminchew97.config.RestraConfig
import io.github.jminchew97.routes.menuRouting
import io.github.jminchew97.routes.restaurantRouting
import io.github.jminchew97.storage.PostgresRestaurantStore
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



// call routes here
fun Application.configureRouting(appApi: PostgresRestaurantStore) {
    val config = ConfigFactory.load().extract<RestraConfig>()

    routing {
        restaurantRouting(appApi)
        menuRouting(appApi)
        route("/"){
            get{
                call.respond("Working")
            }
        }
    }

}
