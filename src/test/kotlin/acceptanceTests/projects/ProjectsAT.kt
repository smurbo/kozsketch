package com.acceptanceTests.projects

import com.helpers.builders.ProjectBuilder
import com.helpers.constants.DEFAULT_PROJECT_NAME
import com.helpers.constants.DEFAULT_PROJECT_RATING
import com.helpers.constants.PROJECTS_URL
import com.helpers.constants.UPDATED_PROJECT_NAME
import com.helpers.constants.UPDATED_PROJECT_RATING
import com.helpers.extensions.create
import com.helpers.extensions.delete
import com.helpers.extensions.getById
import com.helpers.extensions.runAT
import com.helpers.extensions.update
import com.infrastructure.Project
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecWhenContainerScope
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder
import java.util.UUID

class ProjectsAT : BehaviorSpec({
    projectDoesNotYetExistTests()
    projectAlreadyExistsTests()
})

private fun BehaviorSpec.projectDoesNotYetExistTests() {
    Given("a project does not yet exist") {
        When("a new project is created") {
            runAT {
                val createResponse = requestCreateDefaultProject()
                val projectId = UUID.fromString(createResponse.bodyAsText())

                Then("we should receive HTTP.Created") {
                    createResponse.status shouldBe HttpStatusCode.Created
                }
                Then("we should be able to get the project") {
                    val project = client.getById<Project>(
                        PROJECTS_URL,
                        projectId
                    )

                    project shouldNotBe null
                    project.name shouldBe DEFAULT_PROJECT_NAME
                    project.rating shouldBe DEFAULT_PROJECT_RATING
                }
                And("we try to update the project") {
                    val updateResponse = client.update(
                        PROJECTS_URL,
                        projectId,
                        ProjectBuilder().createWithValidProperties(
                            UPDATED_PROJECT_NAME,
                            UPDATED_PROJECT_RATING
                        )
                    )

                    Then("we should receive HTTP.OK") {
                        updateResponse.status shouldBe HttpStatusCode.OK
                    }
                    And("we get the project again") {
                        projectShouldBeUpdated(
                            client,
                            projectId,
                            this@And
                        )
                    }
                }
                And("we try to delete the project") {
                    val deleteResponse = client.delete(PROJECTS_URL, projectId)

                    Then("we should receive HTTP.OK") {
                        deleteResponse.status shouldBe HttpStatusCode.OK
                    }
                    And("we try to get the project again") {
                        val getResponse = client.get("$PROJECTS_URL/$projectId")

                        Then("we should receive 404 Not Found") {
                            getResponse.status shouldBe HttpStatusCode.NotFound
                        }
                    }
                }
            }
        }
    }
}

private suspend fun ApplicationTestBuilder.requestCreateDefaultProject(): HttpResponse {
    return client.create(
        PROJECTS_URL,
        ProjectBuilder().createDefault()
    )
}

//TODO: might extract this to separate class?
private suspend fun projectShouldBeUpdated(
    client: HttpClient,
    projectId: UUID?,
    scope: BehaviorSpecWhenContainerScope
) {
    val updatedProject = client.get(
        "$PROJECTS_URL/$projectId"
    )
        .body() as Project

    scope.Then("the project should be updated") {
        updatedProject shouldNotBe null
        updatedProject.name shouldBe UPDATED_PROJECT_NAME
        updatedProject.rating shouldBe UPDATED_PROJECT_RATING
    }
}

private fun BehaviorSpec.projectAlreadyExistsTests() {
    Given("a project already exists") {
        When("a project with the same name is created") {
            runAT {
                requestCreateDefaultProject()
                val createResponse = requestCreateDefaultProject()
                Then("we should still receive HTTP.Created") {
                    createResponse.status shouldBe HttpStatusCode.Created
                }
            }
        }
    }
}