package io.github.jminchew97.ApiTests

import io.github.jminchew97.models.*
import jdk.jfr.FlightRecorder.isInitialized
import kotlinx.uuid.UUID
import kotlinx.uuid.toJavaUUID
import org.junit.jupiter.api.ClassOrderer
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestClassOrder
import org.junit.jupiter.api.TestMethodOrder


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestClassOrder(ClassOrderer.OrderAnnotation::class)
@Order(1)
class TestRestaurantApiMethods : BasePostgresTestContainer() {
    companion object {
        lateinit var testRestId: UUID
        lateinit var testMenuId: UUID
        lateinit var testItemId: UUID
    }

    @Order(1)
    @Test
    fun testCreateRestaurant() {
        assert(
            appApi.createRestaurant(
                CreateRestaurant(
                    "Fried chickey",
                    "123 chikey street",
                    foodType = "Southern"
                )
            )
        )
    }

    @Order(2)
    @Test
    fun getAllRestaurants() {
        val results = appApi.getRestaurants()
        assert(results.isNotEmpty())
        //Set testRestaurantId so we can do the remaining tests
        testRestId = results.toMutableList()[0].id.unwrap

    }

    @Order(3)
    @Test
    fun testGetRestaurantById() {
        assert(appApi.getRestaurant(RestaurantId(testRestId)) != null)
    }

    @Order(4)
    @Test
    fun testUpdateRestaurant() {
        assert(
            appApi.updateRestaurant(
                UpdateRestaurant(
                    RestaurantId(testRestId),
                    "Dim Sum",
                    "123123 new address",
                    "Chinese"
                )
            )
        )
    }

    @Order(5)
    @Test
    fun createMenu() {
        assert(
            appApi.createMenu(
                CreateMenu(
                    RestaurantId(testRestId),
                    "Main menu"
                )
            )
        )
    }

    //TODO getMenus
    @Order(6)
    @Test
    fun getAllMenus() {
        val results = appApi.getAllMenus()
        assert(results.isNotEmpty())
        //Set test menu id for further tests
        testMenuId = results.toMutableList()[0].id.unwrap
    }

    //TODO getMenu
    @Order(7)
    @Test
    fun getMenu() {
        assert(
            appApi.getMenu(MenuId(testMenuId)) != null
        )
    }

    //TODO getMenu
    @Order(8)
    @Test
    fun getMenusFromRestaurant() {
        assert(
            appApi.getMenusFromRestaurant(RestaurantId(testRestId)).isNotEmpty()
        )
    }

    //TODO updateMenu
    @Order(9)
    @Test
    fun updateMenu() {
        assert(
            appApi.updateMenu(
                UpdateMenu(
                    RestaurantId(testRestId),
                    MenuId(testMenuId),
                    "New Menu Name"
                )
            )
        )
        assert(
            !appApi.updateMenu(
                UpdateMenu(
                    RestaurantId(testRestId),
                    MenuId(testMenuId),
                    ""
                )
            )
        )
    }
    @Order(10)
    @Test
    fun createItem() {
        assert(
            appApi.createItem(
                CreateItem(
                    RestaurantId(testRestId),
                    MenuId(testMenuId),
                    "Cheese Stick",
                    "A cheezzy stick.",
                    Cents(453),
                    ItemType.APPETIZER
                )
            )
        )
    }

}