[![GitHub Actions](https://img.shields.io/github/actions/workflow/status/janheinrichmerker/spark-gradle-plugin/ci.yml?branch=master&style=flat-square)](https://github.com/janheinrichmerker/spark-gradle-plugin/actions/workflows/ci.yml)
[![Gradle plugin portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/dev/reimer/spark/dev.reimer.spark.gradle.plugin/maven-metadata.xml.svg?label=gradle&style=flat-square)](https://plugins.gradle.org/plugin/dev.reimer.spark)
[![JitPack](https://img.shields.io/jitpack/v/github/janheinrichmerker/spark-gradle-plugin?style=flat-square)](https://jitpack.io/#dev.reimer/spark-gradle-plugin)
# 💾 spark-gradle-plugin<sup>[α](#status-α)</sup>

Gradle plugin for launching Spark applications.

## Gradle Dependency

This plugin is available from the **Gradle [plugin portal](https://plugins.gradle.org/plugin/dev.reimer.spark)**.  
Add this in your `build.gradle.kts` or `build.gradle` file:

<details open><summary>Kotlin</summary>

```kotlin
plugins {
  id("dev.reimer.spark") version "<version>"
}
```

</details>

<details><summary>Groovy</summary>

```groovy
plugins {
  id "dev.reimer.spark" version "<version>"
}
```

</details>

### Snapshots
Alternatively, snapshot builds are available on **[JitPack](https://jitpack.io/#dev.reimer/spark-gradle-plugin)**.  
Add this in your `build.gradle.kts` or `build.gradle` file:

<details><summary>Kotlin</summary>

```kotlin
buildscript {
    repositories {
        maven("https://jitpack.io")
    }
    dependencies {
        implementation("dev.reimer:spark-gradle-plugin:<version>")
    }
}
```

</details>

<details><summary>Groovy</summary>

```groovy
buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        implementation 'dev.reimer:spark-gradle-plugin:<version>'
    }
}
```

</details>

## Usage

### Configuring builds

Run Spark applications by registering tasks:

<details open><summary>Kotlin</summary>

```kotlin
tasks.register<SparkSubmit>("launchSparkApp") {
    applicationResource.set(file("app.jar"))
    mainClass.set("com.example.Example")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
task launchSparkApp(type: SparkSubmit) {
    applicationResource = file("app.jar")
    mainClass = "com.example.Example"
}
```

</details>

(You may need to import [`SparkSubmit`](src/main/kotlin/dev/reimer/spark/gradle/plugin/SparkSubmit.kt))

### Options

Options can be applied to each task individually.

<details open><summary>Kotlin</summary>

```kotlin
tasks.register<SparkSubmit>("launchSparkApp") {
    applicationResource.set(file("…"))
    mainClass.set("…")
    applicationArguments.add("…")
    applicationName.set("…")
    deployMode.set(SparkSubmit.DeployMode.Cluster)
    master.set("yarn")
    configuration.put("…", 3)
    propertiesFile.set(file("…"))
    jars.from("…")
    files.from("…")
    pythonFiles.from("…")
    verbose.set(true)
    javaHome.set("…")
    sparkHome.set("…")
    awaitCompletion.set(false)
}
```

</details>

<details><summary>Groovy</summary>

```groovy
task launchSparkApp(type: SparkSubmit) {
    applicationResource = file("…")
    mainClass = "…"
    applicationArguments.add("…")
    applicationName = "…"
    deployMode = SparkSubmit.DeployMode.Cluster
    master = "yarn"
    configuration["…"] = 3
    propertiesFile = file("…")
    jars.from("…")
    files.from("…")
    pythonFiles.from("…")
    verbose = true
    javaHome = "…"
    sparkHome = "…"
    awaitCompletion = false
}
```

</details>

## Status α

⚠️ _Warning:_ This project is in an experimental alpha stage:
- The API may be changed at any time without further notice.
- Development still happens on `master`.
- Pull Requests are highly appreciated!
