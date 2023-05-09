package io.github.jminchew97.config

data class RestraConfig(
    val postgres: PostgresConfig
    // add more configs later on
)
data class PostgresConfig(
    val host: String,
    val port: Int,
    val name: String,
    val user: String,
    val password: String,
)
// add more configs later on
