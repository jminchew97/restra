//package io.github.jminchew97.ApiTests
//
//import io.github.jminchew97.models.CreateMenu
//import io.github.jminchew97.models.RestaurantId
//import org.junit.jupiter.api.*
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
//@TestClassOrder(ClassOrderer.OrderAnnotation::class)
//@Order(2)
//class TestMenuApiMethods : BasePostgresTestContainer() {
//    @Order(1)
//    @Test
//    fun CreateMenu() {
//        val createMenuResult = appApi.createMenu(
//            CreateMenu(RestaurantId(testRestaurantId),"Main Menu")
//        )
//        assert(createMenuResult)
//    }
//
//    @Order(2)
//    @Test
//    fun getMenusFromRestaurant() {
//        val menusFromRestaurant = appApi.getMenusFromRestaurant(RestaurantId(testRestaurantId))
//        assert(menusFromRestaurant.size == 1)
//    }
//
//}