package io.github.jminchew97

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.jminchew97.config.PostgresConfig
import java.sql.Connection

class HikariService(private val postgresConfig: PostgresConfig, private val isTestService:Boolean = false, private val url:String = "") {

    private val ds: HikariDataSource = createDataSource()

    fun <A> withConnection(f: (Connection) -> A): A {
        return ds.connection.use(f)
    }

    private fun createDataSource(): HikariDataSource {
        val config = HikariConfig()

        if (isTestService){ // If testcontainer, only needs db name and have tc in url
            config.jdbcUrl = url
            config.username = "restra"
            config.password = "restra"
            println("USING TEST CONTAINER URL-------------------========================================")
            return HikariDataSource(config)
        }

        config.jdbcUrl = "jdbc:postgresql://${postgresConfig.host}:${postgresConfig.port}/${postgresConfig.name}"
        config.username = postgresConfig.user
        config.password = postgresConfig.password
        return HikariDataSource(config)
    }
}
