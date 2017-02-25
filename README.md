# Quantum Leap Gradle Plugin

[![](https://jitpack.io/v/vkuzel/Quantum-Leap-Gradle-Plugin.svg)](https://jitpack.io/#vkuzel/Quantum-Leap-Gradle-Plugin)

Base plugin for Quantum Leap project that "configures everything" and makes project's build files smaller and cleaner.
Probably not useful for anybody outside the Quantum Leap but it can be used as a source of inspiration.

Plugin has been designed with following project structure in mind.

````
root project <-- Here's applied the plugin
|
+--- core module <-- Module containing @SpringBootProject annotation, its name must be "core"
|
\--- some other module
````

## Features

* Applies [Spring Boot Gradle plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-gradle-plugin.html) to a root project and fixes findMainClass task in multi-module project.
* Configures JOOQ domain objects generator.
  Generator reads it's configuration from `db/jooq-generator-configuration.xml` resource and database credentials from `config/application-default.properties` resource.
  Use the standard Spring Boot's `spring.datasource.` datasource configuration prefix in properties file.
* Adds new `discoverProjectDependencies` task that stores serialized version of project dependencies to file. This can be used while application's runtime to determine which sub-project should execute first, etc.
* Configures Thymeleaf to version 3.x.
* Adds [Maven Central Repository](http://search.maven.org) and [JitPack Repository](https://jitpack.io) to all projects in the multi-project build.
* Support for new `testFixtures` source set to store common test classes.
Heavily inspired by [testFixtures dependencies in Gradle project](https://github.com/gradle/gradle/blob/master/gradle/testFixtures.gradle).
Other projects of multi-project application can rely on this source set by declaring proper dependency.

  ````groovy
  dependencies {
      // Notice the name of configuration!
      testCompile (path: ":core-module", configuration: "testFixturesUsageCompile")
      // Or you can depend on runtime configuration.
      testRuntime (path: ":core-module", configuration: "testFixturesUsageRuntime")
  }
  ````
  The source set can depend on other projects by declaring `testFixturesCompile` or `testFixturesRuntime` dependencies.
  ````groovy
  dependencies {
      testFixturesCompile "some:library:1.0.0"
      testFixturesRuntime "other:library:1.0.0"
  }
  ````

## Getting Started

### Configuration

Add plugin dependency into the root project's `build.gradle` file. Then configure name of the Spring Boot sub-project and finally apply the plugin.

````groovy
buildscript {
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.github.vkuzel:Quantum-Leap-Gradle-Plugin:1.0.0'
    }
}

apply plugin: 'cz.quantumleap'
````

Eventually specify location of `@SpringBootApplication` class.   

````groovy
ext {
    // Parameter mainClassName allows you to set Spring Boot's main class
    // explicitly and to suppress findMainClass task.
    mainClassName = "your.class.Name"
}
````

### Run

In root project run the `generateModuleDependencies` to store module dependencies to resources files.
Domain class used to preserve dependencies can be found in [Gradle Project Dependencies](https://github.com/vkuzel/Gradle-Project-Dependencies).

````bash
gradle generateModuleDependencies
````

Run the `generateJooqDomainObjects` task to generate domain objects from the database.
Generated classes will be stored in `src/generated/java` directory.

````bash
gradle generateJooqDomainObjects
````

Start the application.

````bash
gradle bootRun
````
