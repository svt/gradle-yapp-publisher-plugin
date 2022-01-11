package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

fun ObjectFactory.prop(
    property: String,
    propPrefix: String,
    envPrefix: String? = "",
    notFound: String = "",
    project: Project
): Property<String> {

    project.logger.warn("PROPSTRING " + property + " " + propPrefix + " " + envPrefix)
    return withDefault(
        (
            project.findProperty("$propPrefix$property") ?: System.getenv("$envPrefix${property.uppercase()}")
                ?: notFound
            ).toString()
    )
}

fun ObjectFactory.propBool(
    property: String,
    propPrefix: String,
    envPrefix: String? = "",
    project: Project
): Property<Boolean> {
    var result = (
        project.findProperty("$propPrefix$property") ?: System.getenv("$envPrefix${property.uppercase()}")
            ?: false
        ).toString()
    return withDefault(result.toBoolean())
}

inline fun ObjectFactory.propList(
    property: String,
    propPrefix: String,
    envPrefix: String,
    project: Project
): List<String> {

    return (
        project.findProperty("$propPrefix$property") ?: System.getenv("$envPrefix${property.uppercase()}")
            ?: ""
        ).toString()
        .split(",")
}

inline fun <reified T> ObjectFactory.withDefault(value: T): Property<T> =
    property(T::class.java).apply { convention(value) }

inline fun <reified T> ObjectFactory.withDefaultList(value: List<T>): ListProperty<T> =
    listProperty(T::class.java).apply { convention(value) }
