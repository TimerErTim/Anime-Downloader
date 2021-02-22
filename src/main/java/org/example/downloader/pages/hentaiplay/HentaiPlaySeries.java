package org.example.downloader.pages.hentaiplay;

import javafx.util.Pair;
import org.example.downloader.core.framework.Downloader;
import org.example.downloader.core.framework.Page;
import org.example.downloader.core.framework.Series;
import org.example.downloader.utils.JSoupDriver;
import org.example.downloader.utils.WebScrapers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class HentaiPlaySeries extends Series {
    /**
     * Creates a {@code HentaiPlaySeries} object from the URL.
     *
     * @param seriesURL the URL
     * @throws MalformedURLException the the exception that is thrown if the URL is no valid HentaiPlay site
     */
    public HentaiPlaySeries(URL seriesURL) throws MalformedURLException {
        super(seriesURL);
    }

    /**
     * Creates a {@code HentaiPlaySeries} object from the String.
     *
     * @param seriesURLString the String representing URL to HentaiPlay site
     * @throws MalformedURLException the exception that is thrown if the URL is no HentaiPlay site
     */
    public HentaiPlaySeries(String seriesURLString) throws MalformedURLException {
        super(seriesURLString);
    }

    @Override
    protected Pair<Set<? extends Downloader>, String> parseSeries(URL seriesURL) {
        Set<HentaiPlayDownloader> downloaders = new LinkedHashSet<>();
        String name;

        JSoupDriver driver = WebScrapers.noJavaScript();
        driver.get(seriesURL.toString());

        // Find name
        try {
            name = driver.findElement(By.className("loop-title")).getText();
        } catch (NullPointerException ex) {
            name = null;
        }

        // Parse Downloaders
        List<WebElement> elements = driver.findElements(By.tagName("a"));
        for (WebElement element : elements) {
            try {
                if (element.getAttribute("rel").equals("bookmark")) {
                    HentaiPlayDownloader downloader = new HentaiPlayDownloader(element.getAttribute("href"));
                    downloaders.add(downloader);
                }
            } catch (MalformedURLException ignored) {
            }
        }

        return new Pair<>(downloaders, name);
    }

    @Override
    protected boolean isValidSeriesURL(URL seriesURL) {
        return page.isValidPageURL(seriesURL) && seriesURL.toString().contains("/episode-list/");
    }

    @Override
    protected Page getPage() {
        return HentaiPlayPage.PAGE;
    }
}
