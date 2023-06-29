package io.github.jminchew97


import io.github.jminchew97.config.PostgresConfig
import io.github.jminchew97.models.CreateItem
import io.github.jminchew97.models.CreateRestaurant
import io.github.jminchew97.models.RestaurantId
import io.github.jminchew97.storage.PostgresRestaurantStore
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.containers.PostgreSQLContainer
import kotlin.test.assertEquals
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration

@Testcontainers
class MixedLifecycleTests {

     //will be started before and stopped after each test method
    @Container
    private val postgresqlContainer = PostgreSQLContainer("postgres:15.0").withDatabaseName("restra")
        .withUsername("restra").withPassword("restra").withExposedPorts(5432)

    @Test
    fun testContainerRunning() {
        assertEquals(true, postgresqlContainer.isRunning)

    }

    @Test
    fun testApi() {
        val flyway = Flyway.configure().dataSource(
            postgresqlContainer.getJdbcUrl(),
            postgresqlContainer.getUsername(),
            postgresqlContainer.getPassword()
        ).load()
        flyway.migrate()

        val appApi = PostgresRestaurantStore(
            HikariService(
                PostgresConfig(
                    "localhost",
                    32856,
                    "restra",
                    "test",
                    "test"
                ), isTestService = true,
                url =postgresqlContainer.getJdbcUrl()
            )
        )

        //Test restaurants
        assertEquals(
            true, appApi.createRestaurant(
                CreateRestaurant(
                    "frie chickies",
                    "123 address",
                    "ENTREE"
                )
            )
        )

    }
}