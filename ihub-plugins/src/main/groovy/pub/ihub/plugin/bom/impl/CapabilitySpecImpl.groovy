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
    void dependency(String dependency, String... capabilities) {
        specs << new Capability(dependency: dependency, capabilities: capabilities as Set<String>)
    }

}
