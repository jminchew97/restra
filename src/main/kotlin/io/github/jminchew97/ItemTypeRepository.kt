package io.github.jminchew97

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.jminchew97.config.RestraConfig

class ItemTypeRepository {
    companion object {
        private val hks = HikariService(
            ConfigFactory.load().extract<RestraConfig>().postgres
        )

        fun fetchItemTypesFromDb():

    }
}
