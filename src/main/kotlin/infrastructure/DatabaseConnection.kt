package com.infrastructure

import io.ktor.server.application.ApplicationEnvironment
import org.jetbrains.exposed.sql.Database
import java.sql.Connection
import java.sql.DriverManager

interface DatabaseConnection {
    val database : Database
    val dbConnection : Connection
}

class DatabaseConnectionImpl(val env: ApplicationEnvironment) : DatabaseConnection {
    override val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
    override val dbConnection: Connection = connectToPostgres(embedded = true)

    private fun connectToPostgres(embedded: Boolean): Connection {
        Class.forName("org.postgresql.Driver")
        if (embedded) {
            //log.info("Using embedded Sqlite database for testing; replace this flag to use postgres")
            return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
        } else {
            val url = env.config.property("postgres.url").getString()
            //log.info("Connecting to postgres database at $url")
            val user = env.config.property("postgres.user").getString()
            val password = env.config.property("postgres.password").getString()

            return DriverManager.getConnection(url, user, password)
        }
    }
}

