package pub.ihub.plugin.bom.impl

import pub.ihub.plugin.bom.specs.CapabilitySpec

/**
 * Capability Spec Impl
 * @author liheng
 */
class CapabilitySpecImpl : CapabilitySpec {

    override val specs: MutableList<Capability> = mutableListOf()

    override fun requireCapability(dependency: String, vararg capabilities: String) {
        specs.add(Capability(dependency, capabilities.toMutableSet()))
    }
}
