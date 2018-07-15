plugins {
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "cz.quantumleap"
version = "2.0.3"

val springBootVersion = "2.0.3.RELEASE"
val gradleProjectDependenciesVersion = "3.0.0"
val jooqVersion = "3.10.7"
val postgreSqlVersion = "42.2.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
    implementation("com.github.vkuzel:Gradle-Project-Dependencies:$gradleProjectDependenciesVersion")
    implementation("org.jooq:jooq:$jooqVersion")
    implementation("org.jooq:jooq-meta:$jooqVersion")
    implementation("org.jooq:jooq-codegen:$jooqVersion")
    implementation("org.postgresql:postgresql:$postgreSqlVersion")

    testCompile("junit:junit:4.12")
    testCompile("org.mockito:mockito-core:2.15.0")
}

gradlePlugin {
    plugins {
        create("QuantumLeapPlugin") {
            id = "cz.quantumleap"
            implementationClass = "cz.quantumleap.gradle.QuantumLeapPlugin"
        }
    }
}
