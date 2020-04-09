package dev.reimer.spark.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.invoke

class SparkPlugin : Plugin<Project> {

    companion object {
        const val TASK_GROUP = "spark"
        const val LAUNCH_SPARK_TASK_NAME = "launchSparkApplications"
    }

    override fun apply(project: Project) {
        // Register standard task for launching all Spark applications.
        val launchSpark: TaskProvider<Task> = project.tasks.register(LAUNCH_SPARK_TASK_NAME)

        // Add all other SparkSubmit tasks as dependencies to the main task.
        project.tasks.whenObjectAdded { task ->
            if (task is SparkSubmit) {
                launchSpark {
                    dependsOn(task)
                }
            }
        }
    }
}