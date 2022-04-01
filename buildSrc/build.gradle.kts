plugins {
    id("java-library")
    java
}

dependencies {
    gradleApi()
    implementation("org.apache.poi:poi:4.0.1")
    implementation("org.apache.jena:jena-core:4.2.0")
    implementation("org.apache.jena:jena-arq:4.2.0")
    implementation("org.apache.commons:commons-text:1.9")
}

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

