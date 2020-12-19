package pub.ihub.plugin

import org.gradle.api.Plugin



/**
 * @author liheng
 */
class HelloWorldPlugin implements Plugin<String> {

    @Override
    void apply(String s) {

        println 'Hello, ' + s

    }

}
