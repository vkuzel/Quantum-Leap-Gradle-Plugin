package cz.quantumleap.gradle.jooq;

import cz.quantumleap.gradle.utils.ProjectUtils;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSet;

public class JooqDomainObjectsGeneratorConfigurer {

    private static final String GENERATE_JOOQ_DOMAIN_OBJECTS_TASK_NAME = "generateJooqDomainObjects";

    public void configure(Project project) {
        addGeneratedSrcDirToMainSourceSet(project);

        GenerateJooqDomainObjectsTask generate = project.getTasks().create(GENERATE_JOOQ_DOMAIN_OBJECTS_TASK_NAME, GenerateJooqDomainObjectsTask.class);
        describeTask(generate);
    }

    private void addGeneratedSrcDirToMainSourceSet(Project project) {
        SourceSet mainSourceSet = ProjectUtils.getSourceSets(project).getByName(SourceSet.MAIN_SOURCE_SET_NAME);
        mainSourceSet.getJava().srcDir(GenerateJooqDomainObjectsTask.getGeneratedSrcPath(project));
    }

    private void describeTask(GenerateJooqDomainObjectsTask task) {
        String description = "TODO Fill description!";
        task.setDescription(description);
        task.setGroup("build");
    }
}
