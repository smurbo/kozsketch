package com.helpers.builders

import com.helpers.constants.DEFAULT_PROJECT_NAME
import com.helpers.constants.DEFAULT_PROJECT_RATING
import com.models.ProjectModel

class ProjectModelBuilder {
    fun createDefault() : ProjectModel {
        return createWithValidProperties()
    }

    fun createWithValidProperties(
        name : String? = null,
        rating : String? = null
    ) : ProjectModel {
        val name = if (name.isNullOrBlank()) DEFAULT_PROJECT_NAME else name
        val rating = if (rating.isNullOrBlank()) DEFAULT_PROJECT_RATING else rating

        return ProjectModel(
            id = null,
            name,
            rating)
    }
}