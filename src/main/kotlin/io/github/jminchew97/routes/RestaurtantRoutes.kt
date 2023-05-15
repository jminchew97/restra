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
import io.github.jminchew97.models.UpdateRestaurant

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
            val id = call.parameters["id"] ?: call.respond(
                status = HttpStatusCode.BadRequest, "bad"
            )
            println("INSIDE ROUTE FUNCTION:")
            val restaurant = appApi.getRestaurant(RestaurantId(id.toString()))

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

            if (appApi.createRestaurant(restaurantObj)) call.respond(restaurantObj) else call.respond(
                status = HttpStatusCode.Created,
                restaurantObj
            )

        }
        put {
            val updateRest = call.receive<UpdateRestaurant>()
            if (appApi.updateRestaurant(updateRest)) call.respond(updateRest) else call.respond(
                status = HttpStatusCode.BadRequest,
                updateRest
            )

            call.respond(updateRest)
        }
        delete("{id}") {
            val id = call.parameters["id"]
            if (id == null) call.respond(status = HttpStatusCode.BadRequest, "bad")
            else if (appApi.deleteRestaurant(RestaurantId(id))) call.respond(HttpStatusCode(204, "Deleted resource"))
            else call.respond(HttpStatusCode(404, "Resource not found"))
        }
        delete {
            call.respond(
                status = HttpStatusCode.BadRequest,
                "In order to delete restaurant you must enter an id e.g /restaurant/{id} "
            )
        }
        delete("/") {
            call.respond(
                status = HttpStatusCode.BadRequest,
                "In order to delete restaurant you must enter an id e.g /restaurant/{id} "
            )
        }
    }
}
