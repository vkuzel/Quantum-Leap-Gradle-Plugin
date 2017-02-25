package cz.quantumleap.gradle.project;

import groovy.lang.Closure;
import groovy.lang.MissingPropertyException;
import org.gradle.api.*;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.dsl.ArtifactHandler;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.component.SoftwareComponentContainer;
import org.gradle.api.file.*;
import org.gradle.api.initialization.dsl.ScriptHandler;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.LoggingManager;
import org.gradle.api.plugins.*;
import org.gradle.api.resources.ResourceHandler;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.WorkResult;
import org.gradle.process.ExecResult;
import org.gradle.process.ExecSpec;
import org.gradle.process.JavaExecSpec;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RootProject implements Project {

    private final Project project;

    public RootProject(Project project) {
        this.project = project;
    }

    @Override
    public int compareTo(Project o) {
        return project.compareTo(o);
    }

    @Override
    public PluginContainer getPlugins() {
        return project.getPlugins();
    }

    @Override
    public void apply(Closure closure) {
        project.apply(closure);
    }

    @Override
    public void apply(Action<? super ObjectConfigurationAction> action) {
        project.apply(action);
    }

    @Override
    public void apply(Map<String, ?> map) {
        project.apply(map);
    }

    @Override
    @Incubating
    public PluginManager getPluginManager() {
        return project.getPluginManager();
    }

    @Override
    public Project getRootProject() {
        return project.getRootProject();
    }

    @Override
    public File getRootDir() {
        return project.getRootDir();
    }

    @Override
    public File getBuildDir() {
        return project.getBuildDir();
    }

    @Override
    public void setBuildDir(Object o) {
        project.setBuildDir(o);
    }

    @Override
    public File getBuildFile() {
        return project.getBuildFile();
    }

    @Override
    public Project getParent() {
        return project.getParent();
    }

    @Override
    public String getName() {
        return project.getName();
    }

    @Override
    public String getDescription() {
        return project.getDescription();
    }

    @Override
    public void setDescription(String s) {
        project.setDescription(s);
    }

    @Override
    public Object getGroup() {
        return project.getGroup();
    }

    @Override
    public void setGroup(Object o) {
        project.setGroup(o);
    }

    @Override
    public Object getVersion() {
        return project.getVersion();
    }

    @Override
    public void setVersion(Object o) {
        project.setVersion(o);
    }

    @Override
    public Object getStatus() {
        return project.getStatus();
    }

    @Override
    public void setStatus(Object o) {
        project.setStatus(o);
    }

    @Override
    public Map<String, Project> getChildProjects() {
        return project.getChildProjects();
    }

    @Override
    public void setProperty(String s, Object o) throws MissingPropertyException {
        project.setProperty(s, o);
    }

    @Override
    public Project getProject() {
        return project.getProject();
    }

    @Override
    public Set<Project> getAllprojects() {
        return project.getAllprojects();
    }

    @Override
    public Set<Project> getSubprojects() {
        return project.getSubprojects();
    }

    @Override
    public Task task(String s) throws InvalidUserDataException {
        return project.task(s);
    }

    @Override
    public Task task(Map<String, ?> map, String s) throws InvalidUserDataException {
        return project.task(map, s);
    }

    @Override
    public Task task(Map<String, ?> map, String s, Closure closure) {
        return project.task(map, s, closure);
    }

    @Override
    public Task task(String s, Closure closure) {
        return project.task(s, closure);
    }

    @Override
    public String getPath() {
        return project.getPath();
    }

    @Override
    public List<String> getDefaultTasks() {
        return project.getDefaultTasks();
    }

    @Override
    public void setDefaultTasks(List<String> list) {
        project.setDefaultTasks(list);
    }

    @Override
    public void defaultTasks(String... strings) {
        project.defaultTasks(strings);
    }

    @Override
    public Project evaluationDependsOn(String s) throws UnknownProjectException {
        return project.evaluationDependsOn(s);
    }

    @Override
    public void evaluationDependsOnChildren() {
        project.evaluationDependsOnChildren();
    }

    @Override
    public Project findProject(String s) {
        return project.findProject(s);
    }

    @Override
    public Project project(String s) throws UnknownProjectException {
        return project.project(s);
    }

    @Override
    public Project project(String s, Closure closure) {
        return project.project(s, closure);
    }

    @Override
    public Map<Project, Set<Task>> getAllTasks(boolean b) {
        return project.getAllTasks(b);
    }

    @Override
    public Set<Task> getTasksByName(String s, boolean b) {
        return project.getTasksByName(s, b);
    }

    @Override
    public File getProjectDir() {
        return project.getProjectDir();
    }

    @Override
    public File file(Object o) {
        return project.file(o);
    }

    @Override
    public File file(Object o, PathValidation pathValidation) throws InvalidUserDataException {
        return project.file(o, pathValidation);
    }

    @Override
    public URI uri(Object o) {
        return project.uri(o);
    }

    @Override
    public String relativePath(Object o) {
        return project.relativePath(o);
    }

    @Override
    public ConfigurableFileCollection files(Object... objects) {
        return project.files(objects);
    }

    @Override
    public ConfigurableFileCollection files(Object o, Closure closure) {
        return project.files(o, closure);
    }

    @Override
    public ConfigurableFileTree fileTree(Object o) {
        return project.fileTree(o);
    }

    @Override
    public ConfigurableFileTree fileTree(Object o, Closure closure) {
        return project.fileTree(o, closure);
    }

    @Override
    public ConfigurableFileTree fileTree(Map<String, ?> map) {
        return project.fileTree(map);
    }

    @Override
    public FileTree zipTree(Object o) {
        return project.zipTree(o);
    }

    @Override
    public FileTree tarTree(Object o) {
        return project.tarTree(o);
    }

    @Override
    public File mkdir(Object o) {
        return project.mkdir(o);
    }

    @Override
    public boolean delete(Object... objects) {
        return project.delete(objects);
    }

    @Override
    public WorkResult delete(Action<? super DeleteSpec> action) {
        return project.delete(action);
    }

    @Override
    public ExecResult javaexec(Closure closure) {
        return project.javaexec(closure);
    }

    @Override
    public ExecResult javaexec(Action<? super JavaExecSpec> action) {
        return project.javaexec(action);
    }

    @Override
    public ExecResult exec(Closure closure) {
        return project.exec(closure);
    }

    @Override
    public ExecResult exec(Action<? super ExecSpec> action) {
        return project.exec(action);
    }

    @Override
    public String absoluteProjectPath(String s) {
        return project.absoluteProjectPath(s);
    }

    @Override
    public String relativeProjectPath(String s) {
        return project.relativeProjectPath(s);
    }

    @Override
    public AntBuilder getAnt() {
        return project.getAnt();
    }

    @Override
    public AntBuilder createAntBuilder() {
        return project.createAntBuilder();
    }

    @Override
    public AntBuilder ant(Closure closure) {
        return project.ant(closure);
    }

    @Override
    public ConfigurationContainer getConfigurations() {
        return project.getConfigurations();
    }

    @Override
    public void configurations(Closure closure) {
        project.configurations(closure);
    }

    @Override
    public ArtifactHandler getArtifacts() {
        return project.getArtifacts();
    }

    @Override
    public void artifacts(Closure closure) {
        project.artifacts(closure);
    }

    @Override
    public Convention getConvention() {
        return project.getConvention();
    }

    @Override
    public int depthCompare(Project project) {
        return this.project.depthCompare(project);
    }

    @Override
    public int getDepth() {
        return project.getDepth();
    }

    @Override
    public TaskContainer getTasks() {
        return project.getTasks();
    }

    @Override
    public void subprojects(Action<? super Project> action) {
        project.subprojects(action);
    }

    @Override
    public void subprojects(Closure closure) {
        project.subprojects(closure);
    }

    @Override
    public void allprojects(Action<? super Project> action) {
        project.allprojects(action);
    }

    @Override
    public void allprojects(Closure closure) {
        project.allprojects(closure);
    }

    @Override
    public void beforeEvaluate(Action<? super Project> action) {
        project.beforeEvaluate(action);
    }

    @Override
    public void afterEvaluate(Action<? super Project> action) {
        project.afterEvaluate(action);
    }

    @Override
    public void beforeEvaluate(Closure closure) {
        project.beforeEvaluate(closure);
    }

    @Override
    public void afterEvaluate(Closure closure) {
        project.afterEvaluate(closure);
    }

    @Override
    public boolean hasProperty(String s) {
        return project.hasProperty(s);
    }

    @Override
    public Map<String, ?> getProperties() {
        return project.getProperties();
    }

    @Override
    public Object property(String s) throws MissingPropertyException {
        return project.property(s);
    }

    @Override
    @Incubating
    public Object findProperty(String s) {
        return project.findProperty(s);
    }

    @Override
    public Logger getLogger() {
        return project.getLogger();
    }

    @Override
    public Gradle getGradle() {
        return project.getGradle();
    }

    @Override
    public LoggingManager getLogging() {
        return project.getLogging();
    }

    @Override
    public Object configure(Object o, Closure closure) {
        return project.configure(o, closure);
    }

    @Override
    public Iterable<?> configure(Iterable<?> iterable, Closure closure) {
        return project.configure(iterable, closure);
    }

    @Override
    public <T> Iterable<T> configure(Iterable<T> iterable, Action<? super T> action) {
        return project.configure(iterable, action);
    }

    @Override
    public RepositoryHandler getRepositories() {
        return project.getRepositories();
    }

    @Override
    public void repositories(Closure closure) {
        project.repositories(closure);
    }

    @Override
    public DependencyHandler getDependencies() {
        return project.getDependencies();
    }

    @Override
    public void dependencies(Closure closure) {
        project.dependencies(closure);
    }

    @Override
    public ScriptHandler getBuildscript() {
        return project.getBuildscript();
    }

    @Override
    public void buildscript(Closure closure) {
        project.buildscript(closure);
    }

    @Override
    public WorkResult copy(Closure closure) {
        return project.copy(closure);
    }

    @Override
    public CopySpec copySpec(Closure closure) {
        return project.copySpec(closure);
    }

    @Override
    public WorkResult copy(Action<? super CopySpec> action) {
        return project.copy(action);
    }

    @Override
    public CopySpec copySpec(Action<? super CopySpec> action) {
        return project.copySpec(action);
    }

    @Override
    public CopySpec copySpec() {
        return project.copySpec();
    }

    @Override
    public ProjectState getState() {
        return project.getState();
    }

    @Override
    public <T> NamedDomainObjectContainer<T> container(Class<T> aClass) {
        return project.container(aClass);
    }

    @Override
    public <T> NamedDomainObjectContainer<T> container(Class<T> aClass, NamedDomainObjectFactory<T> namedDomainObjectFactory) {
        return project.container(aClass, namedDomainObjectFactory);
    }

    @Override
    public <T> NamedDomainObjectContainer<T> container(Class<T> aClass, Closure closure) {
        return project.container(aClass, closure);
    }

    @Override
    public ExtensionContainer getExtensions() {
        return project.getExtensions();
    }

    @Override
    public ResourceHandler getResources() {
        return project.getResources();
    }

    @Override
    @Incubating
    public SoftwareComponentContainer getComponents() {
        return project.getComponents();
    }
}
