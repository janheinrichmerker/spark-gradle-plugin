package dev.reimer.spark.gradle.plugin

import org.apache.spark.launcher.SparkAppHandle
import org.apache.spark.launcher.SparkLauncher
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.property
import java.util.concurrent.CountDownLatch

@Suppress("UnstableApiUsage")
open class SparkSubmit : DefaultTask() {

    init {
        group = SparkPlugin.TASK_GROUP
    }

    @get:Optional
    @get:InputFile
    val propertiesFile: RegularFileProperty = project.objects.fileProperty()

    @get:Input
    val configuration: MapProperty<String, Any> = project.objects.mapProperty()

    @get:Optional
    @get:Input
    @get:Option(option = "application-name", description = "")
    val applicationName: Property<String> = project.objects.property()

    @get:Optional
    @get:Input
    @get:Option(option = "master", description = "")
    val master: Property<String> = project.objects.property()

    @get:Optional
    @get:Input
    @get:Option(option = "deploy-mode", description = "")
    val deployMode: Property<DeployMode> = project.objects.property()

    @get:InputFile
    val applicationResource: RegularFileProperty = project.objects.fileProperty()

    @get:Optional
    @get:Input
    @get:Option(option = "main-class", description = "")
    val mainClass: Property<String> = project.objects.property()

    @get:Input
    @get:Option(option = "application-arguments", description = "")
    val applicationArguments: ListProperty<String> = project.objects.listProperty()

    @get:InputFiles
    val jars: ConfigurableFileCollection = project.objects.fileCollection()

    @get:InputFiles
    val files: ConfigurableFileCollection = project.objects.fileCollection()

    @get:InputFiles
    val pythonFiles: ConfigurableFileCollection = project.objects.fileCollection()

    @get:Optional
    @get:Input
    @get:Option(option = "verbose", description = "")
    val verbose: Property<Boolean> = project.objects.property()

    @get:Optional
    @get:Input
    @get:Option(option = "java-home", description = "")
    val javaHome: Property<String> = project.objects.property()

    @get:Optional
    @get:Input
    @get:Option(option = "spark-home", description = "")
    val sparkHome: Property<String> = project.objects.property()

    @get:Optional
    @get:Input
    @get:Option(option = "await-completion", description = "")
    val awaitCompletion: Property<Boolean> = project.objects.property()

    private fun buildLauncher(): SparkLauncher {
        return SparkLauncher().apply {
            propertiesFile.orNull?.let { setPropertiesFile(it.asFile.absolutePath) }
            for ((key, value) in configuration.get()) {
                setConf(key, value.toString())
            }
            applicationName.orNull?.let { setAppName(it) }
            master.orNull?.let { setMaster(it) }
            deployMode.orNull?.let { setDeployMode(it.mode) }
            setAppResource(applicationResource.get().asFile.absolutePath)
            mainClass.orNull?.let { setMainClass(it) }
            for (argument in applicationArguments.get()) {
                addAppArgs(argument)
            }
            for (jar in jars) {
                addJar(jar.absolutePath)
            }
            for (file in files) {
                addFile(file.absolutePath)
            }
            for (file in pythonFiles) {
                addPyFile(file.absolutePath)
            }
            verbose.orNull?.let { setVerbose(it) }
            javaHome.orNull?.let { setJavaHome(it) }
            sparkHome.orNull?.let { setSparkHome(it) }
        }
    }

    private fun SparkLauncher.submitAndWait(): SparkAppHandle {
        val latch = CountDownLatch(1)
        val completionListener = object : SparkAppHandle.Listener {
            override fun infoChanged(handle: SparkAppHandle) {}
            override fun stateChanged(handle: SparkAppHandle) {
                if (handle.state.isFinal) latch.countDown()
            }
        }
        val handle = startApplication(completionListener)

        // Wait until the Spark app finished.
        latch.await()

        if (handle.state != SparkAppHandle.State.FINISHED) {
            throw GradleException("The Spark app did not finish successfully. (State: ${handle.state})")
        }

        return handle
    }

    private fun SparkLauncher.submit(): SparkAppHandle = startApplication()

    @TaskAction
    fun execute(): SparkAppHandle {
        val launcher = buildLauncher()
        return if (awaitCompletion.orNull == true) {
            launcher.submitAndWait()
        } else {
            launcher.submit()
        }
    }

    enum class DeployMode(val mode: String) {
        Cluster("cluster"), Client("client")
    }
}