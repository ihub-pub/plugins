/*
 * Copyright (c) 2022 Henry 李恒 (henry.box@outlook.com).
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
package pub.ihub.plugin.doc

import groovy.transform.TupleConstructor
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import net.bytebuddy.description.annotation.AnnotationDescription
import net.bytebuddy.description.field.FieldDescription
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.DynamicType.Builder
import net.bytebuddy.matcher.ElementMatcher
import org.jmolecules.ddd.annotation.Entity
import org.jmolecules.ddd.annotation.Identity
import org.jmolecules.ddd.types.Identifier
import org.springframework.web.bind.annotation.RestController
import pub.ihub.plugin.IHubByteBuddyPluginSupport

import static net.bytebuddy.description.annotation.AnnotationDescription.Builder.ofType
import static net.bytebuddy.implementation.SuperMethodCall.INSTANCE



/**
 * Swagger增强编译插件
 * @author henry
 */
class SwaggerByteBuddyPlugin implements IHubByteBuddyPluginSupport {

    private final static List<Closure<Builder<?>>> CONTROLLER_BUILDER_MAPPING = [
        { Builder<?> builder, TypeDescription target, JavaSourceClass source ->
            builder.method { it.declaringType == target }.intercept(INSTANCE).annotateType source.tag
        },
        { Builder<?> builder, TypeDescription target, JavaSourceClass source ->
            source.methods.inject(builder) { b, method ->
                builder = b.method { it.name == method.tagName }.intercept(INSTANCE)
                    .annotateMethod method.operation, method.apiResponse
                method.params.eachWithIndex { JavaSourceDesc param, int i ->
                    builder = builder.annotateParameter i, param.parameter
                }
                builder
            }
        },
    ]

    private final static List<Closure<Builder<?>>> ENTITY_BUILDER_MAPPING = [
        { Builder<?> builder, TypeDescription target, JavaSourceClass source ->
            builder.method { it.declaringType == target }
                .intercept(INSTANCE).annotateType source.schema
        },
        { Builder<?> builder, TypeDescription target, JavaSourceClass source ->
            source.fields.inject(builder) { b, field ->
                b.field(fieldMatcher(field)).annotateField field.schema
            }
        },
    ]

    @Override
    Builder<?> apply(Builder<?> builder, TypeDescription target, ClassFileLocator locator) {
        List location = [config.sourcePath, 'src', 'main']
        String fileType = ((location + ['groovy']).join(File.separator) as File).directory ? 'groovy' : 'java'
        String path = (location + [fileType] + target.name.split('\\.').toList()).join File.separator
        JavaSourceClass source = getJavaSourceAnnotations path, fileType
        (isController(target) ? CONTROLLER_BUILDER_MAPPING : ENTITY_BUILDER_MAPPING)
            .inject(builder) { b, closure -> closure b, target, source }
    }

    @Override
    boolean matches(TypeDescription target) {
        isEntity(target) || isController(target)
    }

    private static boolean isEntity(TypeDescription target) {
        isAnnotatedWith(target, Entity) || target.isAssignableTo(org.jmolecules.ddd.types.Entity) ||
            isAnnotatedWith(target, Identity) || target.isAssignableTo(Identifier)
    }

    private static boolean isController(TypeDescription target) {
        isAnnotatedWith target, RestController
    }

    private static ElementMatcher<? super FieldDescription> fieldMatcher(field) {
        { target ->
            target.name == field.tagName
        } as ElementMatcher<? super FieldDescription>
    }

    private static JavaSourceClass getJavaSourceAnnotations(String path, String fileType) {
        String subClass = null
        if (path.contains('$')) {
            path.split('\\$').with {
                path = first()
                subClass = last()
            }
        }
        JavaSourceClass source = new JavaSourceClass()
        String sourceCode = ((path + '.' + fileType) as File).readLines().join '\n'
        (sourceCode =~ /\/\*\*(([^\*^\/]*|[\*^\/*]*|[^\**\/]*)*)\*\/([^\{|;]*)/).each {
            def (note, annotate) = it[1].replaceAll(/[\r\n\t]/, '').replaceAll('  ', '').split('\\* \\*')
                .with { [first(), size() > 1 ? last() : null] }
            def (name, description) = note.replaceAll(/[ *]/, '').split('<p>')
                .with { [first(), size() > 1 ? last() : ''] }

            def classMatcher = it[-1] =~ / class (\w+)/
            def methodMatcher = it[-1] =~ / ([a-z]\w+)\(/
            def fieldMatcher = it[-1] =~ / ([a-z]\w+)/
            if (classMatcher.find()) {
                if (!source.tagName || source.tagName == subClass) {
                    source.tagName = classMatcher[0][1]
                    source.name = name
                    source.description = description
                }
                if (source.tagName == subClass) {
                    source.fields.clear()
                    source.methods.clear()
                }
            } else if (methodMatcher.find()) {
                def tag = methodMatcher[0][1], params = [], returnDesc = ''
                annotate?.strip()?.split(' \\* ')?.each {
                    if (it.startsWith('@param')) {
                        it.split(' ').with {
                            params << new JavaSourceDesc(tag, it[1], it[2])
                        }
                    } else if (it.startsWith('@return')) {
                        returnDesc = it.split(' ').last()
                    }
                }
                source.methods << new JavaSourceMethod(tag, name, description, params, returnDesc)
            } else if (fieldMatcher.find()) {
                source.fields << new JavaSourceDesc(fieldMatcher[-1][1], name, description)
            }
        }
        source
    }

    @TupleConstructor
    static class JavaSourceDesc {

        String tagName
        String name
        String description

        AnnotationDescription getSchema() {
            ofType(Schema).define('description', name + description).build()
        }

        AnnotationDescription getParameter() {
            ofType(Parameter).define('name', name).define('description', description).build()
        }

    }

    static final class JavaSourceClass extends JavaSourceDesc {

        List<JavaSourceDesc> fields = []

        List<JavaSourceMethod> methods = []

        AnnotationDescription getTag() {
            ofType(Tag).define('name', name).define('description', description).build()
        }

    }

    @TupleConstructor(includeSuperProperties = true)
    static final class JavaSourceMethod extends JavaSourceDesc {

        List<JavaSourceDesc> params

        String returnDesc

        AnnotationDescription getOperation() {
            ofType(Operation).define('summary', name).define('description', description).build()
        }

        AnnotationDescription getApiResponse() {
            ofType(ApiResponse).define('responseCode', '200').define('description', returnDesc).build()
        }

    }

}
