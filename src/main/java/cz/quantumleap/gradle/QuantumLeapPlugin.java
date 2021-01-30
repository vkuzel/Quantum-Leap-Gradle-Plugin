package cz.quantumleap.gradle;

import cz.quantumleap.gradle.jooq.JooqDomainObjectsGeneratorConfigurer;
import cz.quantumleap.gradle.moduledependencies.ModuleDependenciesConfigurer;
import cz.quantumleap.gradle.project.ProjectManager;
import cz.quantumleap.gradle.springboot.SpringBootPluginConfigurer;
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin;
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension;
import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.internal.jvm.Jvm;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;

public class QuantumLeapPlugin implements Plugin<Project> {

    private static final String SPRING_BOOT_BOM = "org.springframework.boot:spring-boot-dependencies:" + SpringBootPlugin.class.getPackage().getImplementationVersion();
    private static final String JITPACK_REPOSITORY = "https://jitpack.io";

    private final ModuleDependenciesConfigurer moduleDependenciesConfigurer = new ModuleDependenciesConfigurer();
    private final SpringBootPluginConfigurer springBootPluginConfigurer = new SpringBootPluginConfigurer();
    private final JooqDomainObjectsGeneratorConfigurer jooqDomainObjectsGeneratorConfigurer = new JooqDomainObjectsGeneratorConfigurer();

    @Override
    public void apply(Project project) {
        validateJavaVersion();
        ProjectManager projectManager = new ProjectManager(project);
        apply(projectManager);
    }

    private void apply(ProjectManager projectManager) {
        projectManager.getAllProjects().forEach(this::configureStandardRepositoriesAndPlugins);
        springBootPluginConfigurer.configure(projectManager.getRootProject(), projectManager.getSpringBootProject());
        moduleDependenciesConfigurer.configure(projectManager.getSpringBootProject());
        jooqDomainObjectsGeneratorConfigurer.configure(projectManager.getSpringBootProject());
    }

    private void configureStandardRepositoriesAndPlugins(Project project) {
        project.getRepositories().mavenCentral();
        project.getRepositories().maven(mavenArtifactRepository -> mavenArtifactRepository.setUrl(JITPACK_REPOSITORY));

        project.getPlugins().apply(JavaLibraryPlugin.class);
        JavaPluginConvention javaPlugin = project.getConvention().getPlugin(JavaPluginConvention.class);
        javaPlugin.setSourceCompatibility(JavaVersion.VERSION_11);
        javaPlugin.setTargetCompatibility(JavaVersion.VERSION_11);
        project.getPlugins().apply(DependencyManagementPlugin.class);
        project.getExtensions().getByType(DependencyManagementExtension.class)
                .imports(importsHandler -> importsHandler.mavenBom(SPRING_BOOT_BOM));
    }

    private void validateJavaVersion() {
        JavaVersion javaVersion = Jvm.current().getJavaVersion();
        int version = javaVersion != null ? javaVersion.ordinal() + 1 : -1;
        if (version >= 14) {
            String msg = "Java 14 and newer are not supported at this moment!\n" +
                    "\n" +
                    "The project uses jOOQ which has a class name `org.jooq.Record` conflicting with\n" +
                    "new `java.lang.Record`. This causes an error if your IDE optimize imports to\n" +
                    "auto-import.\n" +
                    "\n" +
                    "This can be fixed by specifying explicit import and configuring your IDE to not\n" +
                    "to optimize imports. But for now, let's just not use Java 14 and newer versions\n" +
                    "until a proper solution will be created in jOOQ project or in IDEA.\n" +
                    "\n" +
                    "* https://github.com/jOOQ/jOOQ/issues/9988\n" +
                    "* https://youtrack.jetbrains.com/issue/IDEA-251602";
            throw new IllegalStateException(msg);
        }
    }
}
