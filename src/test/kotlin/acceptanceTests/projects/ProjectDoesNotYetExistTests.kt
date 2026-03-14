package com.acceptanceTests.projects

import com.helpers.builders.ProjectModelBuilder
import com.helpers.constants.DEFAULT_PROJECT_NAME
import com.helpers.constants.DEFAULT_PROJECT_RATING
import com.routes.PROJECTS_URL
import com.helpers.constants.UPDATED_PROJECT_NAME
import com.helpers.constants.UPDATED_PROJECT_RATING
import com.helpers.extensions.delete
import com.helpers.extensions.getById
import com.helpers.extensions.runAT
import com.helpers.extensions.update
import com.helpers.utils.apiRoute
import com.models.ProjectModel
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import java.util.UUID

class ProjectDoesNotYetExistTests : BehaviorSpec({
    Given("a project does not yet exist") {
        When("a new project is created") {
            runAT {
                val createResponse = requestCreateDefaultProject()
                val projectId = UUID.fromString(createResponse.bodyAsText())

                Then("we should receive HTTP.Created") {
                    createResponse.status shouldBe HttpStatusCode.Created
                }
                Then("we should be able to get the project") {
                    val project = client.getById<ProjectModel>(
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
                        ProjectModelBuilder().createWithValidProperties(
                            UPDATED_PROJECT_NAME,
                            UPDATED_PROJECT_RATING
                        )
                    )

                    Then("we should receive HTTP.OK") {
                        updateResponse.status shouldBe HttpStatusCode.OK
                    }
                    And("we get the project again") {
                        val updatedProject = client.getById<ProjectModel>(
                            PROJECTS_URL,
                            projectId
                        )

                        Then("the project should be updated") {
                            updatedProject shouldNotBe null
                            updatedProject.name shouldBe UPDATED_PROJECT_NAME
                            updatedProject.rating shouldBe UPDATED_PROJECT_RATING
                        }
                    }
                }
                And("we try to delete the project") {
                    val deleteResponse = client.delete(
                        PROJECTS_URL,
                        projectId)

                    Then("we should receive HTTP.OK") {
                        deleteResponse.status shouldBe HttpStatusCode.OK
                    }
                    And("we try to get the project again") {
                        val getResponse = client.get(
                            "${apiRoute(PROJECTS_URL)}/$projectId")

                        Then("we should receive 404 Not Found") {
                            getResponse.status shouldBe HttpStatusCode.NotFound
                        }
                    }
                }
            }
        }
    }
})