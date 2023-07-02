package io.github.jminchew97.config

import java.sql.JDBCType

data class RestraConfig(
    val postgres: PostgresConfig
    // add more configs later on
)
data class PostgresConnectInfo(
    val jdbcUrl:String,
    val user:String,
    val password:String
)
data class PostgresConfig (
    val port: Int,
    val host: String,
    val name:String,
    val user: String,
    val password: String

) {
    fun toConnectInfo():PostgresConnectInfo{
        return PostgresConnectInfo(
            "jdbc:postgresql://${host}:$port/$name",
            user,
            password
        )
    }
}


