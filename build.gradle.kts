import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.npm.NpmTask
import com.moowork.gradle.node.task.NodeTask

plugins {
    id("com.github.node-gradle.node") version "2.2.4"
    id("java-library")
    id("maven-publish")
    id("signing")
}

group = "io.openmanufacturing"
version = "2.0.0"

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.3")
    testImplementation("org.assertj:assertj-core:3.18.1")
    testImplementation("org.topbraid:shacl:1.3.1")
    testImplementation("io.vavr:vavr:0.10.3")
}

publishing {
    publications {
        create<MavenPublication>("mavenRelease") {
            groupId = "io.openmanufacturing"
            artifactId = "sds-aspect-meta-model"
            version = "2.0.0"

            from(components["java"])

            pom {
                name.set("BAMM Aspect Meta Model")
                description.set("BAMM Aspect Meta Model")
                url.set("https://openmanufacturingplatform.github.io/sds-bamm-aspect-meta-model/index.html")
                licenses {
                    license {
                        name.set("Mozilla Public License, Version 2.0")
                        url.set("https://www.mozilla.org/en-US/MPL/2.0/")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com:OpenManufacturingPlatform/sds-bamm-aspect-meta-model.git")
                    developerConnection.set("scm:git:ssh://git@github.com:OpenManufacturingPlatform/sds-bamm-aspect-meta-model.git")
                    url.set("https://github.com/OpenManufacturingPlatform/sds-bamm-aspect-meta-model")
                }
                developers {
                    developer {
                        name.set("Semantic Data Structuring Working Group")
                        email.set("artifacts@open-manufacturing.org")
                        organization.set("Open Manufacturing Platform")
                        organizationUrl.set("https://open-manufacturing.org")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_TOKEN")
            }
            val repositoryUrl: String =
                System.getenv("REPOSITORY_URL") ?: "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            url = uri(repositoryUrl)
        }
    }
}

signing {
    val signingKey: String? = System.getenv("PGP_KEY")
    val signingPassword: String? = System.getenv("PGP_KEY_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)

    sign(publishing.publications["mavenRelease"])
}

configure<NodeExtension> {
    version = "12.13.1"
    npmVersion = "6.9.0"
    isDownload = true
    workDir = file("${project.buildDir}/node")
    npmWorkDir = file("${project.buildDir}/npm")
    nodeModulesDir = file("${project.buildDir}")
}

val downloadAntoraCli = tasks.register<NpmTask>("downloadAntoraCli") {
    setArgs(listOf("install", "@antora/cli"))
    onlyIf { !file("${project.buildDir}/node_modules/@antora/cli/bin/antora").exists() }
}

val downloadAntoraSiteGeneratorLunr = tasks.register<NpmTask>("downloadAntoraSiteGeneratorLunr") {
    setArgs(listOf("install", "antora-site-generator-lunr"))
    onlyIf { !file("${project.buildDir}/node_modules/antora-site-generator-lunr/lib/index.js").exists() }
}

val downloadPlantUml = tasks.register<NpmTask>("downloadPlantUml") {
    setArgs(listOf("install", "asciidoctor-plantuml"))
    onlyIf { !file("${project.buildDir}/node_modules/asciidoctor-plantuml/package.json").exists() }
}

tasks.register<NodeTask>("antora") {
    setDependsOn(listOf(downloadAntoraCli, downloadAntoraSiteGeneratorLunr, downloadPlantUml))
    script = file("${project.buildDir}/node_modules/@antora/cli/bin/antora")
    setArgs(listOf("--generator", "antora-site-generator-lunr", "site.yml", "--stacktrace"))
    setWorkingDir(project.projectDir)
}

tasks.register<NodeTask>("antoraLocal") {
    setDependsOn(listOf(downloadAntoraCli, downloadAntoraSiteGeneratorLunr, downloadPlantUml))
    script = file("${project.buildDir}/node_modules/@antora/cli/bin/antora")
    setArgs(listOf("--generator", "antora-site-generator-lunr", "site-local.yml", "--stacktrace"))
    setWorkingDir(project.projectDir)
}

val disableSigning by tasks.registering {
    doLast {
        tasks.withType<Sign>().configureEach {
            isEnabled = false
        }
    }
}

tasks.getByName("publishToMavenLocal").dependsOn(disableSigning)

/**
 * Downloads the BCP 47 Language Tag Registry as defined by iana,
 * @see <a href="https://www.iana.org/assignments/language-subtag-registry/language-subtag-registry">https://www.iana.org/assignments/language-subtag-registry/language-subtag-registry</a>,
 * is JSON format.
 * The types which are required for the validation of the Locale Constraint, are extracted and written to a javascript file.
 */
val downloadBCP47LanguageTagRegistry = tasks.register( "downloadBCP47LanguageTagRegistry" ) {
    val languageTagRegistryScriptFile = file( "src/main/resources/bamm/scripts/languageRegistry.js" )
    val languageTagRegistryUrl = uri( "https://raw.githubusercontent.com/mattcg/language-subtag-registry/master/data/json/registry.json" ).toURL()
    val languageTagRegistry: ArrayList<Map<String, String>> = groovy.json.JsonSlurper().parse( languageTagRegistryUrl ) as ArrayList<Map<String, String>>
    val cleanedLanguageTagRegistry = kotlin.collections.HashMap<String, ArrayList<String>>()
    val grandfathered = ArrayList<String>()
    val languages = ArrayList<String>()
    val extlangs = ArrayList<String>()
    val scripts = ArrayList<String>()
    val regions = ArrayList<String>()
    val variants = ArrayList<String>()
    languageTagRegistry.forEach {
        val type = it["Type"]
        if ( type.equals( "grandfathered" ) ) {
            it["Tag"]?.let { tag -> grandfathered.add( tag ) }
        }
        if ( type.equals( "language" ) ) {
            it["Subtag"]?.let { subtag -> languages.add( subtag ) }
        }
        if ( type.equals( "extlang" ) ) {
            it["Subtag"]?.let { subtag -> extlangs.add( subtag ) }
        }
        if ( type.equals( "script" ) ) {
            it["Subtag"]?.let { subtag -> scripts.add( subtag ) }
        }
        if ( type.equals( "region" ) ) {
            it["Subtag"]?.let { subtag -> regions.add( subtag ) }
        }
        if ( type.equals( "variant" ) ) {
            it["Subtag"]?.let { subtag -> variants.add( subtag ) }
        }
    }
    cleanedLanguageTagRegistry["grandfathered"] = grandfathered
    cleanedLanguageTagRegistry["languages"] = languages
    cleanedLanguageTagRegistry["extlangs"] = extlangs
    cleanedLanguageTagRegistry["scripts"] = scripts
    cleanedLanguageTagRegistry["regions"] = regions
    cleanedLanguageTagRegistry["variants"] = variants

    val cleanedLanguageTagRegistryJson = groovy.json.JsonOutput.toJson( cleanedLanguageTagRegistry )
    val languageRegistryScript = "var languageRegistryAsJson = '$cleanedLanguageTagRegistryJson'"
    languageTagRegistryScriptFile.outputStream().write( languageRegistryScript.toByteArray() )
}

tasks.compileJava.get().dependsOn( downloadBCP47LanguageTagRegistry )