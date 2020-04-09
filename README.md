[![GitHub Actions](https://img.shields.io/github/workflow/status/reimersoftware/spark-gradle-plugin/CI?style=flat-square)](https://github.com/reimersoftware/spark-gradle-plugin/actions?query=workflow%3A"CI")
[![Gradle plugin portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/dev/reimer/spark/dev.reimer.spark.gradle.plugin/maven-metadata.xml.svg?label=gradle&style=flat-square)](https://plugins.gradle.org/plugin/dev.reimer.spark)
[![JitPack](https://img.shields.io/jitpack/v/github/reimersoftware/spark-gradle-plugin?style=flat-square)](https://jitpack.io/#dev.reimer/spark-gradle-plugin)
[![Central Security Project](https://img.shields.io/badge/report-vulnerability-e10e71?style=flat-square)](https://hackerone.com/central-security-project/reports/new)
# 💾 spark-gradle-plugin<sup>[α](#status-α)</sup>

Gradle plugin for launching Spark applications.

## Gradle Dependency

This library is available on [**jitpack.io**](https://jitpack.io/#dev.reimer/spark-gradle-plugin).  
Add this in your `build.gradle.kts` or `build.gradle` file:

<details open><summary>Kotlin</summary>

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("dev.reimer:spark-gradle-plugin:<version>")
}
```

</details>

<details><summary>Groovy</summary>

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'dev.reimer:spark-gradle-plugin:<version>'
}
```

</details>

## Status α

⚠️ _Warning:_ This project is in an experimental alpha stage:
- The API may be changed at any time without further notice.
- Development still happens on `master`.
- Pull Requests are highly appreciated!