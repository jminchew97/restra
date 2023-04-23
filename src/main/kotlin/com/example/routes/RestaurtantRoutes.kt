package com.example.routes

import io.ktor.server.routing.*
import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.storage.*

fun Route.restaurantRouting() {
    route("/restaurant") {
        val appApi : InMemoryRestaurantStore = InMemoryRestaurantStore()

        get {
            call.respond(appApi.getRestaurants())
        }
        get("{id?}") {

        }
        post {
            val restaurantObj: Restaurant = call.receive<Restaurant>()
            println(restaurantObj)
            appApi.createRestaurant(restaurantObj)
            call.respondText("Restaurant created", status = HttpStatusCode.Created)
        }
        delete("{id?}") {

        }
    }
}