package io.github.jminchew97.routes

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.jminchew97.config.PostgresConfig
import io.github.jminchew97.config.RestraConfig
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.github.jminchew97.models.RestaurantId
import io.github.jminchew97.storage.PostgresRestaurantStore
import io.github.jminchew97.HikariService
import io.github.jminchew97.models.CreateRestaurant

fun Route.restaurantRouting(postgresConfig: PostgresConfig) {

    route("/api/restaurant") {
        val appApi: PostgresRestaurantStore = PostgresRestaurantStore(
            HikariService(
                ConfigFactory.load().extract<RestraConfig>().postgres
            )
        )

        get {
            call.respond(appApi.getRestaurants())
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText( // if id null, respond with 'Bad request'
                "Bad request",
                status = HttpStatusCode.BadRequest
            )
            println("INSIDE ROUTE FUNCTION:")
            val restaurant = appApi.getRestaurant(RestaurantId(id))

            call.respond(
                if (restaurant == null) {
                    call.respond(
                        HttpStatusCode(
                            404,
                            "Resource not found"
                        )
                    )
                } else {
                    call.respond(restaurant)

                }
            )

        }
        post {
            val restaurantObj: CreateRestaurant = call.receive<CreateRestaurant>()

            appApi.createRestaurant(restaurantObj)
            call.respond(restaurantObj)
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Bad request",
                status = HttpStatusCode.BadRequest
            )
            if (appApi.deleteRestaurant(RestaurantId(id))) call.respond(HttpStatusCode(204, "Deleted resource"))

            call.respond(HttpStatusCode(404, "Resource not found"))
        }
    }
}
