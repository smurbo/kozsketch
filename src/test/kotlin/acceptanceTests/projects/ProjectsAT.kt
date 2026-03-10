package com.acceptanceTests.projects

import com.helpers.builders.ProjectBuilder
import com.helpers.constants.DEFAULT_PROJECT_NAME
import com.helpers.constants.DEFAULT_PROJECT_RATING
import com.helpers.constants.PROJECTS_URL
import com.helpers.constants.UPDATED_PROJECT_NAME
import com.helpers.constants.UPDATED_PROJECT_RATING
import com.helpers.extensions.createEnvironment
import com.infrastructure.Project
import com.module
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import java.util.UUID

class ProjectsAT : BehaviorSpec({
    Given("a project does not yet exist") {
        When("a new project is created") {
            testApplication { createEnvironment()
                val createResponse = client.post(PROJECTS_URL) {
                    contentType(ContentType.Application.Json)
                    setBody(ProjectBuilder().createDefault())
                }
                val projectId = UUID.fromString(createResponse.bodyAsText())

                Then("we should receive HTTP.Created") {
                    createResponse.status shouldBe HttpStatusCode.Created
                }
                Then("we should be able to get the project") {
                    val project = client.get(
                            "$PROJECTS_URL/$projectId")
                        .body() as Project

                    project shouldNotBe null
                    project.name shouldBe DEFAULT_PROJECT_NAME
                    project.rating shouldBe DEFAULT_PROJECT_RATING
                }
                And("we try to update the project") {
                    val updateResponse = client.put(
                        "$PROJECTS_URL/$projectId"){
                        contentType(ContentType.Application.Json)
                        setBody(ProjectBuilder().createWithValidProperties(
                            UPDATED_PROJECT_NAME,
                            UPDATED_PROJECT_RATING
                        ))
                    }

                    Then("we should receive HTTP.OK") {
                        updateResponse.status shouldBe HttpStatusCode.OK
                    }
                    And("we get the project again") {
                        val updatedProject = client.get(
                            "$PROJECTS_URL/$projectId")
                            .body() as Project

                        Then("the project should be updated") {
                            updatedProject shouldNotBe null
                            updatedProject.name shouldBe UPDATED_PROJECT_NAME
                            updatedProject.rating shouldBe UPDATED_PROJECT_RATING
                        }
                    }
                }
                And("we try to delete the project") {
                    val deleteResponse = client.delete(
                        "$PROJECTS_URL/$projectId")

                    Then("we should receive HTTP.OK") {
                        deleteResponse.status shouldBe HttpStatusCode.OK
                    }
                    And("we try to get the project again") {
                        val getResponse = client.get(
                            "$PROJECTS_URL/$projectId")

                        Then("we should receive 404 Not Found") {
                            getResponse.status shouldBe HttpStatusCode.NotFound
                        }
                    }
                }
            }
        }
    }
    Given("a project already exists") {
        When("a project with the same name is created") {
            testApplication { createEnvironment()
                client.post(PROJECTS_URL) {
                    contentType(ContentType.Application.Json)
                    setBody(ProjectBuilder().createDefault())
                }

                val createResponse = client.post(PROJECTS_URL) {
                    contentType(ContentType.Application.Json)
                    setBody(ProjectBuilder().createDefault())
                }
                Then("we should still receive HTTP.Created") {
                    createResponse.status shouldBe HttpStatusCode.Created
                }
            }
        }
    }
})