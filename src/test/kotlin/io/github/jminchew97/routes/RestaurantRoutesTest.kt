package io.github.jminchew97.routes

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.jminchew97.HikariService
import io.github.jminchew97.config.RestraConfig
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import io.github.jminchew97.routes.*
import io.github.jminchew97.storage.PostgresRestaurantStore

class RestaurantRoutesTest {

    @Test
    fun testGetRestaurants() {
        //Implement database tests
    }

    @Test
    fun testCreateRestaurant() {
        //TODO test create restaurant


    }

    @Test
    fun testGetRestaurant() {
        //TODO test create restaurant
    }

    @Test
    fun testDeleteRestaurant() {
        //TODO test create restaurant
    }
}
