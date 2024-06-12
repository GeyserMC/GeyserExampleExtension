import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.ArrayList
import kotlin.io.path.*

plugins {
    java
    id("io.github.goooler.shadow") version "8.1.7"
}

val id = project.property("id") as String
val extensionName = project.property("extensionName") as String
var mainClass: String? = null

repositories {
    mavenCentral()
    // Repo for the Geyser API artifact
    maven("https://repo.opencollab.dev/main/")
}

dependencies {
    // Geyser API - needed for all extensions
    compileOnly("org.geysermc.geyser:api:2.3.2-SNAPSHOT")

    // Include other dependencies here - e.g. configuration libraries.
}

// Java currently requires Java 17 or higher, so extensions should also target it
java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}

afterEvaluate {
    val idRegex = Regex("[a-z][a-z0-9-_]{0,63}")
    if (idRegex.matches(id).not()) {
        throw IllegalArgumentException("Invalid extension id $id! Must only contain lowercase letters, " +
                "and cannot start with a number.")
    }

    val nameRegex = Regex("^[A-Za-z_.-]+$")
    if (nameRegex.matches(extensionName).not()) {
        throw IllegalArgumentException("Invalid extension name $extensionName! Must fit regex: ${nameRegex.pattern})")
    }
}

tasks {
    compileJava {
        doLast {
            val targetInterface = "org.geysermc.geyser.api.extension.Extension"
            val sourceOutput = sourceSets.main.get().output.classesDirs // Access compiled class directory
            val classLoader = URLClassLoader(sourceOutput.map { file -> file.toURI().toURL() }.toTypedArray())

            sourceOutput.forEach { folders ->
                Files.walk(folders.toPath())
                    .filter { it.name.endsWith(".class") }
                    .forEach { file ->
                        val className = file.relativeTo(folders.toPath()).toString()
                            .removeSuffix(".class")
                            .replace(File.separator, ".")
                        try {
                            val clazz = classLoader.loadClass(className)
                            if (clazz.interfaces.any { it.name == targetInterface }) {
                                if (mainClass == null) {
                                    mainClass = className
                                } else {
                                    throw IllegalStateException("Found multiple classes that implement the Extension interface!")
                                }
                            }
                        } catch (e: NoClassDefFoundError) {
                            // Handle potential class not found exceptions
                            println("Class not found for ${file.name}, $className")
                        }
                    }
            }

            if (mainClass == null) {
                throw GradleException("No class implementing $targetInterface found!")
            }
        }
    }

    // This automatically fills in the extension.yml file.
    processResources {
        filesMatching("extension.yml") {
            expand(
                "id" to id,
                "name" to extensionName,
                "main" to mainClass,
                "version" to project.version,
                "author" to project.property("author")
            )
        }
    }

    build {
        dependsOn(shadowJar)
    }

    jar {
        archiveClassifier.set("unshaded")
    }

    shadowJar {
        archiveClassifier.set("")
    }
}