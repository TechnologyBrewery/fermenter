package org.bitbucket.fermenter.mda;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Removes transient code generated by Fermenter.
 */
@Mojo(name="clean")
public class CleanMojo extends AbstractMojo {

    /**
     * Base Java source directory. All generated Java source code will wind up
     * underneath this folder. This folder will be cleaned as part of the clean
     * target, and its contents should not be put under version control.
     */
    @Parameter(required=true,defaultValue="${project.basedir}/src/generated")
    private File generatedBasedir;

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        removeDirectory(generatedBasedir);
    }

    private void removeDirectory(File dir) throws MojoExecutionException {
        if (dir != null) {
            if (dir.exists() && dir.isDirectory()) {
                getLog().info("Deleting " + dir.getAbsolutePath());
                try {
                    FileUtils.deleteDirectory(dir);
                } catch (IOException e) {
                    getLog().error("Problem encountered removing a directory!", e);
                }
            }
        }
    }

}