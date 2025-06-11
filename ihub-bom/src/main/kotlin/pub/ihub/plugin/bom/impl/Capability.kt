package pub.ihub.plugin.bom.impl

import pub.ihub.plugin.bom.specs.ConfigSpec

/**
 * Capabilities
 * @author liheng
 */
data class Capability(
    var dependency: String,
    var capabilities: MutableSet<String>
) : ConfigSpec<Capability> {

    override val comparedProperties: List<String> = listOf("dependency", "capabilities")

    override fun renewSpec(spec: Capability) {
        spec.capabilities.addAll(this.capabilities)
    }

    override fun appendToPrintData(data: MutableList<List<*>>) {
        capabilities.forEach { capability ->
            data.add(listOf(dependency, capability))
        }
    }

    // Custom equals and hashCode to match Groovy's @EqualsAndHashCode(includes = 'dependency')
    // if capabilities should not be part of equality/hashcode
    // However, the Groovy code includes 'capabilities' in comparedProperties, implying it should be part of comparison.
    // Kotlin data class by default includes all primary constructor properties in equals/hashCode.
    // If only 'dependency' was for equals/hashCode, it would be:
    // override fun equals(other: Any?): Boolean {
    //     if (this === other) return true
    //     if (javaClass != other?.javaClass) return false
    //     other as Capability
    //     return dependency == other.dependency
    // }
    // override fun hashCode(): Int = dependency.hashCode()
    // But since `comparedProperties` includes 'capabilities', the default data class behavior is correct.
}
