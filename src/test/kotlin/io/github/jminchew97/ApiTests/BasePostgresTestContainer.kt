package io.github.jminchew97.ApiTests

import io.github.jminchew97.HikariService
import io.github.jminchew97.config.PostgresConnectInfo
import io.github.jminchew97.storage.PostgresRestaurantStore
import kotlinx.uuid.UUID
import org.flywaydb.core.Flyway
import org.testcontainers.containers.PostgreSQLContainer

open class BasePostgresTestContainer {
    companion object {
        var postgresContainer: PostgreSQLContainer<*>
        var appApi: PostgresRestaurantStore
        val testRestaurantId = UUID("00000000-0000-0000-0000-000000000000")
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