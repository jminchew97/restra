package io.github.jminchew97

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.jminchew97.config.PostgresConfig
import java.sql.Connection

class HikariService(private val postgresConfig: PostgresConfig) {

    private val ds: HikariDataSource = createDataSource()

    fun getConnection(): Connection {
        return ds.connection
    }
    fun <A> withConnection(f: (Connection) -> A): A {
        return ds.connection.use(f)
    }
    fun testConnection() =  ds.connection.isValid(0)


    private fun createDataSource(): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:postgresql://${postgresConfig.host}:${postgresConfig.port}/${postgresConfig.name}"
        config.username = postgresConfig.user
        config.password = postgresConfig.password
        return HikariDataSource(config)
    }
}
