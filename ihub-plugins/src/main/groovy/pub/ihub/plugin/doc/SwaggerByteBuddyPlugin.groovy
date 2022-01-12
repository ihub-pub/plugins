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

import groovy.io.FileType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import net.bytebuddy.build.Plugin.WithPreprocessor
import net.bytebuddy.description.annotation.AnnotationDescription
import net.bytebuddy.description.field.FieldDescription
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.DynamicType.Builder
import net.bytebuddy.matcher.ElementMatcher
import org.codehaus.groovy.groovydoc.GroovyClassDoc
import org.codehaus.groovy.groovydoc.GroovyDoc
import org.codehaus.groovy.groovydoc.GroovyMethodDoc
import org.codehaus.groovy.groovydoc.GroovyParameter
import org.codehaus.groovy.groovydoc.GroovyRootDoc
import org.codehaus.groovy.tools.groovydoc.GroovyDocTool
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
@SuppressWarnings('CloseWithoutCloseable')
class SwaggerByteBuddyPlugin implements IHubByteBuddyPluginSupport, WithPreprocessor {

    private GroovyRootDoc rootDoc

    @Override
    void onPreprocess(TypeDescription target, ClassFileLocator locator) {
        if (!rootDoc) {
            String domainSrcPath = [config.sourcePath, 'src', 'main'].join File.separator
            List srcFiles = []
            (domainSrcPath as File).eachFileRecurse(FileType.FILES) {
                if (it.absolutePath.endsWith('.java') || it.absolutePath.endsWith('.groovy')) {
                    srcFiles << (it.absolutePath - domainSrcPath)
                }
            }
            rootDoc = new GroovyDocTool(domainSrcPath).tap {
                add srcFiles
            }.rootDoc
        }
    }

    @Override
    Builder<?> apply(Builder<?> builder, TypeDescription target, ClassFileLocator locator) {
        isController(target) ? applyController(builder, target) : applyEntity(builder, target)
    }

    @Override
    boolean matches(TypeDescription target) {
        isEntity(target) || isController(target)
    }

    @Override
    void close() throws IOException {
        rootDoc = null
    }

    private Builder<?> applyController(Builder<?> builder, TypeDescription target) {
        if (!isAnnotatedWith(target, Tag)) {
            builder = builder.annotateType getTag(target)
        }
        getGroovyClassDoc(target).methods().inject(builder) { b, method ->
            Map<String, List<String>> tags = (method.rawCommentText =~ /@(\w+) (.*)/).inject([:]) { tags, it ->
                tags.putIfAbsent(it[1], (tags.get(it[1]) ?: []) << it[2])
                tags
            }
            builder = b.method {
                it.name == method.name() && !isAnnotatedWith(it, Operation) && !isAnnotatedWith(it, ApiResponse)
            }.intercept(INSTANCE).annotateMethod getOperation(method), getApiResponse(tags.return)
            method.parameters().eachWithIndex { param, int i ->
                builder = builder.annotateParameter i, getParameter(param, tags.param)
            }
            builder
        }
    }

    private Builder<?> applyEntity(Builder<?> builder, TypeDescription target) {
        if (!isAnnotatedWith(target, Schema)) {
            builder = builder.annotateType getSchema(getGroovyClassDoc(target))
        }
        getGroovyClassDoc(target).fields().inject(builder) { b, field ->
            b.field(fieldMatcher(field.name())).annotateField getSchema(field)
        }
    }

    private GroovyClassDoc getGroovyClassDoc(TypeDescription target) {
        target.name.split('\\$').with {
            GroovyClassDoc classDoc = rootDoc.classes().find { doc -> first().endsWith '.' + doc.name() }
            size() > 1 ? classDoc.innerClasses().find { doc -> doc.name() == "${classDoc.name()}.${last()}" } : classDoc
        }
    }

    private AnnotationDescription getTag(TypeDescription target) {
        getGroovyClassDoc(target).with {
            ofType(Tag).define('name', firstSentenceCommentText()).define('description', commentText()
                .replaceAll('\\s|<.*>', '') - firstSentenceCommentText()).build()
        }
    }

    private static AnnotationDescription getOperation(GroovyMethodDoc doc) {
        ofType(Operation).define('summary', doc.firstSentenceCommentText())
            .define('description', doc.commentText()).build()
    }

    private static AnnotationDescription getApiResponse(List<String> returnDesc) {
        ofType(ApiResponse).define('responseCode', '200')
            .define('description', returnDesc?.join('') ?: '').build()
    }

    private static AnnotationDescription getParameter(GroovyParameter doc, List<String> params) {
        ofType(Parameter).define('name', doc.name()).define('description',
            params*.split(' ', 2).find { it.first() == doc.name() }?.last()).build()
    }

    private static AnnotationDescription getSchema(GroovyDoc doc) {
        ofType(Schema).define('description', doc.commentText()).build()
    }

    private static boolean isEntity(TypeDescription target) {
        isAnnotatedWith(target, Entity) || target.isAssignableTo(org.jmolecules.ddd.types.Entity) ||
            isAnnotatedWith(target, Identity) || target.isAssignableTo(Identifier)
    }

    private static boolean isController(TypeDescription target) {
        isAnnotatedWith target, RestController
    }

    private static ElementMatcher<? super FieldDescription> fieldMatcher(name) {
        { target ->
            target.name == name && !isAnnotatedWith(target, Schema)
        } as ElementMatcher<? super FieldDescription>
    }

}
