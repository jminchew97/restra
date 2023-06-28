package io.github.jminchew97.routes

import io.github.jminchew97.models.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.github.jminchew97.storage.PostgresRestaurantStore
import io.github.jminchew97.utils.Conversions
import io.github.jminchew97.utils.isDigit
import java.sql.SQLException
import java.util.*

fun Route.restaurantRouting(appApi: PostgresRestaurantStore) {

    route("/api/restaurants") {
        //region Restaurant Routes
        // Restaurant specific routes
        get {
            call.respond(appApi.getRestaurants())
        }
        get("{restaurant_id}") {
            val restaurantId = call.parameters["restaurant_id"] ?: call.respond(
                status = HttpStatusCode.BadRequest, "bad"
            )
            try {
                UUID.fromString(restaurantId.toString())
            } catch (e: Exception) {
                call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
            }

            val restaurant = appApi.getRestaurant(RestaurantId(UUID.fromString(restaurantId.toString())))
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
        delete("{restaurant_id}") {
            val restaurantId = call.parameters["restaurant_id"]
            if (restaurantId != null) {
                try {
                    UUID.fromString(restaurantId)
                } catch (e: Exception) {
                    call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
                }

                try {
                    appApi.deleteRestaurant(RestaurantId(UUID.fromString(restaurantId)))
                } catch (ex: SQLException) {
                    println(ex.message)
                    call.respond(status = HttpStatusCode.InternalServerError, "An error occurred within the server")
                }
            } else call.respond(status = HttpStatusCode.BadRequest, "bad")

            call.respond(HttpStatusCode(404, "Resource not found"))
        }
        delete {
            call.respond(
                status = HttpStatusCode.BadRequest,
                "In order to delete restaurant you must enter an id e.g /restaurant/{id} "
            )
        }
        //endregion

        //region Menu Routes
        // Menu routes
        get("/{restaurant_id}/menus") {
            val restaurantId = call.parameters["restaurant_id"]
            try {
                UUID.fromString(restaurantId)
            } catch (e: Exception) {
                call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
            }

            call.respond(
                appApi.getMenusFromRestaurant(
                    RestaurantId(UUID.fromString(restaurantId))
                )
            )
        }
        post("/{restaurant_id}/menus") {
            val restaurantId = call.parameters["restaurant_id"]
            if (restaurantId == null) call.respond(HttpStatusCode.BadRequest)
            try {
                UUID.fromString(restaurantId)
            } catch (e: Exception) {
                call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
            }

            val menuNoRestaurantId: MenuReceive = call.receive<MenuReceive>()

            val restId = RestaurantId(UUID.fromString(restaurantId))
            val createMenu = CreateMenu(
                restId,
                menuNoRestaurantId.name
            )


            if (
                appApi.createMenu(
                    CreateMenu(
                        restId,
                        createMenu.name
                    )
                )
            ) call.respond(HttpStatusCode.Created) else call.respond(HttpStatusCode.BadRequest)
        }
        delete("/{restaurant_id}/menus/{menu_id}") {
            val restaurantIdParam: String? = call.parameters["restaurant_id"]
            val menuIdParam: String? = call.parameters["menu_id"]

            if (restaurantIdParam == null || menuIdParam == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Either restaurant_id or menu_id missing from URI. Be sure to follow this format: /{restaurant_id}/menus/{menu_id} ."
                )
            }
            try {
                UUID.fromString(restaurantIdParam)
                UUID.fromString(menuIdParam)
            } catch (e: Exception) {
                call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
            }

            if (appApi.deleteMenu(
                    RestaurantId(UUID.fromString(restaurantIdParam)),
                    MenuId(UUID.fromString(menuIdParam))
                )
            ) call.respond(
                HttpStatusCode.NoContent,
                "Deleted menu successfully"
            )
        }
        put("/menus/{menu_id}") {
            val restaurantIdParam = call.parameters["restaurant_id"]
            val menuIdParam = call.parameters["menu_id"]
            // Use UpdateMenuReceive, to get JSON content for the new menu
            val updateMenuReceive = call.receive<UpdateMenuReceive>()

            try {
                UUID.fromString(restaurantIdParam)
                UUID.fromString(menuIdParam)
            } catch (e: Exception) {
                call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
            }

            // Combine the IDs from URI and also content from UpdateMenuReceive object
            val newMenu = UpdateMenu(
                RestaurantId(
                    UUID.fromString(restaurantIdParam)
                ),
                MenuId(
                    UUID.fromString(menuIdParam)
                ),
                updateMenuReceive.name
            )

            // Verify name is not empty and that the restaurant_id and menu_id are digits
            if (newMenu.name == ""
            ) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Name parameter for resource cannot be empty. Make sure {restaurant_id} and {menu_id} in URI are digits only."
                )
            }

            if (appApi.updateMenu(newMenu)) call.respond(
                HttpStatusCode.OK,
                "Menu updated successfully"
            ) else // Menu record was not updated for other reason
                call.respond(
                    HttpStatusCode.NotFound,
                    "Request could not be processed. Ensure menu attempting to request exists."
                )
        }
        get("/menus/{menu_id}") {
            val menuId: String? = call.parameters["menu_id"]
            if (menuId == null) {
                call.respond(status = HttpStatusCode.BadRequest, "No menu id entered")
            }
            try {
                UUID.fromString(menuId)
            } catch (e: Exception) {
                call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
            }

            val menu = appApi.getMenu(MenuId(UUID.fromString(menuId)))

            if (menu == null) {
                call.respond(status = HttpStatusCode.NotFound, "Menu not found")

            } else {
                call.respond(menu)
            }
        }
        //endregion

        //region Item Routes
        post("/{restaurant_id}/menus/{menu_id}/items") {
            val menuId = call.parameters["menu_id"]
            val restaurantId = call.parameters["restaurant_id"]
            if (menuId == null || restaurantId == null) {
                call.respond(HttpStatusCode.BadRequest, "Parameter in URI is null.")
            }

            try {
                UUID.fromString(menuId)
                UUID.fromString(restaurantId)
            } catch (e: Exception) {
                call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
            }

            val cir: CreateItemReceive = call.receive<CreateItemReceive>()

            val createItem = CreateItem( //Convert CreateRecievedItem into CreateItem
                RestaurantId(UUID.fromString(restaurantId)),
                MenuId(UUID.fromString(menuId)),
                cir.name,
                cir.description,
                Conversions.convertMoneyStringToCents(cir.price),
                cir.itemType

            )

            if (appApi.createItem(createItem)) call.respond(
                HttpStatusCode.Created,
                "item created"
            ) else call.respond(
                HttpStatusCode.BadRequest,
                "Item not created"
            )
        }

        get("/menus/items/{item_id}") {
            val itemId = call.parameters["item_id"]
            if (itemId == null) {
                call.respond(HttpStatusCode.BadRequest, "Parameter in URI is null.")
            } else {
                try {
                    UUID.fromString(itemId)
                } catch (e: Exception) {
                    call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
                }

                val item = appApi.getItem(ItemId(UUID.fromString(itemId)))
                if (item == null) call.respond("Item is null.") else call.respond(item)
            }
            //endregion
        }
        get("/menus/items") {
            val items = appApi.getAllItems()
            call.respond(items)
        }
        put("/menus/items/{item_id}") {
            val idParam = call.parameters["item_id"]

            // Id Validation
            if (idParam == null) call.respond(
                HttpStatusCode.BadRequest,
                "ItemId parameter in URI either null or not a digit."
            )
            try {
                UUID.fromString(idParam)
            } catch (e: Exception) {
                call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
            }


            val itemWithoutId = call.receive<CreateItem>()

            val updateItem = UpdateItem(
                ItemId(UUID.fromString(idParam)),
                itemWithoutId.restaurantId,
                itemWithoutId.menuId,
                itemWithoutId.name,
                itemWithoutId.description,
                itemWithoutId.price,
                itemWithoutId.itemType
            )
            if (appApi.updateItem(updateItem)) call.respond(HttpStatusCode.OK, "Item updated") else call.respond(
                HttpStatusCode.NotModified
            )
        }
        delete("/{restaurant_id}/menus/{menu_id}/items/{item_id}") {
            val restaurantIdParam = call.parameters["restaurant_id"]
            val menuIdParam = call.parameters["menu_id"]
            val itemIdParam = call.parameters["item_id"]
            if (
                restaurantIdParam == null || menuIdParam == null || itemIdParam == null
            ) call.respond(HttpStatusCode.BadRequest, "One of the required parameters are null in URI.")

            try {
                UUID.fromString(restaurantIdParam)
                UUID.fromString(menuIdParam)
                UUID.fromString(itemIdParam)
            } catch (e: Exception) {
                call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
            }

            val restaurantId = RestaurantId(
                UUID.fromString(restaurantIdParam)
            )
            val menuId = MenuId(
                UUID.fromString(menuIdParam)
            )
            val itemId = ItemId(
                UUID.fromString(itemIdParam)
            )

            if (appApi.deleteItem(
                    itemId,
                    restaurantId,
                    menuId
                )
            )
                call.respond(HttpStatusCode.Accepted)
        }
        get("/menus/{menu_id}/items") {
            val idParam = call.parameters["menu_id"]

            try {
                UUID.fromString(idParam)
            } catch (e: Exception) {
                call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
            }

            val menuId: UUID = UUID.fromString(idParam)

            // Id Validation
            if (idParam == null) call.respond(HttpStatusCode.BadRequest, "MenuId parameter in URI is null.")

            val items = appApi.getItemsByMenu(
                MenuId(menuId)
            )

            call.respond(items)
        }
        get("/{restaurant_id}/menus/items") {
            val idParam = call.parameters["restaurant_id"]

            try {
                UUID.fromString(idParam)
            } catch (e: Exception) {
                call.respond("Error converting ID from URI into UUID. Make sure the ID in the URI is in valid UUID format.")
            }

            val restaurantId: UUID = UUID.fromString(idParam)
            // Id Validation
            if (idParam == null) call.respond(
                HttpStatusCode.BadRequest,
                "RestaurantId parameter in URI either null or not a digit."
            )
            else if (idParam.toInt() < 0) call.respond(
                HttpStatusCode.BadRequest,
                "RestaurantId parameter in URI cannot be negative."
            )

            val items = appApi.getItemsByRestaurant(RestaurantId(restaurantId))

            call.respond(items)
        }
    }
}
