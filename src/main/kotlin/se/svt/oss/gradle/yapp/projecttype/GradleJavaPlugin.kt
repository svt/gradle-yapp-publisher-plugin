package se.svt.oss.gradle.yapp.projecttype

import org.gradle.api.Project
import se.svt.oss.gradle.yapp.config.ProjectType

class GradleJavaPlugin(override val project: Project) :
    ProjectType(project)
