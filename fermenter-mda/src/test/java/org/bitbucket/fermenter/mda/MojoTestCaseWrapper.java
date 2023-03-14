package org.bitbucket.fermenter.mda;

import org.apache.commons.io.input.XmlStreamReader;
import org.apache.maven.DefaultMaven;
import org.apache.maven.Maven;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.execution.*;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptorBuilder;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.repository.RepositorySystem;
import org.apache.maven.session.scope.internal.SessionScope;
import org.codehaus.plexus.component.configurator.ComponentConfigurator;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.util.InterpolationFilterReader;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.internal.impl.SimpleLocalRepositoryManagerFactory;
import org.eclipse.aether.repository.LocalRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Wraps the default behavior provided by the Maven plugin testing harness through {@link AbstractMojoTestCase} to
 * set standard Maven defaults on any {@link Mojo}s that are created and reduce the amount of mock stubs required.
 * <p>
 * This class is largely adapted from the testing approach developed by the license-audit-maven-plugin's
 * {@code BetterAbstractMojoTestCase} (https://github.com/ahgittin/license-audit-maven-plugin)
 */
public class MojoTestCaseWrapper extends AbstractMojoTestCase {
    public void configurePluginTestHarness() throws Exception {
        super.setUp();
    }

    public void tearDownPluginTestHarness() throws Exception {
        super.tearDown();
    }

    /**
     * Creates a new {@link MavenSession} with standard defaults populated.
     *
     * @return
     * @throws Exception
     */
    public MavenSession newMavenSession() throws Exception {
        MavenExecutionRequest request = new DefaultMavenExecutionRequest();
        MavenExecutionResult result = new DefaultMavenExecutionResult();

        // Populates sensible defaults, including repository basedir and remote repos
        MavenExecutionRequestPopulator populator = getContainer().lookup(MavenExecutionRequestPopulator.class);
        populator.populateDefaults(request);

        // Enables the usage of Java system properties for interpolation and profile activation
        request.setSystemProperties(System.getProperties());

        // Ensures that the repo session in the maven session
        // has a repo manager and it points at the local repo
        DefaultMaven maven = (DefaultMaven) getContainer().lookup(Maven.class);
        DefaultRepositorySystemSession repoSession =
            (DefaultRepositorySystemSession) maven.newRepositorySession(request);
        repoSession.setLocalRepositoryManager(
            new SimpleLocalRepositoryManagerFactory().newInstance(repoSession,
                new LocalRepository(request.getLocalRepository().getBasedir())));

        @SuppressWarnings("deprecation")
        MavenSession session = new MavenSession(getContainer(),
            repoSession,
            request, result);
        return session;

    }

    /**
     * Extends the parent implementation of this method to delegate to the new {@link #newMavenSession()} introduced
     * in {@link MojoTestCaseWrapper}, which sets the defaults that are normally expected by Maven
     */
    @Override
    protected MavenSession newMavenSession(MavenProject project) {
        MavenSession session;
        try {
            session = newMavenSession();
            // seed the session so it can be injected during initialization
            SessionScope sessionScope = getContainer().lookup(SessionScope.class);
            sessionScope.enter();
            sessionScope.seed(MavenSession.class, session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        session.setCurrentProject(project);
        session.setProjects(Arrays.asList(project));
        return session;
    }

    /**
     * Creates and appropriately configures the {@link Mojo} responsible for executing the given
     * plugin goal as defined within the given {@code pom.xml} {@link File}.
     *
     * @param pom  {@code pom.xml} file defining desired plugin and configuration to test.
     * @param goal target plugin goal for which to create the associated {@link Mojo}
     * @return
     * @throws Exception
     */
    public Mojo lookupConfiguredMojo(File pom, String goal) throws Exception {
        assertNotNull(pom);
        assertTrue(pom.exists());

        ProjectBuildingRequest buildingRequest = newMavenSession().getProjectBuildingRequest();
        ProjectBuilder projectBuilder = lookup(ProjectBuilder.class);
        MavenProject project = projectBuilder.build(pom, buildingRequest).getProject();

        return lookupConfiguredMojo(project, goal);
    }
}
