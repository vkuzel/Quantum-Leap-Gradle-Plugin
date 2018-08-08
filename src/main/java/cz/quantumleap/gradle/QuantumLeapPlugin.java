package cz.quantumleap.gradle;

import cz.quantumleap.gradle.jooq.JooqDomainObjectsGeneratorConfigurer;
import cz.quantumleap.gradle.moduledependencies.ModuleDependenciesConfigurer;
import cz.quantumleap.gradle.project.ProjectManager;
import cz.quantumleap.gradle.springboot.SpringBootPluginConfigurer;
import cz.quantumleap.gradle.testfixturessourceset.TestFixturesSourceSetConfigurer;
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin;
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension;
import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;

public class QuantumLeapPlugin implements Plugin<Project> {

    private static final String SPRING_BOOT_BOM = "org.springframework.boot:spring-boot-dependencies:" + SpringBootPlugin.class.getPackage().getImplementationVersion();
    private static final String JITPACK_REPOSITORY = "https://jitpack.io";

    private final ModuleDependenciesConfigurer moduleDependenciesConfigurer = new ModuleDependenciesConfigurer();
    private final SpringBootPluginConfigurer springBootPluginConfigurer = new SpringBootPluginConfigurer();
    private final TestFixturesSourceSetConfigurer testFixturesSourceSetConfigurer = new TestFixturesSourceSetConfigurer();
    private final JooqDomainObjectsGeneratorConfigurer jooqDomainObjectsGeneratorConfigurer = new JooqDomainObjectsGeneratorConfigurer();

    @Override
    public void apply(Project project) {
        ProjectManager projectManager = new ProjectManager(project);

        apply(projectManager);
    }

    private void apply(ProjectManager projectManager) {
        projectManager.getAllProjects().forEach(this::configureStandardRepositoriesAndPlugins);
        springBootPluginConfigurer.configure(projectManager.getRootProject(), projectManager.getSpringBootProject());
        moduleDependenciesConfigurer.configure(projectManager.getSpringBootProject());
        projectManager.getAllProjects().forEach(testFixturesSourceSetConfigurer::configure);
        jooqDomainObjectsGeneratorConfigurer.configure(projectManager.getSpringBootProject());
    }

    private void configureStandardRepositoriesAndPlugins(Project project) {
        project.getRepositories().mavenCentral();
        project.getRepositories().maven(mavenArtifactRepository -> mavenArtifactRepository.setUrl(JITPACK_REPOSITORY));

        project.getPlugins().apply(JavaPlugin.class);
        // Spring Boot is not ready for Jigsaw yet. Also, there is a collision between deprecated Java EE (JAXB) modules
        // and external libraries that should replace those in Java 11. Until Java 11 and until full modularization of
        // Spring Boot the Quantum Leap code will be Java 8 compatible only.
        project.getConvention().getPlugin(JavaPluginConvention.class)
                .setSourceCompatibility(JavaVersion.VERSION_1_8);
        project.getPlugins().apply(DependencyManagementPlugin.class);
        project.getExtensions().getByType(DependencyManagementExtension.class)
                .imports(importsHandler -> importsHandler.mavenBom(SPRING_BOOT_BOM));
    }
}
