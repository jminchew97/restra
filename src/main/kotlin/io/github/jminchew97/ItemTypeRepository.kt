package io.github.jminchew97

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.github.jminchew97.config.RestraConfig
import java.sql.PreparedStatement

class ItemTypeRepository {
    companion object {
        private val hks = HikariService(
            ConfigFactory.load().extract<RestraConfig>().postgres
        )
        val itemTypes = fetchItemTypesFromDb()
        fun fetchItemTypesFromDb():MutableSet<String> {
            val itemTypeList = hks.withConnection {
                conn ->

                val sql = "SELECT unnest(enum_range(NULL::item_type))"
                val prp = conn.prepareStatement(sql)

                val rs = prp.executeQuery()

                val ls:MutableList<String> = mutableListOf()
                while (rs.next()){
                    ls.add(rs.getString(1))
                }
                ls.toMutableSet()
            }
            return itemTypeList
        }

    }
}
