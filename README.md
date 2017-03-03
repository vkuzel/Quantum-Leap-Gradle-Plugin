# Quantum Leap Gradle Plugin

[![](https://jitpack.io/v/vkuzel/Quantum-Leap-Gradle-Plugin.svg)](https://jitpack.io/#vkuzel/Quantum-Leap-Gradle-Plugin)

Gradle build plugin for the Quantum Leap project so project's build scripts can be smaller and cleaner.
Probably not useful for anything except for Quantum Leap, still can serve as a good source of inspiration.

Quantum Leap (not published yet) is a project template for building Java multi-module web applications backed by Spring Boot, jOOQ, Thymeleaf and PostgreSQL.
The plugin expects following project structure.

````
root project <-- Here's applied the plugin
|
+--- core module <-- Module containing @SpringBootProject annotation, its name must be "core"
|
\--- some other modules
````

## Features

* Applies [Spring Boot Gradle plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-gradle-plugin.html) to a root project and fixes `findMainClass' task in multi-module projects.
* New `generateJooqDomainObjects` task, generates jOOQ domain objects to `src/generated/java` directories.
  * Generator configuration is read from `db/jooq-generator-configuration.xml` file located in resources of any of modules.
  * Database configuration is read from `config/application-default.properties` properties file, also located in resources.
    Following properties are recognized:
    * spring.datasource.url
    * spring.datasource.username
    * spring.datasource.password
* New `discoverProjectDependencies` task that discovers dependencies between project's modules and serializes this structure to [`projectDependencies.ser`](https://github.com/vkuzel/Gradle-Project-Dependencies) files into resources directory of every module.
  Structure is used in Quantum Leap project to sort resources loaded from various modules, etc.
* Upgrades Thymeleaf to version 3.x.
* Adds [Maven Central Repository](http://search.maven.org) and [JitPack Repository](https://jitpack.io) to all projects in the multi-project build.
* Configures new `testFixtures` source set for every module. The source set should be used for test classes between modules. This is inspired by [testFixtures dependencies in Gradle project](https://github.com/gradle/gradle/blob/master/gradle/testFixtures.gradle).

  To use sources from the `textFixtures` source set, use following configuration.

  ````groovy
  dependencies {
      // Notice the name of configuration!
      testCompile (path: ":core-module", configuration: "testFixturesUsageCompile")
      // Or you can depend on runtime configuration.
      testRuntime (path: ":core-module", configuration: "testFixturesUsageRuntime")
  }
  ````
  The `testFixtures` source set can depend on other projects by declaring `testFixturesCompile` or `testFixturesRuntime` dependencies.
  ````groovy
  dependencies {
      testFixturesCompile "some:library:1.0.0"
      testFixturesRuntime "other:library:1.0.0"
  }
  ````

## Getting Started

### Configuration

Add the plugin dependency into the root project's `build.gradle` file and apply the plugin.

````groovy
buildscript {
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.github.vkuzel:Quantum-Leap-Gradle-Plugin:1.0.1'
    }
}

apply plugin: 'cz.quantumleap'
````

Eventually specify location of a `@SpringBootApplication` class in a module containing the class.   

````groovy
ext {
    // Parameter mainClassName allows you to set Spring Boot's main class
    // explicitly and to suppress findMainClass task.
    mainClassName = "your.class.Name"
}
````

### Run

In root project run the `generateModuleDependencies` to generate and store module dependencies to resources files.

````bash
gradle generateModuleDependencies
````

Run the `generateJooqDomainObjects` task to generate domain objects from the database schema. Generated classes will be stored in `src/generated/java` directory.

````bash
gradle generateJooqDomainObjects
````

Start the application.

````bash
gradle bootRun
````

## Library versions

* [Spring Boot](https://github.com/spring-projects/spring-boot) 1.5.1.RELEASE
* [ProjectDependencies](https://github.com/vkuzel/Gradle-Project-Dependencies) 3.0.0
* [jOOQ](https://github.com/jOOQ/jOOQ) 3.9.1
* [Thymeleaf](https://github.com/thymeleaf) 3.0.3.RELEASE

