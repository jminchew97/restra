package io.github.jminchew97

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.jminchew97.config.RestraConfig
import org.junit.Test
import kotlin.test.assertEquals

class CheckDatabaseConnection {
    @Test
    fun testConnectionIsValid(){
        val config = ConfigFactory.load().extract<RestraConfig>()

        val hks: HikariService = HikariService(config.postgres)

        assertEquals(true, hks.getConnection().isValid(0))

    }
}
