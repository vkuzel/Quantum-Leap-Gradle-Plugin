package cz.quantumleap.gradle;

import cz.quantumleap.gradle.jooq.JooqDomainObjectsGeneratorConfigurer;
import cz.quantumleap.gradle.moduledependencies.ModuleDependenciesConfigurer;
import cz.quantumleap.gradle.project.ProjectManager;
import cz.quantumleap.gradle.project.RootProject;
import cz.quantumleap.gradle.project.SubProject;
import cz.quantumleap.gradle.springboot.SpringBootPluginConfigurer;
import cz.quantumleap.gradle.testfixturessourceset.TestFixturesSourceSetConfigurer;
import cz.quantumleap.gradle.utils.ProjectUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.springframework.boot.gradle.dependencymanagement.DependencyManagementPluginFeatures;

import javax.inject.Inject;

public class QuantumLeapPlugin implements Plugin<Project> {

    private static final String JITPACK_REPOSITORY = "https://jitpack.io";

    private static final String THYMELEAF_VERSION_PROPERTY_NAME = "thymeleaf.version";
    private static final String THYMELEAF_VERSION = "3.0.3.RELEASE";
    private static final String THYMELEAF_LAYOUT_DIALECT_VERSION_PROPERTY_NAME = "thymeleaf-layout-dialect.version";
    private static final String THYMELEAF_LAYOUT_DIALECT_VERSION = "2.1.2";
    private static final String THYMELEAF_EXTRAS_SPRINGSECURITY4_VERSION_PROPERTY_NAME = "thymeleaf-extras-springsecurity4.version";
    private static final String THYMELEAF_EXTRAS_SPRINGSECURITY4_VERSION = "3.0.1.RELEASE";
    private static final String THYMELEAF_EXTRAS_JAVA8TIME_VERSION_PROPERTY_NAME = "thymeleaf-extras-java8time.version";
    private static final String THYMELEAF_EXTRAS_JAVA8TIME_VERSION = "3.0.0.RELEASE";

    private final ModuleDependenciesConfigurer moduleDependenciesConfigurer;
    private final SpringBootPluginConfigurer springBootPluginConfigurer;
    private final TestFixturesSourceSetConfigurer testFixturesSourceSetConfigurer;
    private final JooqDomainObjectsGeneratorConfigurer jooqDomainObjectsGeneratorConfigurer;
    private final DependencyManagementPluginFeatures dependencyManagementPluginFeatures;

    @Inject
    public QuantumLeapPlugin() {
        this(
                new ModuleDependenciesConfigurer(),
                new SpringBootPluginConfigurer(),
                new TestFixturesSourceSetConfigurer(),
                new JooqDomainObjectsGeneratorConfigurer(),
                new DependencyManagementPluginFeatures()
        );
    }

    private QuantumLeapPlugin(
            ModuleDependenciesConfigurer moduleDependenciesConfigurer,
            SpringBootPluginConfigurer springBootPluginConfigurer,
            TestFixturesSourceSetConfigurer testFixturesSourceSetConfigurer,
            JooqDomainObjectsGeneratorConfigurer jooqDomainObjectsGeneratorConfigurer,
            DependencyManagementPluginFeatures dependencyManagementPluginFeatures
    ) {
        this.moduleDependenciesConfigurer = moduleDependenciesConfigurer;
        this.springBootPluginConfigurer = springBootPluginConfigurer;
        this.testFixturesSourceSetConfigurer = testFixturesSourceSetConfigurer;
        this.jooqDomainObjectsGeneratorConfigurer = jooqDomainObjectsGeneratorConfigurer;
        this.dependencyManagementPluginFeatures = dependencyManagementPluginFeatures;
    }

    @Override
    public void apply(Project project) {
        ProjectManager projectManager = new ProjectManager(project);

        projectManager.getAllProjects().forEach(this::configureStandardRepositories);

        projectManager.getSubProjects().forEach(this::configureStandardPlugins);

        moduleDependenciesConfigurer.configure(projectManager.getSpringBootProject());

        springBootPluginConfigurer.configure(projectManager.getRootProject(), projectManager.getSpringBootProject());

        projectManager.getAllProjects().forEach(testFixturesSourceSetConfigurer::configure);

        projectManager.getAllProjects().forEach(jooqDomainObjectsGeneratorConfigurer::configure);

        configureThymeleafDependencies(projectManager.getRootProject());
    }

    private void configureStandardRepositories(Project project) {
        project.getRepositories().mavenCentral();
        project.getRepositories().maven(mavenArtifactRepository -> mavenArtifactRepository.setUrl(JITPACK_REPOSITORY));
    }

    private void configureStandardPlugins(SubProject subProject) {
        dependencyManagementPluginFeatures.apply(subProject);
        subProject.getPlugins().apply(JavaPlugin.class);
    }

    private void configureThymeleafDependencies(RootProject rootProject) {
        ProjectUtils.setExtraProperty(rootProject, THYMELEAF_VERSION_PROPERTY_NAME, THYMELEAF_VERSION);
        ProjectUtils.setExtraProperty(rootProject, THYMELEAF_LAYOUT_DIALECT_VERSION_PROPERTY_NAME, THYMELEAF_LAYOUT_DIALECT_VERSION);
        ProjectUtils.setExtraProperty(rootProject, THYMELEAF_EXTRAS_SPRINGSECURITY4_VERSION_PROPERTY_NAME, THYMELEAF_EXTRAS_SPRINGSECURITY4_VERSION);
        ProjectUtils.setExtraProperty(rootProject, THYMELEAF_EXTRAS_JAVA8TIME_VERSION_PROPERTY_NAME, THYMELEAF_EXTRAS_JAVA8TIME_VERSION);
    }
}
