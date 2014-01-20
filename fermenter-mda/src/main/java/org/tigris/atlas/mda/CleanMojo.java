package org.tigris.atlas.mda;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @goal clean
 * @description Generates Java code from XML document(s)
 */
public class CleanMojo extends AbstractMojo {

	/**
	 * Base Java source directory.  All generated Java source code will wind
	 * up underneath this folder.  This folder will be cleaned as part of
	 * the clean target, and its contents should not be put under version
	 * control.
	 * 
	 * @parameter expression="${project.basedir}/src/generated"
	 * @required
	 */
	private File generatedBasedir;
		
		/**
	 	 * @parameter expression="${project.basedir}/src/xmlbeans"
		 * @required
		 */
		private File xmlBeansBasedir;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		removeDirectory(generatedBasedir);
		removeDirectory(xmlBeansBasedir);
	}    
	
	private void removeDirectory( File dir ) throws MojoExecutionException {
        if ( dir != null ) {
            if ( dir.exists() && dir.isDirectory() ) {
                getLog().info( "Deleting directory " + dir.getAbsolutePath() );
                removeDir( dir );
            }
        }
    }

    /**
     * Accommodate Windows bug encountered in both Sun and IBM JDKs.
     * Others possible. If the delete does not work, call System.gc(),
     * wait a little and try again.
     */
    private boolean delete( File f )
    {
        if ( !f.delete() )
        {
            if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "windows" ) > -1 )
            {
                System.gc();
            }
            try
            {
                Thread.sleep( 10 );
                return f.delete();
            }
            catch ( InterruptedException ex )
            {
                return f.delete();
            }
        }
        return true;
    }

    /**
     * Delete a directory
     *
     * @param d the directory to delete
     */
    protected void removeDir( File d )
        throws MojoExecutionException
    {
        String[] list = d.list();
        if ( list == null )
        {
            list = new String[0];
        }
        for ( int i = 0; i < list.length; i++ )
        {
            String s = list[i];
            File f = new File( d, s );
            if ( f.isDirectory() )
            {
                removeDir( f );
            }
            else
            {
                if ( !delete( f ) )
                {
                    String message = "Unable to delete file " + f.getAbsolutePath();
// TODO:...
//                    if ( failOnError )
//                    {
                        throw new MojoExecutionException( message );
//                    }
//                    else
//                    {
//                        getLog().info( message );
//                    }
                }
            }
        }

        if ( !delete( d ) )
        {
            String message = "Unable to delete directory " + d.getAbsolutePath();
// TODO:...
//            if ( failOnError )
//            {
                throw new MojoExecutionException( message );
//            }
//            else
//            {
//                getLog().info( message );
//            }
        }
    }
	
}
