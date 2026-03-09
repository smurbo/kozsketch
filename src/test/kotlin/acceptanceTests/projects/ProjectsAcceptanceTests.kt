package com.acceptanceTests.projects

import com.helpers.builders.ProjectBuilder
import com.helpers.constants.DEFAULT_PROJECT_NAME
import com.helpers.constants.DEFAULT_PROJECT_RATING
import com.helpers.constants.PROJECTS_URL
import com.infrastructure.Project
import com.module
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import java.util.UUID

class ProjectsAcceptanceTests : BehaviorSpec({
    val partialUrl = PROJECTS_URL
    isolationMode = IsolationMode.SingleInstance

    testApplication {
        application {
            module()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        Given("a project does not yet exist") {
            When("a new project is created") {
                val response = client.post(partialUrl) {
                    contentType(ContentType.Application.Json)
                    setBody(ProjectBuilder().createDefault())
                }

                Then("we should receive HTTP.Created") {
                    response.status shouldBe HttpStatusCode.Created
                }
                Then("we should be able to get the project") {
                    val project = client.get(
                            "$partialUrl/${UUID.fromString(response.bodyAsText())}")
                        .body() as Project

                    project shouldNotBe null
                    project.name shouldBe DEFAULT_PROJECT_NAME
                    project.rating shouldBe DEFAULT_PROJECT_RATING
                }
                And("we try to update the project") {
                    Then("we should receive HTTP.OK") {

                    }
                    And("we get the project again") {
                        Then("the project should be updated") {

                        }
                    }
                }
                And("we try to delete the project") {
                    Then("we should receive HTTP.Deleted") {

                    }
                    And("we try to get the project again") {
                        Then("we should receive 404 Not Found") {

                        }
                    }
                }
            }
        }
    }
})