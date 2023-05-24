package io.github.jminchew97.routes

import io.github.jminchew97.models.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.github.jminchew97.storage.PostgresRestaurantStore

fun Route.restaurantRouting(appApi: PostgresRestaurantStore) {

    route("/api/restaurants") {

        // Restaurant specific routes
        get {
            call.respond(appApi.getRestaurants())
        }
        get("{id}") {
            val id = call.parameters["id"] ?: call.respond(
                status = HttpStatusCode.BadRequest, "bad"
            )

            val restaurant = appApi.getRestaurant(RestaurantId(id.toString()))

            call.respond(
                if (restaurant == null) {
                    call.respond(
                        HttpStatusCode(404, "Not found")
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

        // Menu routes
        get("/{r_id}/menus") {
            val rId = call.parameters["r_id"]

            call.respond(
                appApi.getMenusFromRestaurant(
                    RestaurantId(rId.toString())
                )
            )

        }
        post("/{id}/menus") {
            val restaurant_id = call.parameters["id"]
            if (restaurant_id == null) call.respond(HttpStatusCode.BadRequest)

            var createMenu: CreateMenu = call.receive<CreateMenu>()
            createMenu.restaurantId = RestaurantId(restaurant_id.toString())


            if (
                appApi.createMenu(
                    CreateMenu(
                        RestaurantId(
                            restaurant_id.toString()
                        ), createMenu.name
                    )
                )
            ) call.respond(HttpStatusCode.Created) else call.respond(HttpStatusCode.BadRequest)
        }
        delete("/{restaurant_id}/menus/{menu_id}") {
            val restaurantId: String? = call.parameters["restaurant_id"]
            val menuId: String? = call.parameters["menu_id"]

            if (restaurantId == null || menuId == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Either restaurant_id or menu_id missing from URI. Be sure to follow this format: /{restaurant_id}/menus/{menu_id} ."
                )
            }

            if (appApi.deleteMenu(RestaurantId(restaurantId.toString()), MenuId(menuId.toString()))) call.respond(
                HttpStatusCode.NoContent,
                "Deleted menu successfully"
            )
        }

    }
}
