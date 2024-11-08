plugins {
    id("java")
    id("io.freefair.lombok") version "8.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.slf4j:slf4j-api:1.7.36")
}


tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "Main"
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(arrayOf("--release", "17"))
}

tasks.compileJava{
    options.encoding = "UTF-8"
}

tasks.javadoc {
    options.encoding = "UTF-8"
}

tasks.create("deploy") {

    dependsOn("jar")

    doLast {
        exec {
            workingDir(".")
            commandLine("bash", "deploy.sh")
        }
    }
}

tasks.create("updateFrontend") {
    exec {
        workingDir(".")
        commandLine("bash", "updFront.sh")
    }
}