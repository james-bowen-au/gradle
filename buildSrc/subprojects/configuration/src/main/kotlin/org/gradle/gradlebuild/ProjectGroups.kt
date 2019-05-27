package org.gradle.gradlebuild

import org.gradle.api.Project


object ProjectGroups {
    val excludedFromVulnerabilityCheck = setOf(
        ":buildScanPerformance",
        ":distributions",
        ":docs",
        ":integTest",
        ":internalAndroidPerformanceTesting",
        ":internalIntegTesting",
        ":internalPerformanceTesting",
        ":internalTesting",
        ":performance",
        ":runtimeApiInfo",
        ":smokeTest",
        ":soak")

    // TODO Why is smoke test not on that list
    private
    val Project.internalProjects
        get() = rootProject.subprojects.filter { it.name.startsWith("internal") ||
            it.name in setOf("integTest", "distributions", "performance", "buildScanPerformance", "kotlinCompilerEmbeddable", "kotlinDslTestFixtures", "kotlinDslIntegTests") }.toSet()

    val Project.javaProjects
        get() = rootProject.subprojects - listOf(project(":distributionsDependencies"))

    val Project.publicJavaProjects
        get() = javaProjects - internalProjects


    val Project.pluginProjects
        get() = setOf("announce", "antlr", "plugins", "codeQuality", "wrapper", "osgi", "maven",
            "ide", "scala", "signing", "ear", "javascript", "buildComparison",
            "diagnostics", "reporting", "publish", "ivy", "jacoco", "buildInit", "platformBase",
            "platformJvm", "languageJvm", "languageJava", "languageGroovy", "languageScala",
            "platformNative", "platformPlay", "idePlay", "languageNative", "toolingNative", "ideNative",
            "testingBase", "testingNative", "testingJvm", "testingJunitPlatform", "pluginDevelopment", "pluginUse",
            "resourcesHttp", "resourcesSftp", "resourcesS3", "resourcesGcs", "compositeBuilds", "buildCacheHttp").map { rootProject.project(it) }.toSet()

    val Project.implementationPluginProjects
        get() = setOf(
            rootProject.project("buildProfile"),
            rootProject.project("toolingApiBuilders"),
            rootProject.project("kotlinDslProviderPlugins"),
            rootProject.project("kotlinDslToolingBuilders")
        )

    val Project.publishedProjects
        get() = setOf(
            rootProject.project(":logging"),
            rootProject.project(":core"),
            rootProject.project(":modelCore"),
            rootProject.project(":toolingApi"),
            rootProject.project(":wrapper"),
            rootProject.project(":baseServices"),
            rootProject.project(":baseServicesGroovy"),
            rootProject.project(":workers"),
            rootProject.project(":dependencyManagement"),
            rootProject.project(":messaging"),
            rootProject.project(":processServices"),
            rootProject.project(":resources"),
            rootProject.project(":kotlinDslToolingModels"),
            rootProject.project(":buildOption"),
            rootProject.project(":cli"),
            rootProject.project(":coreApi"),
            rootProject.project(":files"),
            rootProject.project(":native"),
            rootProject.project(":persistentCache"),
            rootProject.project(":resourcesHttp"),
            rootProject.project(":buildCache"),
            rootProject.project(":buildCacheHttp"),
            rootProject.project(":snapshots")
        )

    val Project.publicProjects
        get() = pluginProjects +
            implementationPluginProjects +
            publicJavaProjects +
            rootProject.project(":kotlinDsl") -
            setOf(":smokeTest", ":soak").map { rootProject.project(it) }
}
