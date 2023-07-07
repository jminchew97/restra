//package io.github.jminchew97
//import io.github.jminchew97.config.PostgresConnectInfo
//import io.github.jminchew97.models.CreateRestaurant
//import io.github.jminchew97.models.UpdateRestaurant
//import io.github.jminchew97.storage.PostgresRestaurantStore
//import org.flywaydb.core.Flyway
//import org.junit.After
//import org.junit.jupiter.api.Test
//import org.testcontainers.containers.PostgreSQLContainer
//
//open class SingletonTestContainer{
//    companion object {
//        var postgresContainer:PostgreSQLContainer<*>
//        var appApi:PostgresRestaurantStore
//        init{
//            postgresContainer = PostgreSQLContainer("postgres:15.0")
//                .withDatabaseName("restra")
//                .withUsername("restra")
//                .withPassword("restra")
//            postgresContainer.start()
//            Flyway.configure().dataSource(
//                postgresContainer.jdbcUrl,
//                postgresContainer.username,
//                postgresContainer.password
//            ).load().migrate()
//            appApi = PostgresRestaurantStore(
//                HikariService(
//                    PostgresConnectInfo(
//                        postgresContainer.jdbcUrl,
//                        postgresContainer.username,
//                        postgresContainer.password
//                    )
//                )
//            )
//        }
//    }
//
//}
//
//class TestApi : SingletonTestContainer() {
//    @Test
//    fun testRestaurantsCrud() {
//        println("Container info\n" + postgresContainer.containerInfo)
//        //Create restaurant
//        assert(
//            appApi.createRestaurant(
//                CreateRestaurant(
//                    "Fried chickey",
//                    "123 chikey street",
//                    foodType = "Southern"
//                )
//            )
//        )
//        //Get all restaurants
//        assert(appApi.getRestaurants().size == 1)
//        val restaurant = appApi.getRestaurants().toMutableList()[0]
//        //Get specific restaurant
//        assert(
//            appApi.getRestaurant(restaurant.id) != null
//        )
//        //Update restaurant
//        assert(
//            appApi.updateRestaurant(UpdateRestaurant(
//                restaurant.id,
//                "Dim Sum",
//                "1453 Dim sum Street",
//                "Chinese Cuisine"
//            ))
//        )
//        //Delete restaurant
//        assert(appApi.deleteRestaurant(restaurant.id))
//    }
//
//    @Test
//    fun testMenusCrud() {
//        println("Container info\n" + postgresContainer.containerInfo)
//        assert(
//            appApi.createRestaurant(
//                CreateRestaurant(
//                    "Fried chickey",
//                    "123 chikey street",
//                    foodType = "Southern"
//                )
//            )
//        )
//    }
//
//}