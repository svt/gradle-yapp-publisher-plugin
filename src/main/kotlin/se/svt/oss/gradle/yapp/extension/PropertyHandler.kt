package se.svt.oss.gradle.yapp.extension

import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import java.util.Properties
import javax.inject.Inject

abstract class PropertyHandler @Inject constructor(
    val project: Project,
    val objects: ObjectFactory,
    val propPrefix: String,
    val envPrefix: String
) {

    internal fun property(property: String) = objects.prop(property, propPrefix, envPrefix, project = project)
    internal fun propertyBool(property: String) = objects.propBool(property, propPrefix, envPrefix, project)

    internal inline fun <reified T> propertyList(
        property: String,
        convent: (Map<String, List<String>>) -> List<T>
    ): ListProperty<T> {
        val propList = objects.propList(property, propPrefix, envPrefix, project)
        propList.forEach {
            project.logger.warn("PROPLIST " + it)
        }
        project.logger.warn("PROPLIST VARS " + property + propPrefix + envPrefix)
        var list: List<String> = if (propList.isEmpty()) { emptyList() } else propList
        return objects.listProperty(T::class.java).apply { convention(convent(mapOf(property to list))) }
    }

    fun getPropertyList(properties: Properties, name: String): List<List<String>> {
        val a: Map<String, String> = properties.entries.associate { it.key.toString() to it.value.toString() }
        return a.filter { (k, v) -> k.matches("""^$name\.\d+$""".toRegex()) }
            .map { (k, v) -> v }.map { v -> v.split(",") }
            .toList()
    }
}
