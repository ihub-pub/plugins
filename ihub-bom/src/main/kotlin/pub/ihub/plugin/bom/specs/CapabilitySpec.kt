package pub.ihub.plugin.bom.specs

import pub.ihub.plugin.bom.impl.Capability

/**
 * Capability Spec
 * @author liheng
 */
interface CapabilitySpec : ActionSpec<Capability> {
    fun requireCapability(dependency: String, vararg capabilities: String)
}
