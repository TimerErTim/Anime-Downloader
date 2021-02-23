package org.example.downloader.core.download;

import org.example.downloader.core.format.EpisodeFormat;
import org.example.downloader.core.framework.Downloader;
import org.example.downloader.core.framework.Series;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

public class CollectiveDownloadBuilder {
    private final static String DEFAULT_FORMAT_DOWNLOAD = "/S - /[S/sE/e /[/E/]/]/[/!sEpisode /e/]";
    private final static String DEFAULT_FORMAT_SUBDIR = "/S/[" + (File.separator.equals("/") ? "//" : File.separator) + "Season /s/]";

    private final Set<? extends Series> seriesSet;

    private int maxDownloads;

    private final String path;
    private String formatSubDir;
    private String formatDownload;

    /**
     * Creates a {@code CollectiveDownloadBuilder} object, which can be used to
     * download multiple {@code Episode}s/{@code Series}.
     * <p>
     * If the amount of {@code Series} in the given {@code Set} is 1, by default
     * no subdirectories will be created.
     *
     * @param path      the destination folder
     * @param seriesSet a {@code Set} of {@link Series} which should be downloaded
     */
    public CollectiveDownloadBuilder(String path, Set<? extends Series> seriesSet) {
        this.seriesSet = seriesSet;
        this.path = path;
        maxDownloads = 4;
        setFormatSubDir(null); //Generate Default for Subdirectory creation
        formatDownload = DEFAULT_FORMAT_DOWNLOAD;
    }

    /**
     * Creates a {@code CollectiveDownloadBuilder} object, which can be used to
     * download multiple {@code Episode}s/{@code Series}.
     * If the amount of {@code Series} in the given varargs is 1, by default
     * no subdirectories will be created.
     *
     * @param path   the destination folder
     * @param series {@link Series} which should be downloaded
     */
    public CollectiveDownloadBuilder(String path, Series... series) {
        this(path, new HashSet<>(Arrays.asList(series)));
    }


    /**
     * Creates a {@code CollectiveDownloadBuilder} object, which can be used to
     * download multiple {@code Episode}s/{@code Series}.
     *
     * @param path        the destination folder
     * @param downloaders {@link Downloader}(s) which should be downloaded
     */
    public CollectiveDownloadBuilder(String path, Downloader... downloaders) {
        this(path, Series.custom(new LinkedHashSet<>(Arrays.asList(downloaders))));
    }

    /**
     * Builds a immutable {@code CollectiveDownload} object, which can be used to download
     * multiple files in a simple manner.
     *
     * @return a new {@code CollectiveDownload}
     */
    public CollectiveDownload build() {
        return new CollectiveDownload(new LinkedHashSet<>(seriesSet), path, formatSubDir, formatDownload, maxDownloads);
    }

    /**
     * Builds a immutable {@code CollectiveDownload} object, which can be used to download
     * multiple files in a simple manner, and starts it.
     *
     * @return a new started {@code CollectiveDownload}
     */
    public CollectiveDownload execute() {
        CollectiveDownload download = build();
        download.download();
        return download;
    }

    /**
     * Sets the maximum amount of parallel {@code Download}s.
     *
     * @param maxDownloads the maximum amount of parallel {@code Download}s
     */
    public void setMaxDownloads(int maxDownloads) {
        this.maxDownloads = maxDownloads;
    }

    /**
     * Sets the formatting of subdirectories.
     * <p>
     * The parameter controls the creation of subdirectories for every downloaded
     * episode/video. If the parameter is an empty string, no subdirectories
     * will be created. If however the parameter is null, the default subdirectory creation
     * will be used. The parameter is a formatting according to {@link EpisodeFormat#format(String)}.
     *
     * @param formatting empty -> no subdirectories<br>
     *                   null -> {@link CollectiveDownloadBuilder#DEFAULT_FORMAT_SUBDIR}<br>
     *                   everything else -> subdirectory with the given formatting
     */
    public void setFormatSubDir(String formatting) {
        formatSubDir = Objects.requireNonNullElseGet(formatting, () -> (seriesSet.size() == 1 ? DEFAULT_FORMAT_SUBDIR.replaceFirst(Pattern.quote("/S"), "") : DEFAULT_FORMAT_SUBDIR));
    }

    /**
     * Sets the formatting of each download.
     * <p>
     * The parameter controls the naming scheme for every downloaded
     * episode/video. If the parameter is an empty string or null, the
     * default naming scheme will be used. The parameter is a formatting
     * according to {@link EpisodeFormat#format(String)}.
     *
     * @param formatting null or empty -> {@link CollectiveDownloadBuilder#DEFAULT_FORMAT_DOWNLOAD}<br>
     *                   everything else -> naming scheme according to the given format
     */
    public void setFormatDownload(String formatting) {
        if (formatting == null || formatting.equals("")) {
            formatDownload = DEFAULT_FORMAT_DOWNLOAD;
        } else
            formatDownload = formatting;
    }
}
