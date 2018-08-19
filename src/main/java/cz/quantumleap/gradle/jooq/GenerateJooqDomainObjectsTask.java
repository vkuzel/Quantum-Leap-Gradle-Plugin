package cz.quantumleap.gradle.jooq;

import cz.quantumleap.gradle.utils.ProjectUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Generator;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.Schema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerateJooqDomainObjectsTask extends DefaultTask {

    private static final String TEMP_DIR_PREFIX = "quantumleap-jooq";

    private static final String GENERATED_SRC_PATH = "src/generated/java";
    private static final String GENERATED_CLASSES_PACKAGE = "cz.quantumleap";
    private static final String GENERATED_CLASSES_PATH = "cz/quantumleap";

    private static final String JOOQ_GENERATOR_CONFIGURATION_PATH = "db/jooq-generator-configuration.xml";
    private static final String APPLICATION_CONFIGURATION_PATH = "config/application-default.properties";

    private static final String DATASOURCE_URL_PROPERTY_NAME = "spring.datasource.url";
    private static final String DATASOURCE_USERNAME_PROPERTY_NAME = "spring.datasource.username";
    private static final String DATASOURCE_PASSWORD_PROPERTY_NAME = "spring.datasource.password";

    @TaskAction
    public void generate() throws Exception {
        Project project = getProject();
        Path tempDirectory = Files.createTempDirectory(TEMP_DIR_PREFIX);
        Configuration configuration = initJooqGeneratorConfiguration(project, tempDirectory);
        GenerationTool.generate(configuration);
        moveGeneratedClassesToSpringBootProject(tempDirectory);
    }

    private Configuration initJooqGeneratorConfiguration(Project project, Path targetDirectory) throws IOException {
        Configuration configuration = loadJooqConfiguration();
        Generator generator = configuration.getGenerator();

        Properties properties = loadJdbcProperties();
        applyJdbcProperties(properties, configuration.getJdbc());

        List<Schema> schemata = getAllProjects().stream().map(this::createSchema).collect(Collectors.toList());
        generator.getDatabase().withSchemata(schemata);

        generator.getTarget().setDirectory(targetDirectory.toAbsolutePath().toString());
        generator.getTarget().setPackageName(GENERATED_CLASSES_PACKAGE);

        return configuration;
    }

    private void moveGeneratedClassesToSpringBootProject(Path generatedClassesPath) throws IOException {
        Path fromPath = generatedClassesPath.resolve(GENERATED_CLASSES_PATH);
        Path toPath = getGeneratedSrcPath(getProject()).toPath().resolve(GENERATED_CLASSES_PATH);

        Files.createDirectories(toPath);
        Files.walk(toPath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

        Files.move(fromPath, toPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private void applyJdbcProperties(Properties properties, Jdbc jdbc) {
        jdbc
                .withUrl(properties.getProperty(DATASOURCE_URL_PROPERTY_NAME))
                .withUser(properties.getProperty(DATASOURCE_USERNAME_PROPERTY_NAME))
                .withPassword(properties.getProperty(DATASOURCE_PASSWORD_PROPERTY_NAME));

    }

    private Configuration loadJooqConfiguration() throws IOException {
        File file = ProjectUtils.findFileInProjectResources(getProject().getRootProject(), JOOQ_GENERATOR_CONFIGURATION_PATH);

        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            return GenerationTool.load(inputStream);
        }
    }

    private Properties loadJdbcProperties() throws IOException {
        File file = ProjectUtils.findFileInProjectResources(getProject().getRootProject(), APPLICATION_CONFIGURATION_PATH);

        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            properties.load(inputStream);
        }
        return properties;
    }

    private Set<Project> getAllProjects() {
        return getProject().getRootProject().getAllprojects();
    }

    private Schema createSchema(Project project) {
        Schema schema = new Schema();
        schema.setInputSchema(project.getName());
        return schema;
    }

    static File getGeneratedSrcPath(Project project) {
        return new File(project.getProjectDir(), GENERATED_SRC_PATH);
    }
}
