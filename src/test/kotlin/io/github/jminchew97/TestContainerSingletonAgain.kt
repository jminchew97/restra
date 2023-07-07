package io.github.jminchew97
import io.github.jminchew97.config.PostgresConnectInfo
import io.github.jminchew97.models.CreateRestaurant
import io.github.jminchew97.models.RestaurantId
import io.github.jminchew97.models.UpdateRestaurant
import io.github.jminchew97.storage.PostgresRestaurantStore
import kotlinx.uuid.UUID
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.ClassOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.testcontainers.containers.PostgreSQLContainer
open class TestContainerSingletonAgain {
    companion object {
        var postgresContainer:PostgreSQLContainer<*>
        var appApi:PostgresRestaurantStore
        init {
            postgresContainer = PostgreSQLContainer("postgres:15.0")
                .withDatabaseName("restra")
                .withUsername("restra")
                .withPassword("restra")
            postgresContainer.start()

            Flyway.configure().dataSource(
                postgresContainer.jdbcUrl,
                postgresContainer.username,
                postgresContainer.password
            ).load().migrate()

            appApi = PostgresRestaurantStore(
                HikariService(
                    PostgresConnectInfo(
                        postgresContainer.jdbcUrl,
                        postgresContainer.username,
                        postgresContainer.password
                    )
                )
            )
        }
    }
}


class ImplementContainer: TestContainerSingletonAgain(){
    companion object{
        lateinit var testRestaurant:UUID
    }
    @Order(1)
    @Test
    fun isRunning(){
        assert(postgresContainer.isRunning)
    }

    @Test
    fun testCreateAndGetAll(){
        assert(
            appApi.createRestaurant(
            CreateRestaurant(
                "Fried chickey",
                "123 chikey street",
                foodType = "Southern"
            )
        ))
        assert(appApi.getRestaurants().size == 1)
        testRestaurant = appApi.getRestaurants().toMutableList()[0].id.unwrap

    }
    @Test
    fun testGetRestaurantById(){
        assert(appApi.getRestaurant(RestaurantId(testRestaurant)) != null)
    }
    @Test
    fun testUpdateRestaurant(){
        assert(appApi.updateRestaurant(
            UpdateRestaurant(
                RestaurantId(testRestaurant),
                "Dim Sum",
                "123123 new address",
                "Chinese"
            )
        ))
    }
    @Test
    fun test3(){
        println(postgresContainer.containerId)
    }
}