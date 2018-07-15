package cz.quantumleap.gradle.springboot;

import cz.quantumleap.gradle.project.RootProject;
import cz.quantumleap.gradle.project.SpringBootProject;
import cz.quantumleap.gradle.utils.ProjectUtils;
import org.gradle.api.Task;
import org.gradle.api.internal.ConventionTask;
import org.gradle.api.plugins.WarPlugin;
import org.gradle.api.tasks.SourceSet;
import org.springframework.boot.gradle.plugin.SpringBootPlugin;
import org.springframework.boot.loader.tools.MainClassFinder;

import java.io.IOException;

public class SpringBootPluginConfigurer {

    private static final String PACKAGE_TASK_CONVENTION_NAME = "mainClassName";
    private static final String BOOT_RUN_TASK_NAME = "bootRun";
    private static final String RUN_TASK_CONVENTION_NAME = "main";
    private static final String MAIN_CLASS_NAME_PROPERTY = "mainClassName";

    public void configure(RootProject rootProject, SpringBootProject springBootProject) {
        rootProject.getPlugins().apply(SpringBootPlugin.class);

        // SpringBoot plugin is applied on rootProject but we need to search
        // for mainClass in a springBootProject. To do this we have to override
        // MainClassConventions of Spring Boot Gradle plugin by setting correct
        // mainClassName project property in our own implementation.
        rootProject.getTasksByName(SpringBootPlugin.BOOT_JAR_TASK_NAME, false).forEach(task ->
                setFindClassNameConventionToTask(task, PACKAGE_TASK_CONVENTION_NAME, rootProject, springBootProject));
        rootProject.getTasksByName(WarPlugin.WAR_TASK_NAME, false).forEach(task ->
                setFindClassNameConventionToTask(task, PACKAGE_TASK_CONVENTION_NAME, rootProject, springBootProject));
        rootProject.getTasksByName(BOOT_RUN_TASK_NAME, false).forEach(task ->
                setFindClassNameConventionToTask(task, RUN_TASK_CONVENTION_NAME, rootProject, springBootProject));
    }

    private void setFindClassNameConventionToTask(Task task, String conventionName, RootProject rootProject, SpringBootProject springBootProject) {
        ConventionTask conventionTask = (ConventionTask) task;
        conventionTask.conventionMapping(conventionName, () -> findAndSetMainClassName(rootProject, springBootProject));
    }

    private String findAndSetMainClassName(RootProject rootProject, SpringBootProject springBootProject) {
        String rootMainClassName = ProjectUtils.getExtraProperty(rootProject.getProject(), MAIN_CLASS_NAME_PROPERTY, null);
        if (rootMainClassName != null) {
            return rootMainClassName;
        }

        String mainClassName = ProjectUtils.getExtraProperty(springBootProject.getProject(), MAIN_CLASS_NAME_PROPERTY, null);

        if (mainClassName != null) {
            ProjectUtils.setExtraProperty(rootProject.getProject(), MAIN_CLASS_NAME_PROPERTY, mainClassName);
        } else {
            try {
                SourceSet mainSourceSet = ProjectUtils.getSourceSets(springBootProject.getProject()).getByName(SourceSet.MAIN_SOURCE_SET_NAME);
                mainClassName = MainClassFinder.findMainClass(mainSourceSet.getOutput().getSingleFile());
                ProjectUtils.setExtraProperty(rootProject.getProject(), MAIN_CLASS_NAME_PROPERTY, mainClassName);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        return mainClassName;
    }
}
