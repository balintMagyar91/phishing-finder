package hu.otpbank.omnichannel.phishingfinder.component;

import com.google.common.collect.Iterables;
import hu.otpbank.omnichannel.phishingfinder.datamanager.DataManager;
import hu.otpbank.omnichannel.phishingfinder.service.GoogleService;
import hu.otpbank.omnichannel.phishingfinder.utils.LinkParser;
import hu.otpbank.omnichannel.phishingfinder.utils.SearchResult;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class ScraperTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScraperTask.class);

    @Autowired
    private GoogleService googleService;

    private List<String> searchKeys;
    private Iterable<String> userAgent;

    public ScraperTask(
            @Value("#{'${searchKey}'.split(',')}") List<String> searchKeys,
            @Value("#{'${userAgent}'.split(',')}") List<String> userAgent) {

        LOGGER.info("Search keys: {}", searchKeys);
        LOGGER.info("User-Agents: {}", userAgent);

        this.searchKeys = searchKeys;
        this.userAgent = Iterables.cycle(userAgent);
    }

    private void scraping() {
        Iterator<String> userAgentIterator = userAgent.iterator();
        for (String key : searchKeys) {
            String currentUserAgent = userAgentIterator.next();
            LOGGER.info("Start google search with key: {} and user-agent: {}", key, currentUserAgent);
            googleService.setUserAgent(currentUserAgent);

            Document searchPage = googleService.search(key);
            List<SearchResult> searchResults = LinkParser.parseLinks(searchPage);
            DataManager.printLinks(searchResults);
            DataManager.writeLinksToCsv(searchResults, key, currentUserAgent);
        }
    }

    @Override
    public void run() {
        scraping();
    }
}
