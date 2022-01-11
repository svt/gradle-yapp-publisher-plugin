package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class SigningExtension @Inject constructor(project: Project, objects: ObjectFactory) :
    PropertyHandler(project, objects, "yapp.signing.", "YAPP_SIGNING_") {

    var enabled: Property<Boolean> = objects.propBool("enabled", propPrefix, envPrefix, project = project)
    var signSnapshot: Property<Boolean> =
        objects.propBool("signSnapshot", propPrefix, envPrefix, project = project)
    var keyId: Property<String> = objects.prop("keyId", propPrefix, envPrefix, project = project)
    var keySecret: Property<String> = objects.prop("keySecret", propPrefix, envPrefix, project = project)
    var key: Property<String> = objects.prop("key", propPrefix, envPrefix, project = project)
}
