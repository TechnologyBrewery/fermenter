package org.bitbucket.fermenter.mda.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.google.common.base.CaseFormat;

/**
 * Provides common generator functionality - specifically around writing files, substituting file name variables, and
 * common VelocityContext setup.
 */
public abstract class AbstractGenerator implements Generator {

    protected static final String VERSION = "version";
    protected static final String ARTIFACT_ID = "artifactId";
    protected static final String GROUP_ID = "groupId";

    protected static final String CAPITALIZED_CAMEL_CASED_ARTIFACT_ID = "capitalizedCamelCasedArtifactId";
    protected static final String CAMEL_CASED_ARTIFACT_ID = "camelCasedArtifactId";
    private static final String UPPER_UNDERSCORE_ARTIFACT_ID = "upperUnderscoreArtifactId";

    protected String metadataContext;

    protected final void generateFile(GenerationContext gc, VelocityContext vc) {
        try {
            Template template = gc.getEngine().getTemplate(gc.getTemplateName());
            long templateLastModified = template.getLastModified();
            File baseFile = getBaseFile(gc);

            String baseFileName = getOutputSubFolder() + gc.getOutputFile();
            String tempFileName = baseFileName + ".tmp";

            File destinationFile = new File(baseFile, baseFileName);
            File tempFile = new File(baseFile, tempFileName);

            boolean isCleanWrite = false;
            if (destinationFile.exists()) {
                if (!gc.isOverwritable()) {
                    // never overwrite a CM-ed (by declaration) file
                    return;
                } else if (destinationFile.lastModified() < templateLastModified) {
                    // if the template is newer that the destination file, just overwrite
                    isCleanWrite = true;
                }

            } else {
                isCleanWrite = true;
            }

            File outputFile = (isCleanWrite || gc.isAppend()) ? destinationFile : tempFile;

            outputFile.getParentFile().mkdirs();

            Writer fw = new FileWriter(outputFile, true);
            template.merge(vc, fw);
            fw.close();

            if (!isCleanWrite) {
                if (gc.isAppend()) {
                    return;
                }
                boolean isContentEqual = FileUtils.contentEquals(destinationFile, tempFile);
                if (!isContentEqual) {
                    destinationFile.delete();
                    tempFile.renameTo(destinationFile);
                } else {
                    tempFile.deleteOnExit();
                }
            }

        } catch (Exception ex) {
            throw new GenerationException("Unable to generate file", ex);
        }
    }

    private File getBaseFile(GenerationContext gc) {
        File baseFile;
        if (gc.isOverwritable()) {
            baseFile = gc.getGeneratedSourceDirectory();
        } else {
            baseFile = gc.getMainSourceDirectory();
        }
        return baseFile;
    }

    /**
     * Provides common velocity attributes from the project perspective
     * 
     * @param gc
     *            The generation context for this generator
     * @return A defaulted <tt>VelocityContext</tt> instance
     */
    protected VelocityContext getNewVelocityContext(GenerationContext gc) {
        VelocityContext vc = new VelocityContext();
        // these are common attribute that are useful across context instances:
        vc.put(GROUP_ID, gc.getGroupId());
        vc.put(ARTIFACT_ID, gc.getArtifactId());
        vc.put(VERSION, gc.getVersion());

        String camelCasedArtifactId = getCamelCasedArtifactId(gc);
        vc.put(CAMEL_CASED_ARTIFACT_ID, camelCasedArtifactId);
        vc.put(CAPITALIZED_CAMEL_CASED_ARTIFACT_ID, StringUtils.capitalize(camelCasedArtifactId));
        vc.put(UPPER_UNDERSCORE_ARTIFACT_ID, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, camelCasedArtifactId));
        return vc;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMetadataContext(String metadataContext) {
        this.metadataContext = metadataContext;
    }

    private String getCamelCasedArtifactId(GenerationContext gc) {
        String upperCaseSubsequentWords = WordUtils.capitalizeFully(gc.getArtifactId(), '-');
        String lowerCaseFirstLetter = WordUtils.uncapitalize(upperCaseSubsequentWords);
        return lowerCaseFirstLetter.replace("-", "");
    }

    protected abstract String getOutputSubFolder();

    protected final String replaceBasePackage(String original, String basePackage) {
        return StringUtils.replace(original, "${basePackage}", basePackage);
    }

    protected final String replaceEntityName(String original, String entityName) {
        return StringUtils.replace(original, "${entityName}", entityName);
    }

    protected final String replaceServiceName(String original, String serviceName) {
        return StringUtils.replace(original, "${serviceName}", serviceName);
    }

    protected final String replaceEnumerationName(String original, String enumerationName) {
        return StringUtils.replace(original, "${enumerationName}", enumerationName);
    }

    protected final String replaceArtifactId(String original, String artifactId) {
        return StringUtils.replace(original, "${" + ARTIFACT_ID + "}", artifactId);
    }

    protected final String replaceCapitalizedCamelCasedArtifactId(String original,
            String capitalizedCamelCaseArtifactId) {
        return StringUtils.replace(original, "${" + CAPITALIZED_CAMEL_CASED_ARTIFACT_ID + "}",
                capitalizedCamelCaseArtifactId);
    }

}
