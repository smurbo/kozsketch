package com.data

import com.utils.getBooleanFromConfigOrDefault
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

interface DatabaseConnection {
    val database : Database
}

class DatabaseConnectionImpl(val config: ApplicationConfig) : DatabaseConnection {
    val logger = LoggerFactory.getLogger(DatabaseConnection::class.java)!!

    override val database: Database by lazy {
        var s: DatabaseSettings =
            if (isInMemoryDatabase()) getInMemorySettings()
            else getPostgresSettings()

        Database.connect(
            s.url,
            s.driver,
            s.user,
            s.password)
    }

    private fun getInMemorySettings(): DatabaseSettings = DatabaseSettings(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        driver = "org.h2.Driver",
        user = "root",
        password = ""
    ).also {
        logger.info("Using embedded Sqlite database for testing")
    }

    private fun getPostgresSettings(): DatabaseSettings = DatabaseSettings(
        url = config.property("postgres.url").getString(),
        driver = "org.postgresql.Driver",
        user = config.property("postgres.user").getString(),
        password = config.property("postgres.password").getString()
    ).also { s ->
        logger.info("Connecting to postgres database at $s.url")
    }

    private fun isInMemoryDatabase(): Boolean {
        return config.getBooleanFromConfigOrDefault(
            "options.inMemory",
            false)
    }
}

private data class DatabaseSettings(
    val url: String,
    val driver: String,
    val user: String,
    val password: String
)

