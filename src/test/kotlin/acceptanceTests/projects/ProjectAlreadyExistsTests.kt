package com.acceptanceTests.projects

import com.helpers.extensions.runAT
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpStatusCode

class ProjectAlreadyExistsTests : BehaviorSpec({
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
})
