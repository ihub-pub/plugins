/*
 * Copyright (c) 2021 Henry 李恒 (henry.box@outlook.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pub.ihub.plugin.bom

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import org.gradle.api.Action
import org.gradle.api.GradleException
import pub.ihub.plugin.IHubExtProperty
import pub.ihub.plugin.IHubExtension
import pub.ihub.plugin.IHubProjectExtensionAware

import static groovy.transform.TypeCheckingMode.SKIP
import static org.gradle.api.plugins.JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.API_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.COMPILE_ONLY_API_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.RUNTIME_ONLY_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.TEST_COMPILE_ONLY_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME
import static org.gradle.api.plugins.JavaPlugin.TEST_RUNTIME_ONLY_CONFIGURATION_NAME
import static pub.ihub.plugin.IHubPluginMethods.printConfigContent
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.BOM
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.DEPENDENCY
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.EXCLUDE
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.GROUP
import static pub.ihub.plugin.bom.IHubBomExtension.VersionType.MODULES



/**
 * BOM插件DSL扩展
 * @author liheng
 */
@IHubExtension(value = 'iHubBom', decorated = true)
@CompileStatic
@SuppressWarnings('ConfusingMethodName')
@TupleConstructor(allProperties = true, includes = 'project')
class IHubBomExtension implements IHubProjectExtensionAware, IHubExtProperty {

    final Set<BomSpecImpl> bomVersions = []
    final Set<BomSpecImpl> dependencyVersions = []
    final Set<BomSpecImpl> groupVersions = []
    final Set<BomSpecImpl> excludeModules = []
    final Set<BomSpecImpl> dependencies = []

    /**
     * 导入mavenBom
     * @param action 配置
     */
    @CompileStatic(SKIP)
    void importBoms(Action<GroupSpec<ModuleSpec>> action) {
        actionExecute BOM, action, bomVersions
    }

    /**
     * 配置依赖默认版本
     * @param action 配置
     */
    @CompileStatic(SKIP)
    void dependencyVersions(Action<GroupSpec<ModulesSpec>> action) {
        actionExecute MODULES, action, dependencyVersions
    }

    /**
     * 配置组版本策略（建议尽量使用bom）
     * @param action 配置
     */
    @CompileStatic(SKIP)
    void groupVersions(Action<GroupSpec<VersionSpec>> action) {
        actionExecute GROUP, action, groupVersions
    }

    /**
     * 排除组件依赖
     * @param action 配置
     */
    @CompileStatic(SKIP)
    void excludeModules(Action<GroupSpec<ModulesSpec>> action) {
        actionExecute EXCLUDE, action, excludeModules
    }

    /**
     * 配置组件依赖
     * @param action 配置
     */
    @CompileStatic(SKIP)
    void dependencies(Action<DependenciesSpec> action) {
        actionExecute DEPENDENCY, action, dependencies
    }

    @CompileStatic(SKIP)
    private void actionExecute(VersionType type, Action<ActionSpec<BomSpecImpl>> action, Set<BomSpecImpl> specs) {
        ActionSpec<BomSpecImpl> actionSpec = DEPENDENCY == type ? new DependenciesSpecImpl() : new GroupSpecImpl(type)
        action.execute actionSpec
        actionSpec.specs*.rightShift specs
    }

    void refreshCommonSpecs() {
        for (VersionType type : VersionType.values()) {
            Set<BomSpecImpl> specs = getProperty(type.fieldName) as Set<BomSpecImpl>
            if (!root) {
                setExtProperty rootProject, type.fieldName, findExtProperty(rootProject, type.fieldName, specs).with {
                    it.findAll { specs.any { s -> it.compare s } }
                }
            }
        }
    }

    void printConfigContent() {
        printConfig BOM, 'Group Maven Bom Version', 'Group', 'Module', 'Version'
        printConfig MODULES, 'Group Maven Module Version', 'Group', 'Module', 'Version'
        printConfig GROUP, 'Group Maven Default Version', 'Group', 'Version'
        printConfig EXCLUDE, 'Exclude Group Modules', 'Group', 'Module'
        printConfig DEPENDENCY, 'Config Default Dependencies', 'DependencyType', 'Dependencies'
    }

    boolean isRoot() {
        project == project.rootProject
    }

    @CompileStatic(SKIP)
    void printConfig(VersionType type, String title, String... taps) {
        Set<BomSpecImpl> specs = this."$type.fieldName" as Set<BomSpecImpl>
        Set<BomSpecImpl> commonSpecs = findExtProperty rootProject, type.fieldName
        printConfigContent "${project.name.toUpperCase()} $title", (root ? specs.tap {
            commonSpecs*.rightShift it
        } ?: specs : specs.findAll { commonSpecs.every { s -> !it.compare(s) } }).inject([]) { set, spec ->
            BomSpecImpl impl = type in [EXCLUDE, DEPENDENCY] ? new BomSpecImpl(type, spec.id).modules(spec.modules -
                (root ? [] : commonSpecs.find { r -> spec.id == r.id }?.modules) as String[]) : spec
            if (BOM == type) {
                set << [impl.id, impl.module, impl.version]
            } else if (MODULES == type) {
                set.addAll impl.modules.collect { [impl.id, it, impl.version] }
            } else if (GROUP == type) {
                set << [impl.id, impl.version]
            } else {
                set.addAll(impl.modules.collect { [impl.id, it] })
            }
            set
        }, taps
    }

