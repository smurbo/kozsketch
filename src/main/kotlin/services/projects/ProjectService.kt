package com.services.projects

import com.data.Project
import com.models.ProjectModel
import com.repositories.Repository
import com.services.ServiceImpl

class ProjectService(
    repository: Repository<Project, ProjectModel>
) : ServiceImpl<ProjectModel>(repository)