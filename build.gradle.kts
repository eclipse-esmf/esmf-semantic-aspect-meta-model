import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.npm.NpmTask
import com.moowork.gradle.node.task.NodeTask

plugins {
    id("com.github.node-gradle.node") version "2.2.4"
}

configure<NodeExtension> {
    setVersion("12.13.1")
    setNpmVersion("6.9.0")
    setDownload(true)
    setWorkDir(file("${project.buildDir}/node"))
    setNpmWorkDir(file("${project.buildDir}/npm"))
    setNodeModulesDir(file("${project.buildDir}"))
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
    setScript(file("${project.buildDir}/node_modules/@antora/cli/bin/antora"))
    setArgs(listOf("--generator", "antora-site-generator-lunr", "site.yml", "--stacktrace"))
    setWorkingDir(project.projectDir)
}

tasks.register<NodeTask>("antoraLocal") {
    setDependsOn(listOf(downloadAntoraCli, downloadAntoraSiteGeneratorLunr, downloadPlantUml))
    setScript(file("${project.buildDir}/node_modules/@antora/cli/bin/antora"))
    setArgs(listOf("--generator", "antora-site-generator-lunr", "site-local.yml", "--stacktrace"))
    setWorkingDir(project.projectDir)
}

tasks.register<Delete>("clean") {
    delete(project.buildDir)
}
