package io.github.jminchew97.routes

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.github.jminchew97.storage.PostgresRestaurantStore
import io.github.jminchew97.models.*
import io.ktor.server.request.*


fun Route.menuRouting(appApi: PostgresRestaurantStore) {
    route("/api/menus") {
        get("/{id}") {
            val id: String? = call.parameters["id"]
            if (id == null) {
                call.respond(status = HttpStatusCode.BadRequest, "No menu id entered")
            }

            val menu = appApi.getMenu(MenuId(id.toString()))

            if (menu == null) {
                call.respond(status = HttpStatusCode.NotFound, "Menu not found")

            } else {
                call.respond(menu)
            }
        }
        post {
            val createMenu: CreateMenu = call.receive<CreateMenu>()

            appApi.createMenu(createMenu)
        }
        get{
            call.respond(HttpStatusCode.OK, appApi.getAllMenus())
        }
    }
}

