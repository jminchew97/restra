package io.github.jminchew97.routes

import io.github.jminchew97.models.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.github.jminchew97.storage.PostgresRestaurantStore
import io.github.jminchew97.utils.Conversions
import java.sql.SQLException
import kotlinx.uuid.UUID

fun Route.restaurantRouting(appApi: PostgresRestaurantStore) {

    route("/api/restaurants") {
        //region Restaurant Routes
        // Restaurant specific routes
        get {
            call.respond(appApi.getRestaurants())
        }
        get("{restaurant_id}") {
            val restaurantId = call.parameters["restaurant_id"]
            if (restaurantId.isNullOrBlank() || !UUID.isValidUUIDString(restaurantId)) {
                call.respond(HttpStatusCode.BadRequest, ResponseMessages.uuidNullOrImproper)
            } else {
                val restaurant = appApi.getRestaurant(
                    RestaurantId(
                        UUID(restaurantId)
                    )
                )
                if (restaurant == null) {
                    call.respond(
                        HttpStatusCode(404, "Not found")
                    )
                } else {
                    call.respond(restaurant)
                }
            }
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
        delete("/{restaurant_id}") {
            val restaurantId = call.parameters["restaurant_id"]
            if (restaurantId.isNullOrBlank() || !UUID.isValidUUIDString(restaurantId)) {
                call.respond(HttpStatusCode.BadRequest, ResponseMessages.uuidNullOrImproper)
            } else {
                call.respond(
                    appApi.deleteRestaurant(
                        RestaurantId(UUID(restaurantId))
                    )
                )
            }

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
            if (restaurantId.isNullOrBlank() || !UUID.isValidUUIDString(restaurantId)) {
                call.respond(HttpStatusCode.BadRequest, ResponseMessages.uuidNullOrImproper)
            } else {
                call.respond(
                    appApi.getMenusFromRestaurant(
                        RestaurantId(UUID(restaurantId))
                    )
                )
            }

        }
        post("/{restaurant_id}/menus") {
            val restaurantId = call.parameters["restaurant_id"]
            if (restaurantId.isNullOrBlank() || !UUID.isValidUUIDString(restaurantId)) call.respond(
                HttpStatusCode.BadRequest,
                ResponseMessages.uuidNullOrImproper
            )
            else {
                val menuNoRestaurantId: MenuReceive = call.receive<MenuReceive>()
                val restId = RestaurantId(UUID(restaurantId))
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
                ) {
                    call.respond(HttpStatusCode.Created)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

        }
        delete("/{restaurant_id}/menus/{menu_id}") {
            val restaurantIdParam: String? = call.parameters["restaurant_id"]
            val menuIdParam: String? = call.parameters["menu_id"]

            if (restaurantIdParam.isNullOrBlank() || !UUID.isValidUUIDString(restaurantIdParam)
                || menuIdParam.isNullOrBlank() || !UUID.isValidUUIDString(menuIdParam)
            ) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ResponseMessages.uuidNullOrImproper
                )
            } else {
                if (appApi.deleteMenu(
                        RestaurantId(UUID(restaurantIdParam)),
                        MenuId(UUID(menuIdParam))
                    )
                ) call.respond(
                    HttpStatusCode.NoContent,
                    "Deleted menu successfully"
                )
            }
        }
        put("/menus/{menu_id}") {
            val restaurantIdParam = call.parameters["restaurant_id"]
            val menuIdParam = call.parameters["menu_id"]
            // Use UpdateMenuReceive, to get JSON content for the new menu
            val updateMenuReceive = call.receive<UpdateMenuReceive>()

            if (restaurantIdParam.isNullOrBlank() || !UUID.isValidUUIDString(restaurantIdParam)
                || menuIdParam.isNullOrBlank() || !UUID.isValidUUIDString(menuIdParam)
            ) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ResponseMessages.uuidNullOrImproper
                )
            } else {
                // Combine the IDs from URI and also content from UpdateMenuReceive object
                val newMenu = UpdateMenu(
                    RestaurantId(UUID(restaurantIdParam)),
                    MenuId(UUID(menuIdParam)),
                    updateMenuReceive.name
                )

                if (newMenu.name.isEmpty()
                ) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Name parameter for resource cannot be empty. Make sure {restaurant_id} and {menu_id} in URI are digits only."
                    )
                }

                if (appApi.updateMenu(newMenu)) {
                    call.respond(
                        HttpStatusCode.OK,
                        "Menu updated successfully"
                    )
                } else // Menu record was not updated for other reason
                {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "Request could not be processed. Ensure menu attempting to request exists."
                    )
                }
            }
        }

        get("/menus/{menu_id}") {
            val menuId: String? = call.parameters["menu_id"]
            if (menuId.isNullOrBlank() || !UUID.isValidUUIDString(menuId)) {
                call.respond(status = HttpStatusCode.BadRequest, ResponseMessages.uuidNullOrImproper)
            } else {
                val menu = appApi.getMenu(MenuId(UUID(menuId)))
                if (menu == null) {
                    call.respond(status = HttpStatusCode.NotFound, "Menu not found")

                } else {
                    call.respond(menu)
                }
            }
        }
        //endregion

        //region Item Routes
        post("/{restaurant_id}/menus/{menu_id}/items") {
            val menuId = call.parameters["menu_id"]
            val restaurantId = call.parameters["restaurant_id"]
            if (menuId.isNullOrBlank() || !UUID.isValidUUIDString(menuId)
                || restaurantId.isNullOrBlank() || !UUID.isValidUUIDString(restaurantId)
            ) {
                call.respond(HttpStatusCode.BadRequest, ResponseMessages.uuidNullOrImproper)
            } else {
                val cir: CreateItemReceive = call.receive<CreateItemReceive>()
                val createItem = CreateItem(
                    RestaurantId(UUID(restaurantId)),
                    MenuId(UUID(menuId)),
                    cir.name,
                    cir.description,
                    Conversions.convertMoneyStringToCents(cir.price),
                    cir.itemType
                )
                if (appApi.createItem(createItem)) {
                    call.respond(
                        HttpStatusCode.Created,
                        "item created"
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Item not created"
                    )
                }
            }
        }

        get("/menus/items/{item_id}") {
            val itemId = call.parameters["item_id"]
            if (itemId.isNullOrBlank() || !UUID.isValidUUIDString(itemId)) {
                call.respond(HttpStatusCode.BadRequest, "Either empty, blank, or not valid UUID formats.")
            } else {
                val item = appApi.getItem(ItemId(UUID(itemId)))
                if (item == null) {
                    call.respond("Item is null.")
                } else {
                    call.respond(item)
                }
            }
            //endregion
        }
        get("/menus/items") {
            val items = appApi.getAllItems()
            call.respond(items)
        }
        put("/menus/items/{item_id}") {
            val itemId = call.parameters["item_id"]

            // Id Validation
            if (itemId.isNullOrBlank() || !UUID.isValidUUIDString(itemId)) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ResponseMessages.uuidNullOrImproper
                )
            } else {
                val itemWithoutId = call.receive<CreateItem>()
                val updateItem = UpdateItem(
                    ItemId(UUID(itemId)),
                    itemWithoutId.restaurantId,
                    itemWithoutId.menuId,
                    itemWithoutId.name,
                    itemWithoutId.description,
                    itemWithoutId.price,
                    itemWithoutId.itemType
                )
                if (appApi.updateItem(updateItem)) call.respond(
                    HttpStatusCode.OK,
                    "Item updated"
                ) else call.respond(
                    HttpStatusCode.NotModified
                )
            }
        }
        delete("/{restaurant_id}/menus/{menu_id}/items/{item_id}") {
            val restaurantIdParam = call.parameters["restaurant_id"]
            val menuIdParam = call.parameters["menu_id"]
            val itemIdParam = call.parameters["item_id"]
            if (
                restaurantIdParam.isNullOrBlank() || !UUID.isValidUUIDString(restaurantIdParam)
                || menuIdParam.isNullOrBlank() || !UUID.isValidUUIDString(menuIdParam) ||
                itemIdParam.isNullOrBlank() || !UUID.isValidUUIDString(itemIdParam)
            ) {
                call.respond(HttpStatusCode.BadRequest, "One of the required parameters are null in URI.")
            } else {
                val restaurantId = RestaurantId(UUID(restaurantIdParam))
                val menuId = MenuId(UUID(menuIdParam))
                val itemId = ItemId(UUID(itemIdParam))

                if (appApi.deleteItem(
                        itemId,
                        restaurantId,
                        menuId
                    )
                ){
                    call.respond(HttpStatusCode.Accepted)
                } else {
                    call.respond(HttpStatusCode.NotModified, "Unable to delete record.")
                }
            }
        }
        get("/menus/{menu_id}/items") {
            val idParam = call.parameters["menu_id"]
            if (idParam.isNullOrBlank() || !UUID.isValidUUIDString(idParam)){
                call.respond(HttpStatusCode.BadRequest, ResponseMessages.uuidNullOrImproper)
            } else {
                val menuId = UUID(idParam)
                val items = appApi.getItemsByMenu(MenuId(menuId))
                call.respond(items)
            }
        }
        get("/{restaurant_id}/menus/items") {
            val restId = call.parameters["restaurant_id"]
            if (restId.isNullOrBlank() || !UUID.isValidUUIDString(restId)){
                call.respond(
                    HttpStatusCode.BadRequest,
                    ResponseMessages.uuidNullOrImproper
                )
            }else {
                val items = appApi.getItemsByRestaurant(RestaurantId(UUID(restId)))
                call.respond(items)
            }
        }
    }
}
