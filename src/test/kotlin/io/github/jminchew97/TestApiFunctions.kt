package io.github.jminchew97


import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.jminchew97.config.PostgresConfig
import io.github.jminchew97.config.RestraConfig
import io.github.jminchew97.storage.PostgresRestaurantStore
import org.flywaydb.core.Flyway
import org.junit.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import java.sql.ResultSet
import kotlin.test.assertEquals


class TestApiFunctions {

    val config = ConfigFactory.load().extract<RestraConfig>()
    val appApi = PostgresRestaurantStore(HikariService(config.postgres))
    val flyway = Flyway.configure()
        .dataSource(
            "jdbc:tc:postgresql:latest://localhost:5432/restra",
            config.postgres.user,
            config.postgres.password
        ).load().migrate()

    @Test
    fun getAllRestauraunts() {

        println(appApi.getAllMenus())

        assertEquals(true, true)


    }
}
