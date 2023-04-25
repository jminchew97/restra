package io.github.jminchew97.plugins


import io.github.jminchew97.routes.restaurantRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



// call routes here
fun Application.configureRouting() {
    routing {
        restaurantRouting()
        route("/"){
            get{
                call.respond("test worked")
            }
        }
    }

}
