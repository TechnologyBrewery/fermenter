package org.technologybrewery.fermenter.stout.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.nodetype.NodeType;
import javax.sql.DataSource;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.jcr.repository.RepositoryImpl;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;
import org.apache.jackrabbit.oak.plugins.document.DocumentStoreException;
import org.apache.jackrabbit.oak.plugins.document.rdb.RDBDocumentNodeStoreBuilder;
import org.technologybrewery.fermenter.stout.exception.UnrecoverableException;
import org.technologybrewery.fermenter.stout.util.KrauseningBasedSpringConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Provides access to Content repository features (e.g., saving streaming content).
 * 
 * See documentation at:
 * https://fermenter.atlassian.net/wiki/spaces/FER/pages/267681793/Saving+and+Retrieving+Binary+Content
 * 
 */
@Component
public class ContentRepository {

    private static final Logger logger = LoggerFactory.getLogger(ContentRepository.class);

    private static ContentConfig config = KrauseningConfigFactory.create(ContentConfig.class);

    private DocumentNodeStore nodeStore;
    private static DocumentNodeStore priorNodeStore;
    private Repository repository;
    private KrauseningBasedSpringConfig krauseningBasedSpringConfig;

    /**
     * Instantiates the content repository leveraging the datasource configuration used already in Stout Spring.
     * 
     * @param krauseningBasedSpringConfig
     *            stout-spring datasource configuration
     */
    public ContentRepository(KrauseningBasedSpringConfig krauseningBasedSpringConfig) {
        this.krauseningBasedSpringConfig = krauseningBasedSpringConfig;

    }

    /**
     * Initializes the repository. Can be called at any time (especially if a lease has failed and we need to
     * reinitialize the repository).
     */
    @PostConstruct
    void initRepository() {
        long start = System.currentTimeMillis();

        if (priorNodeStore != null) {
            logger.warn(
                    "A previously instaniated node store was encountered - you likely have multiple active Spring contexts in this classpath!");
            priorNodeStore.dispose();
            priorNodeStore = null;
            logger.warn("Prior node store removed.");
        }

        DataSource jdbcDataSource = krauseningBasedSpringConfig.krauseningDataSource();
        ContentRepositoryLeaseFailureHandler failureHandler = new ContentRepositoryLeaseFailureHandler(this);
        nodeStore = RDBDocumentNodeStoreBuilder.newRDBDocumentNodeStoreBuilder().setRDBConnection(jdbcDataSource)
                .setLeaseFailureHandler(failureHandler).build();

        // We generally don't want to track static variables in a Spring-managed "singleton". That said, there is an
        // edge case where multiple instances
        // can be created (e.g., mixing JUnit and Cucumber tests) and it creates long delays (~10s) while one
        // ContentRepostory waits for lease expiration
        // on the prior repository. As such, this fail-safe helps eliminate this case and has logging to note the issue
        // to the user above.
        priorNodeStore = nodeStore;

        Oak oak = new Oak(nodeStore);
        oak.with(defaultStoutScheduledExecutor());
        repository = new Jcr(oak).createRepository();

        long stop = System.currentTimeMillis();
        logger.info("Created a new connection to the underlying Oak repository in {}ms", (stop - start));

    }

    /**
     * Cleans up all the async threads that Oak leverages. This is important or else they can fail and churn out inifite
     * stack traces to the log.
     */
    @PreDestroy
    void cleanUp() {
        try {
            logger.info("Cleaning up ContentRepository resources...");
            RepositoryImpl repositoryImpl = (RepositoryImpl) repository;
            logger.info("Shutting down Oak Repository...");
            repositoryImpl.shutdown();
            logger.info("Shutting down Oak DocumentNodeStore...");
            nodeStore.dispose();

            repository = null;
            nodeStore = null;

        } catch (Exception e) {
            logger.error("Error in shutting down ContentRepository resources!", e);
        }

    }

    /**
     * Saves a file in the specified folder. If the folder does not yet exist, it will be created.
     * 
     * @param folderName
     *            folder in which to store a file
     * @param fileName
     *            the file name to use when storing content
     * @param inputStream
     *            the data to save
     */
    public void saveFile(String folderName, String fileName, InputStream inputStream) {
        long start = System.currentTimeMillis();

        Session session = null;
        try {

            session = getJcrSession();
            Node root = session.getRootNode();

            // get reference to the folder in which we are storing the file:
            Node folder = findOrCreateFolder(root, folderName);

            // save the file:
            Node fileNode = folder.addNode(fileName, NodeType.NT_FILE);
            Node contentNode = fileNode.addNode(JcrConstants.JCR_CONTENT, NodeType.NT_RESOURCE);

            Binary binary = session.getValueFactory().createBinary(inputStream);
            contentNode.setProperty(JcrConstants.JCR_DATA, binary);
            contentNode.setProperty(JcrConstants.JCR_LASTMODIFIED, System.currentTimeMillis());

            session.save();

            long stop = System.currentTimeMillis();

            logger.info("File {}/{} saved in {}ms", folderName, fileName, (stop - start));

        } catch (RepositoryException e) {
            throw new UnrecoverableException("Could not save a file " + folderName + "/" + fileName + "!", e);

        } finally {
            closeQuietly(session);
        }

    }

