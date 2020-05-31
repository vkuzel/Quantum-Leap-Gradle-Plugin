# Quantum Leap Gradle Plugin

[![](https://jitpack.io/v/vkuzel/Quantum-Leap-Gradle-Plugin.svg)](https://jitpack.io/#vkuzel/Quantum-Leap-Gradle-Plugin)

Gradle build plugin for the Quantum Leap project, so the project's build scripts can be smaller and cleaner.
Probably its not useful for anything else except for Quantum Leap, but you can check it out and get some inspiration from it.

Quantum Leap is a project template for building Java multi-module web applications backed by Spring Boot, jOOQ, Thymeleaf and PostgreSQL.
The plugin expects following project structure.

````
root project <-- Here's applied the plugin
|
+--- core module <-- Module that contains main class annotated by @SpringBootProject annotation. Name of the module has to be "core".
|
\--- some other modules
````

## Features

* Applies [Spring Boot Gradle plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-gradle-plugin.html) to a root project and fixes Spring Boot's `MainClassConvention` responsible for finding main class in the project.
* New `generateJooqDomainObjects` task, generates jOOQ domain objects to `src/generated/java` directories.
  * Generator configuration is read from `db/jooq-generator-configuration.xml` file located in resources of any of modules.
  * Database configuration is read from `config/application-default.properties` properties file, also located in resources.
    Following properties are recognized:
    * spring.datasource.url
    * spring.datasource.username
    * spring.datasource.password
* New `discoverProjectDependencies` task. It discovers dependencies between project's modules and serializes this structure into [`projectDependencies.ser`](https://github.com/vkuzel/Gradle-Project-Dependencies) files and puts those into the resources directory of every module.
  Structure is used in Quantum Leap project to sort resources loaded from various modules, etc.
* Upgrades Thymeleaf to version 3.x.
* Adds [Maven Central Repository](http://search.maven.org) and [JitPack Repository](https://jitpack.io) to all projects in the multi-project build.
* Configures new `testFixtures` source set for every module. The source set should be used for test classes between modules. This is inspired by [testFixtures dependencies in Gradle project](https://github.com/gradle/gradle/blob/master/gradle/testFixtures.gradle).

  To use sources from the `textFixtures` source set, use following configuration.

  ```kotlin
  dependencies {
      // Notice the name of configuration!
      testCompile(project(path = ":core-module", configuration = "testFixturesUsageCompile"))  
      // Or you can depend on runtime-only configuration.
      testRuntime(project(path = ":core-module", configuration = "testFixturesUsageRuntime"))  
  }
  ```
  The `testFixtures` source set can depend on other projects by declaring `testFixturesCompile` or `testFixturesRuntime` dependencies.
  ```kotlin
  dependencies {
      testFixturesCompile("some:library:1.0.0")
      testFixturesRuntime("other:library:1.0.0")
  }
  ```

## Getting Started

### Configuration

The plugin uses new Gradle [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block) to avoid some problems connected to a Kotlin build scripts and an old way of apply plugins.

1. Add the plugin repository to your project's `settings.gradle.kts` file.

    ```kotlin
    pluginManagement {
        repositories {
            mavenCentral()
            maven(url = "https://jitpack.io")
        }
    }
    ```
    
2. Apply the plugin to your root project's `build.gradle.kts`.

    ```kotlin
    plugins {
        id("cz.quantumleap") version "2.3.0"
    }
    ```

3. Optionally specify locataion of a main class in a module that contains it.

    ```kotlin
    ext {
        // Parameter mainClassName allows you to set Spring Boot's main class
        // explicitly and to suppress findMainClass task.
        mainClassName = "your.class.Name"
    }
    ```

### Run

1. In the root project run the `generateModuleDependencies` to generate and store module dependencies to resources files.

    ```bash
    gradle generateModuleDependencies
    ```

2. Run the `generateJooqDomainObjects` task to generate domain objects from the database schema. Generated classes will be stored in `src/generated/java` directory.
    
    ```bash
    gradle generateJooqDomainObjects
    ```

3. Start the application.

    ```bash
    gradle bootRun
    ```

## Development

1. Build the plugin into your local Maven repository

    ```bash
    gradle publishToMavenLocal
    ```

2. Configure your project to use the plugin located in Maven local

    ```kotlin
    // settings.gradle.kts
    pluginManagement {
        repositories {
            mavenLocal()
        }
    }
    ```

    ```kotlin
    // build.gradle.kts
    plugins {
        id("com.github.vkuzel.Quantum-Leap-Gradle-Plugin") version "correct version"
    }
    ```

## Library versions

* [Spring Boot](https://github.com/spring-projects/spring-boot) 2.3.0.RELEASE
* [ProjectDependencies](https://github.com/vkuzel/Gradle-Project-Dependencies) 3.0.0
