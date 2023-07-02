package io.github.jminchew97


import io.github.jminchew97.config.PostgresConfig
import io.github.jminchew97.config.PostgresConnectInfo
import io.github.jminchew97.models.CreateRestaurant
import io.github.jminchew97.storage.PostgresRestaurantStore
import io.ktor.client.plugins.api.*
import io.ktor.server.plugins.contentnegotiation.*
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.containers.PostgreSQLContainer
import kotlin.test.assertEquals
import org.flywaydb.core.Flyway


@Testcontainers
class ApiTest {

    //will be started before and stopped after each test method
    @Container
    private val postgresqlContainer = PostgreSQLContainer("postgres:15.0").withDatabaseName("restra")
        .withUsername("restra").withPassword("restra")

    @Test
    fun testContainerRunning() {
        assertEquals(true, postgresqlContainer.isRunning)

    }

    @Test
    fun testApi() {


        val appApi = PostgresRestaurantStore(
            HikariService(
                PostgresConnectInfo(
                    postgresqlContainer.getJdbcUrl(),
                    "restra",
                    "restra"
                )
            )
        )

        val flyway = Flyway.configure().dataSource(
            postgresqlContainer.getJdbcUrl(),
            postgresqlContainer.getUsername(),
            postgresqlContainer.getPassword()
        ).load()

        flyway.migrate()

        assertEquals(
            true,
            appApi.createRestaurant(
                CreateRestaurant(
                    "Fried chickey",
                    "123 chikey street",
                    foodType = "ENTREE"
                )
            )
        )

        assertEquals(
            1,
            appApi.getRestaurants().size,

        )
        appApi.getRestaurants()
    }
}