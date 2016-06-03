/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.plugins.ide.idea.model.internal
import org.gradle.api.internal.artifacts.ivyservice.projectmodule.CompositeBuildIdeProjectResolver
import org.gradle.api.internal.artifacts.ivyservice.projectmodule.ProjectComponentRegistry
import org.gradle.internal.component.local.model.DefaultProjectComponentIdentifier
import org.gradle.internal.component.model.ComponentArtifactMetadata
import org.gradle.internal.component.model.DefaultIvyArtifactName
import org.gradle.internal.service.DefaultServiceRegistry
import org.gradle.plugins.ide.internal.resolver.model.IdeProjectDependency
import spock.lang.Specification

class ModuleDependencyBuilderTest extends Specification {

    def projectId = DefaultProjectComponentIdentifier.newId("project-path")
    def ideDependency = new IdeProjectDependency(projectId, "test")
    def projectComponentRegistry = Mock(ProjectComponentRegistry)
    def serviceRegistry = new DefaultServiceRegistry().add(ProjectComponentRegistry, projectComponentRegistry)
    def builder = new ModuleDependencyBuilder(new CompositeBuildIdeProjectResolver(serviceRegistry))

    def "builds dependency for nonIdea project"() {
        when:
        def dependency = builder.create(ideDependency, 'compile')

        then:
        dependency.scope == 'compile'
        dependency.name == "test"

        and:
        projectComponentRegistry.getAdditionalArtifacts(_) >> []
    }

    def "builds dependency for project"() {
        given:
        def imlArtifact = Stub(ComponentArtifactMetadata) {
            getName() >> new DefaultIvyArtifactName("foo", "iml", "iml", null)
        }

        when:
        def dependency = builder.create(ideDependency, 'compile')

        then:
        dependency.scope == 'compile'
        dependency.name == 'foo'

        and:
        projectComponentRegistry.getAdditionalArtifacts(_) >> [imlArtifact]
    }
}
