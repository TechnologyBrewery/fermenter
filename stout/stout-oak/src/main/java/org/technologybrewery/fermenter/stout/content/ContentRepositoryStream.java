package org.technologybrewery.fermenter.stout.content;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.core.StreamingOutput;

/**
 * A common class to allow streaming of a {@link ContentRepository} file in an RESTful response.
 */
public class ContentRepositoryStream implements StreamingOutput {

    private ContentRepository contentRepository;
    private String folderName;
    private String fileName;

    /**
     * New instance.
     * 
     * @param folderName
     *            name of the folder from which to stream
     * @param fileName
     *            name of the file to stream
     * @param contentRepository
     *            the content repository instance
     */
    public ContentRepositoryStream(String folderName, String fileName, ContentRepository contentRepository) {
        this.folderName = folderName;
        this.fileName = fileName;
        this.contentRepository = contentRepository;
    }

    /**
     * Writes the content from the repository to the passed {@link OutputStream}.
     * 
     * {@inheritDoc}
     */
    @Override
    public void write(OutputStream outputStream) throws IOException {
        contentRepository.loadFile(folderName, fileName, outputStream);
    }

}
