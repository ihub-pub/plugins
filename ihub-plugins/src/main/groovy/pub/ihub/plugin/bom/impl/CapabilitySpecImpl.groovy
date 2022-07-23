package pub.ihub.plugin.bom.impl

import groovy.transform.CompileStatic
import pub.ihub.plugin.bom.specs.CapabilitySpec



/**
 * Capability Spec Impl
 * @author liheng
 */
@CompileStatic
final class CapabilitySpecImpl implements CapabilitySpec {

    final List<Capability> specs = []

    @Override
    void requireCapability(String dependency, String... capabilities) {
        specs << new Capability(dependency, capabilities as Set<String>)
    }

}
