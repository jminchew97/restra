package com.example.plugins

import com.example.routes.*
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
