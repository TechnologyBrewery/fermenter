package org.bitbucket.fermenter.mda.generator;

import jodd.io.StreamGobbler;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.GenerateSourcesHelper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Versioner {
    public static String FERM_CACHE_ROOT = ".fermenter/vc/";
    public static String FERM_FILE_SUFFIX = ".ferm";
    public static String DIGEST_SUFFIX = ".digest";

    private static String getDigest(String content) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String getFileContent(File ingest) throws IOException {
        return Files.readString(ingest.toPath());
    }

    private static String getPotentialContent(VelocityContext vc, GenerationContext gc) {
        StringWriter writer = new StringWriter();
        Template template = gc.getEngine().getTemplate(gc.getTemplateName());
        template.merge(vc, writer);
        return writer.toString();
    }

    private static void writeFile(File output, String content) throws IOException {
        output.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(output);
        fw.write(content);
        fw.close();
    }

    private static File getCacheContentFile(File baseDir, String destinationFile) {
        return new File(baseDir, FERM_CACHE_ROOT + destinationFile + Versioner.FERM_FILE_SUFFIX);
    }

    private static File getDigestContentFile(File baseDir, String destinationFile) {
        return new File(baseDir, FERM_CACHE_ROOT + destinationFile + Versioner.DIGEST_SUFFIX);
    }

    private static File getOutputFile(File baseDir, String destinationFile) {
        return new File(baseDir, destinationFile);
    }

    private static int executeCLI(List<String> commands) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commands);
        builder.directory(null);
        Process process = builder.start();
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream());
        Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        future.get();
        return exitCode;
    }

    protected static void invoke(
                            GenerationContext gc,
                            VelocityContext vc,
                            String destinationFile
    ) {
        if (gc.shouldSkipVersioning()) { return; }

        try {
            // If attempting to resolve conflicts, verify that we have kdiff3 before we do anything else
            if (gc.shouldResolveVersionConflicts() && executeCLI(Arrays.asList("which", "kdiff3")) != 0) {
                throw new UnsupportedOperationException(
                    "kdiff3 is required for versioning operations, but was not found in the PATH"
                );
            }

            String potentialContent = getPotentialContent(vc, gc);
            String potentialDigest = getDigest(potentialContent);

            File baseDir = gc.getProjectDirectory();
            File outputFile = getOutputFile(baseDir, destinationFile);
            File cacheContentFile = getCacheContentFile(baseDir, destinationFile);
            File cacheDigestFile = getDigestContentFile(baseDir, destinationFile);
            /*
             * First case: There is no existing form of the output file.  Act as a first-time generation, regardless
             * of what's in the cache.
             */
            if(!outputFile.exists()) {
                writeFile(cacheContentFile, potentialContent);
                writeFile(cacheDigestFile, potentialDigest);
                writeFile(outputFile, potentialContent);
                return;
            }
            /*
             * Second case: The output file exists, but there's no cache version.  We generate a new cache entry, and
             * prompt a one-time two-way merge, OR we want to force a re-sync regardless of prior state.
             */
            if (!getCacheContentFile(baseDir, destinationFile).exists()) {
                writeFile(cacheContentFile, potentialContent);
                writeFile(cacheDigestFile, potentialDigest);
                if (gc.shouldResolveVersionConflicts() && !gc.shouldSkipVersioningInitializationMerge()) {
                    executeCLI(Arrays.asList(
                        "kdiff3",
                        "-b", cacheContentFile.getPath(),
                        "-o", outputFile.getPath(),
                        "--L1", "Fresh Generation",
                        "--L2", "Your File",
                        outputFile.getPath()
                    ));
                } else {
                    promptVersionResolution(outputFile.getPath());
                }

                return;
            }
            /*
             * Third case: Cache file exists, and output file exists.  Digest mismatch between potential digest and
             * cache digest. Prompt a three-way merge, and then overwrite the existing cache entries.
             */
            File tmpFile = getCacheContentFile(baseDir, destinationFile + ".tmp");
            String cacheDigest = getFileContent(getDigestContentFile(baseDir, destinationFile));
            if(!potentialDigest.equals(cacheDigest)) {
                writeFile(tmpFile, potentialContent);

                if (gc.shouldResolveVersionConflicts()) {
                    executeCLI(Arrays.asList(
                        "kdiff3",
                        "-b", cacheContentFile.getPath(),
                        "-o", outputFile.getPath(),
                        "--L1", "Local Cache",
                        "--L2", "Your File",
                        "--L3", "Fresh Generation",
                        outputFile.getPath(),
                        tmpFile.getPath()
                    ));
                    writeFile(cacheContentFile, potentialContent);
                    writeFile(cacheDigestFile, potentialDigest);
                } else {
                    promptVersionResolution(outputFile.getPath());
                }

                tmpFile.delete();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<Path> getVersionFileList(File baseDir) throws IOException {
        File cacheBase = new File(baseDir, FERM_CACHE_ROOT);
        if (!cacheBase.exists()) {
            return new HashSet<>();
        }

        Stream<Path> stream = Files.walk(Paths.get(cacheBase.getPath()));
        return stream.filter(Files::isRegularFile)
            .filter(path -> path.toString().endsWith(Versioner.FERM_FILE_SUFFIX) || path.toString().endsWith(Versioner.DIGEST_SUFFIX))
            .map(path -> path.subpath(baseDir.toPath().getNameCount(), path.getNameCount()))
                .collect(Collectors.toSet());
    }

    public static void processNonGeneratedCacheMembers(
                                                    Set<Path> cached,
                                                    File baseDir,
                                                    GenerateSourcesHelper.LoggerDelegate logger,
                                                    boolean autodelete
    ) {
        cached.stream()
            .filter(path -> path.toString().endsWith(Versioner.FERM_FILE_SUFFIX))
            .forEach(path -> {
                File trueOutputFile = Versioner.getOutputFileForCached(path, baseDir);
                if (autodelete) trueOutputFile.delete();
                String actionTaken = autodelete ? "was automatically deleted" : "may be a candidate for deletion";
                logger.log(GenerateSourcesHelper.LoggerDelegate.LogLevel.WARN, String.format(
                    "File %s was not generated and %s!",
                    trueOutputFile.getPath(),
                    actionTaken));
            });
        cached.forEach(path -> new File(baseDir, path.toString()).delete());
    }

    public static File getOutputFileForCached(Path cachedFile, File baseDir) {
        String cleanedPath = cachedFile.subpath(
            Paths.get(Versioner.FERM_CACHE_ROOT).getNameCount(),
            cachedFile.getNameCount()
        ).toString();
        cleanedPath = cleanedPath.substring(0, cleanedPath.length() - Versioner.FERM_FILE_SUFFIX.length());
        return new File(baseDir, cleanedPath);
    }

    private static void promptVersionResolution(String outputFile) {
        GenerateSourcesHelper.logger.log(
            GenerateSourcesHelper.LoggerDelegate.LogLevel.WARN,
            String.format("Template change detected for %s.  To resolve version changes, run the build with " +
                "the -Dfermenter.resolveVersionConflicts=true -Dfermenter.resetVersionHistory arguments.", outputFile));
    }
}
