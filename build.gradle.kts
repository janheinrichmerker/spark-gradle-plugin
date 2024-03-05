import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.11.0"
    id("org.jetbrains.dokka") version "0.10.1"
    `maven-publish`
}

group = "dev.reimer"
version = "0.1.7"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleKotlinDsl())
    implementation("org.apache.spark:spark-launcher_2.12:2.4.4")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
}

lateinit var javadocJar: TaskProvider<Jar>
lateinit var sourcesJar: TaskProvider<Jar>

tasks {
    // Compile Kotlin to JVM1.8 bytecode.
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    // Include project license in generated JARs.
    withType<Jar> {
        from(project.projectDir) {
            include("LICENSE")
            into("META-INF")
        }
    }

    // Generate Kotlin/Java documentation from sources.
    dokka {
        outputFormat = "html"
    }

    // JAR containing Kotlin/Java documentation.
    javadocJar = register<Jar>("javadocJar") {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        dependsOn(dokka)
        from(dokka)
        archiveClassifier.set("javadoc")
    }

    // JAR containing all source files.
    sourcesJar = register<Jar>("sourcesJar") {
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(sourcesJar.get())
            artifact(javadocJar.get())
        }
    }
}

pluginBundle {
    website = "https://github.com/reimersoftware/spark-gradle-plugin"
    vcsUrl = "https://github.com/reimersoftware/spark-gradle-plugin.git"
    tags = listOf(
            "spark",
            "big data",
            "gradle",
            "gradle-plugin"
    )
}

gradlePlugin {
    plugins {
        create(name) {
            id = "dev.reimer.spark"
            displayName = "Spark Gradle Plugin"
            description = "A plugin for launching Spark applications."
            implementationClass = "dev.reimer.spark.gradle.plugin.SparkPlugin"
        }
    }
}

