package org.technologybrewery.fermenter.mda.reporting;

import java.io.File;
import java.io.Writer;

/**
 * A {@link Writer} to be used with a {@link org.apache.velocity.Template} to capture statistics about the final result
 * of template expansion.
 */
public final class StatsCollectingWriter extends Writer {
    private final StatisticsService statisticsService;
    private final FileStats stats;

    public StatsCollectingWriter(StatisticsService statisticsService, File destination) {
        this.statisticsService = statisticsService;
        stats = new FileStats(destination.getAbsolutePath());
    }

    @Override
    public void write(char cbuf[], int off, int len) {
        stats.addBytesWritten(len);
        long lines = 0;
        for (int i = 0; i < len; i++) {
            char c = cbuf[i+off];
            if (c == '\n') {
                lines++;
            }
        }
        stats.addLinesWritten(lines);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
        statisticsService.updateGeneratedFileStats(stats);
    }
}
