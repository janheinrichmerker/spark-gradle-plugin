plugins {
    kotlin("jvm") version "2.1.0"
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.3.0"
    id("org.jetbrains.dokka") version "0.10.1"
    `maven-publish`
    id("com.palantir.git-version") version "3.1.0"
}

val gitVersion: groovy.lang.Closure<String> by extra
group = "dev.reimer"
version = gitVersion()

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleKotlinDsl())
    implementation("org.apache.spark:spark-launcher_2.12:3.5.4")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
}

lateinit var javadocJar: TaskProvider<Jar>
lateinit var sourcesJar: TaskProvider<Jar>

tasks {
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

gradlePlugin {
    website = "https://github.com/reimersoftware/spark-gradle-plugin"
    vcsUrl = "https://github.com/reimersoftware/spark-gradle-plugin.git"
    plugins {
        create(name) {
            id = "dev.reimer.spark"
            implementationClass = "dev.reimer.spark.gradle.plugin.SparkPlugin"
            displayName = "Spark Gradle Plugin"
            description = "A plugin for launching Spark applications."
            tags = listOf(
                "spark",
                "big data",
                "gradle",
                "gradle-plugin"
            )
        }
    }
}
