package io.github.jminchew97

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.jminchew97.config.RestraConfig
import org.junit.Test
import kotlin.test.assertEquals

class CheckDatabaseConnection {
    @Test
    fun testConnectionIsValid() {
        val hks = HikariService(
            ConfigFactory.load().extract<RestraConfig>().postgres)

        // Test that we can get a valid connection to our docker-compose postgres service
        // must have docker compose up and running for test to work: $ docker-compose up
        assertEquals(true, hks.testConnection())

    }
}
