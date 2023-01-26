package pub.ihub.plugin.bom.impl

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import pub.ihub.plugin.bom.specs.ConfigSpec



/**
 * Capabilities
 * @author liheng
 */
@CompileStatic
@TupleConstructor
@EqualsAndHashCode(includes = 'dependency')
final class Capability implements ConfigSpec<Capability> {

    String dependency
    Set<String> capabilities

    final List<String> comparedProperties = ['dependency', 'capabilities']

    @Override
    void renewSpec(Capability spec) {
        spec.capabilities.addAll capabilities
    }

    @Override
    void appendToPrintData(List<List<?>> data) {
        data.addAll(capabilities.collect { [dependency, it] })
    }

}
