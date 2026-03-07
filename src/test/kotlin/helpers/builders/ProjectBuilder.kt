package com.helpers.builders

import com.helpers.constants.DEFAULT_PROJECT_NAME
import com.helpers.constants.DEFAULT_PROJECT_RATING
import com.infrastructure.Project

class ProjectBuilder {
    fun createDefault() : Project {
        return createWithValidProperties()
    }

    fun createWithValidProperties(
        name : String? = null,
        rating : String? = null
    ) : Project {
        val name = if (name.isNullOrBlank()) DEFAULT_PROJECT_NAME else name
        val rating = if (rating.isNullOrBlank()) DEFAULT_PROJECT_RATING else rating

        return Project(name, rating)
    }
}