    /**
     * Retrieves a file specified by the given folder/file name combination, writing the content of the file to the
     * specified OutputStream.
     * 
     * @param folderName
     *            folder in which the file resides
     * @param fileName
     *            the name of file to retrieve
     * @param outputStream
     *            the stream to which content should be written
     */
    public void loadFile(String folderName, String fileName, OutputStream outputStream) {
        Session session = null;
        try {
            session = getJcrSession();
            Node file = getFileByName(folderName, fileName, session);
            Node fileContent = file.getNode(JcrConstants.JCR_CONTENT);
            Binary data = fileContent.getProperty(JcrConstants.JCR_DATA).getBinary();
            InputStream inputstream = data.getStream();
            long bytesCopies = IOUtils.copyLarge(inputstream, outputStream);
            logger.debug("Retrieved a file {}/{} from content repository (size = {} bytes)", folderName, fileName,
                    bytesCopies);
            data.dispose();

        } catch (PathNotFoundException pnfe) {
            logger.debug("No file {}/{} found in content repository!", folderName, fileName);

        } catch (RepositoryException | IOException e) {
            throw new UnrecoverableException("Could not load a file " + folderName + "/" + fileName + "!", e);

        } finally {
            closeQuietly(session);
        }

    }

    /**
     * Removes a file for the given folder/file name combination.
     * 
     * @param folderName
     *            folder in which the file resides
     * @param fileName
     *            the name of file to delete
     */
    public void deleteFile(String folderName, String fileName) {
        Session session = null;
        try {
            session = getJcrSession();
            Node file = getFileByName(folderName, fileName, session);
            file.remove();

            session.save();

        } catch (RepositoryException e) {
            throw new UnrecoverableException("Could not delete a file " + folderName + "/" + fileName + "!", e);

        } finally {
            closeQuietly(session);
        }
    }

    protected void closeQuietly(Session session) {
        try {
            if (session != null) {
                session.logout();
            }
        } catch (Exception e) {
            logger.error("Could not close Oak Session!", e);
        }
    }

    private Session getJcrSession() throws RepositoryException {
        Session session;
        try {
            session = loginToRepository();

        } catch (RepositoryException | DocumentStoreException e) {
            logger.warn("Issue connecting w/ the Oak Repository - throwing the old one out and creating a new one...");

            initRepository();
            session = loginToRepository();

        }
        return session;
    }

    protected Session loginToRepository() throws RepositoryException {
        Session session;

        if (repository == null) {
            // in this case, we probably had to tear the repository down due to a database being unavailable.
            // to prevent that from becoming a recurring issue (especially in development environments), we
            // lazily re-instantiate here to prevent a boom-bust cycle w/ the repo. The cost is a little bit of
            // a timing hit on the first request to the ContentRepository after a connectivity issue:
            initRepository();
        }

        try {
            session = performLogin();

        } catch (RejectedExecutionException e) {
            // clean up the current instance, then re-try once:
            cleanUp();
            session = performLogin();
        }
        return session;
    }

    protected Session performLogin() throws RepositoryException {
        return repository.login(
                new SimpleCredentials(config.getRepositoryUsername(), config.getRepositoryPassword().toCharArray()));
    }

    private Node findOrCreateFolder(Node node, String folderName) throws RepositoryException {
        Node folder = null;
        if (node.hasNode(folderName)) {
            folder = node.getNode(folderName);

        } else {
            folder = node.addNode(folderName, NodeType.NT_FOLDER);
            logger.info("Create new content repository folder: {}", folderName);

        }

        return folder;

    }

    private Node getFileByName(String folderName, String fileName, Session session) throws RepositoryException {
        Node root = session.getRootNode();

        Node folder = findOrCreateFolder(root, folderName);
        Node file = folder.getNode(fileName);
        return file;
    }

    /**
     * Default {@code ScheduledExecutorService} used for scheduling background tasks. This default spawns up to the
     * number specified in {@link ContentConfig}'s numberOfSchedulerThreads property on an as-needed basis. Idle threads
     * are pruned after one minute.
     * 
     * @return fresh ScheduledExecutorService
     */
    private static ScheduledExecutorService defaultStoutScheduledExecutor() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(config.getNumberOfSchedulerThreads(),
                new ThreadFactory() {
                    private final AtomicInteger counter = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r, createName());
                        thread.setDaemon(true);
                        return thread;
                    }

                    private String createName() {
                        return "stout-oak-scheduled-executor-" + counter.getAndIncrement();
                    }
                });
        executor.setKeepAliveTime(1, TimeUnit.MINUTES);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

}
