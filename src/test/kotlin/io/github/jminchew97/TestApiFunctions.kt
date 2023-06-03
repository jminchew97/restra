package io.github.jminchew97


import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.jminchew97.config.RestraConfig
import io.github.jminchew97.models.CreateRestaurant
import io.github.jminchew97.models.RestaurantId
import io.github.jminchew97.storage.PostgresRestaurantStore
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

import kotlin.test.assertEquals

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TestApiFunctions {
    val config = ConfigFactory.load().extract<RestraConfig>()
    val appApi = PostgresRestaurantStore(HikariService(config.postgres))
    val flyway = Flyway.configure()
        .dataSource(
            "jdbc:tc:postgresql:latest://localhost:5432/restra",
            config.postgres.user,
            config.postgres.password
        ).load().migrate()

    @Order(1)
    @Test
    fun testCreateMultipleRestaurantsAndFetchThemAll() {

        val rs1 = CreateRestaurant("Dennys", "123 denny street", "American")
        val rs2 = CreateRestaurant("Pho Palace", "23 pho street", "Thai")
        appApi.createRestaurant(rs1)
        appApi.createRestaurant(rs2)

        val restaurants = appApi.getRestaurants()
        val converted = mutableListOf<CreateRestaurant>()

        for (restaurant in restaurants) {
            convertRestaurantToCreateRestaurant(restaurant)?.let { converted.add(it) }
        }
        assertEquals(setOf(rs1, rs2), converted.toSet(), "Check that contents of results is what we entered")
    }

    @Order(2)
    @Test
    fun testFetchRestaurantById() {
        val rs1 = CreateRestaurant("Dennys", "123 denny street", "American")

        val restaurant = appApi.getRestaurant(RestaurantId("1"))

        val convertedRestaurant = convertRestaurantToCreateRestaurant(restaurant)
        assertEquals(rs1, convertedRestaurant, "Verifying we receive proper restaurant")


    }
}
