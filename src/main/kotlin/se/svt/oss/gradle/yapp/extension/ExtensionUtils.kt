package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

fun Project.prop(property: String, propPrefix: String? = "", envPrefix: String? = "", notFound: String = ""): String {
    return (
        this.findProperty("$propPrefix$property") ?: System.getenv("$envPrefix${property.uppercase()}")
            ?: notFound
        ).toString()
}

fun Project.propBool(property: String, propPrefix: String, envPrefix: String): Boolean {
    val prop = this.findProperty("$propPrefix$property")
    return prop?.toString()?.toBoolean() ?: System.getenv("$envPrefix$property").toBoolean()
}

fun Project.propList(property: String, propPrefix: String, envPrefix: String): List<String> {
    return (this.findProperty("$propPrefix$property") ?: System.getenv("$envPrefix$property") ?: "").toString()
        .split(",")
}

inline fun <reified T> Project.withDefault(value: T): Property<T> =
    objects.property(T::class.java).apply { convention(value) }

inline fun <reified T> Project.withDefaultList(value: List<T>): ListProperty<T> =
    objects.listProperty(T::class.java).apply { convention(value) }
