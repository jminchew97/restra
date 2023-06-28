package io.github.jminchew97



import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.containers.PostgreSQLContainer
import kotlin.test.assertEquals

@Testcontainers
class MixedLifecycleTests {

    // will be started before and stopped after each test method
    @Container
    private val postgresqlContainer = PostgreSQLContainer("postgres:15.0").withDatabaseName("restra")
        .withUsername("restra")
        .withPassword("restra");

    @Test
    fun testContainerRunning() {
        assertEquals(true, postgresqlContainer.isRunning())

    }
}