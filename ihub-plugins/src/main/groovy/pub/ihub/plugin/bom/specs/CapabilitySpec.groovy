package pub.ihub.plugin.bom.specs

import pub.ihub.plugin.bom.impl.Capability



/**
 * Capability Spec
 * @author liheng
 */
interface CapabilitySpec extends ActionSpec<Capability> {

    void dependency(String dependency, String... capabilities)

}
