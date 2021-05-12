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
package pub.ihub.plugin.spring

import static pub.ihub.plugin.IHubPluginMethods.findProperty

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import pub.ihub.plugin.IHubExtension


/**
 * 原生镜像插件扩展
 * @author henry
 */
class IHubNativeExtension implements IHubExtension {

	final Project project

	//<editor-fold desc="Build Configuration">

	/**
	 * JVM版本
	 */
	String bpJvmVersion
	/**
	 * 是否启用原生映像构建
	 */
	boolean bpNativeImage = true
	/**
	 * 传递给原生映像命令的参数
	 */
	String bpNativeImageBuildArguments

	//</editor-fold>

	//<editor-fold desc="Launch Configuration">

	/**
	 * JVM内存
	 */
	String bplJvmHeadRoom = '8G'
	/**
	 * JVM运行时已加载类的数量，默认“35% of classes"
	 */
	String bplJvmLoadedClassCount
	/**
	 * JVM运行时用户线程数，默认“250”
	 */
	String bplJvmThreadCount
	/**
	 * JVM环境变量
	 */
	String javaToolOptions

	//</editor-fold>

	IHubNativeExtension(Project project) {
		this.project = project
	}

	Map<String, String> getEnvironment() {
		[
			BP_JVM_VERSION                 : bpJvmVersion ?: JavaVersion.current().majorVersion,
			BP_NATIVE_IMAGE                : bpNativeImage.toString(),
			BP_NATIVE_IMAGE_BUILD_ARGUMENTS: bpNativeImageBuildArguments,
			BPL_JVM_HEAD_ROOM              : bplJvmHeadRoom,
			BPL_JVM_LOADED_CLASS_COUNT     : bplJvmLoadedClassCount,
			BPL_JVM_THREAD_COUNT           : bplJvmThreadCount,
			JAVA_TOOL_OPTIONS              : javaToolOptions ?: findProperty('JAVA_TOOL_OPTIONS'),
		].findAll { it.value }
	}

}