    private static void assertProperty(boolean condition, String message) {
        if (!condition) {
            throw new GradleException(message)
        }
    }

    //<editor-fold desc="DSL扩展相关实体">

    interface ActionSpec<T> {

        List<T> getSpecs()

    }

    interface GroupSpec<T> extends ActionSpec<T> {

        T group(String group)

    }

    interface VersionSpec {

        VersionSpec version(String version)

    }

    interface ModuleSpec extends VersionSpec {

        ModuleSpec module(String module)

    }

    interface ModulesSpec extends VersionSpec {

        ModulesSpec modules(String... modules)

    }

    interface DependenciesSpec extends ActionSpec<BomSpecImpl> {

        void compile(String type, String... dependencies)

        default void api(String... dependencies) {
            compile API_CONFIGURATION_NAME, dependencies
        }

        default void implementation(String... dependencies) {
            compile IMPLEMENTATION_CONFIGURATION_NAME, dependencies
        }

        default void compileOnly(String... dependencies) {
            compile COMPILE_ONLY_CONFIGURATION_NAME, dependencies
        }

        default void compileOnlyApi(String... dependencies) {
            compile COMPILE_ONLY_API_CONFIGURATION_NAME, dependencies
        }

        default void runtimeOnly(String... dependencies) {
            compile RUNTIME_ONLY_CONFIGURATION_NAME, dependencies
        }

        default void testImplementation(String... dependencies) {
            compile TEST_IMPLEMENTATION_CONFIGURATION_NAME, dependencies
        }

        default void testCompileOnly(String... dependencies) {
            compile TEST_COMPILE_ONLY_CONFIGURATION_NAME, dependencies
        }

        default void testRuntimeOnly(String... dependencies) {
            compile TEST_RUNTIME_ONLY_CONFIGURATION_NAME, dependencies
        }

        default void annotationProcessor(String... dependencies) {
            compile ANNOTATION_PROCESSOR_CONFIGURATION_NAME, dependencies
        }

    }

    @CompileStatic
    @TupleConstructor(includes = 'type')
    private final class GroupSpecImpl implements GroupSpec<BomSpecImpl> {

        final VersionType type
        List<BomSpecImpl> specs = []

        @Override
        BomSpecImpl group(String group) {
            new BomSpecImpl(type, group).tap {
                specs << it
            }
        }

    }

    @CompileStatic
    private final class DependenciesSpecImpl implements DependenciesSpec {

        final List<BomSpecImpl> specs = []

        @Override
        void compile(String type, String... dependencies) {
            assertProperty type as boolean, 'dependencies type not null!'
            assertProperty dependencies as boolean, type + ' dependencies not empty!'
            specs << new BomSpecImpl(DEPENDENCY, type).tap { it.modules = dependencies as Set<String> }
        }

    }

    @CompileStatic
    @TupleConstructor(includes = 'type,id')
    private class BomSpecImpl implements ModuleSpec, ModulesSpec {

        final VersionType type
        final String id
        String version
        String module
        Set<String> modules

        @Override
        BomSpecImpl version(String version) {
            assertProperty EXCLUDE != type, 'Does not support \'version\' method!'
            this.version = version
            this
        }

        @Override
        BomSpecImpl module(String module) {
            this.module = module
            this
        }

        @Override
        BomSpecImpl modules(String... modules) {
            this.modules = modules as Set<String>
            this
        }

        boolean compare(BomSpecImpl o) {
            id == o.id && version == o.version && module == o.module && modules == o.modules
        }

        void rightShift(Set<BomSpecImpl> specs) {
            if (EXCLUDE == type && !modules) {
                modules = new HashSet<>(['all'])
            }
            BomSpecImpl spec = specs.find { this == it }
            if (spec as boolean) {
                if (EXCLUDE != type) {
                    spec.version version
                }
                if (BOM == type) {
                    spec.module module
                }
                if (modules) {
                    spec.modules.addAll modules
                }
            } else {
                specs << this
            }
        }

        @Override
        boolean equals(o) {
            BomSpecImpl impl = (BomSpecImpl) o
            id == impl.id && (BOM != type || module == impl.module) && (MODULES != type || version == impl.version)
        }

        @Override
        int hashCode() {
            int result
            result = 31 * type.hashCode() + id.hashCode()
            if (BOM == type) {
                result = 31 * result + module.hashCode()
            }
            if (MODULES == type) {
                result = 31 * result + version.hashCode()
            }
            result
        }

    }

    @TupleConstructor
    private enum VersionType {

        BOM('bomVersions'),
        MODULES('dependencyVersions'),
        GROUP('groupVersions'),
        EXCLUDE('excludeModules'),
        DEPENDENCY('dependencies')

        final String fieldName

    }

    //</editor-fold>

}
