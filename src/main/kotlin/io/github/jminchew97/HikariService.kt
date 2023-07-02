package io.github.jminchew97

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.jminchew97.config.PostgresConfig
import io.github.jminchew97.config.PostgresConnectInfo
import java.sql.Connection

class HikariService(private val postgresConfig:PostgresConnectInfo) {

    private val ds: HikariDataSource = createDataSource()

fun <A> withConnection(f: (Connection) -> A): A {
    return ds.connection.use(f)
}
    private fun createDataSource(): HikariDataSource {
        val config = HikariConfig()

        config.jdbcUrl = postgresConfig.jdbcUrl
        config.username = postgresConfig.user
        config.password = postgresConfig.password
        return HikariDataSource(config)
    }
}


