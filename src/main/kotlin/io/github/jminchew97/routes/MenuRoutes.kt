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

        post {
            val createMenu: CreateMenu = call.receive<CreateMenu>()

            appApi.createMenu(createMenu)
        }
        get{
            call.respond(HttpStatusCode.OK, appApi.getAllMenus())
        }
    }
}

