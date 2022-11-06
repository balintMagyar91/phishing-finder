package hu.otpbank.omnichannel.phishingfinder.datamanager;

import hu.otpbank.omnichannel.phishingfinder.utils.SearchResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class DataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);

    public static void printLinks(List<SearchResult> searchResults) {
        searchResults.forEach(System.out::println);
    }

    public static void writeLinksToCsv(List<SearchResult> searchResults, String searchKey, String userAgent) {
        String currentTime = LocalDateTime.now().toString();
        try (CSVPrinter csvPrinter =
                     new CSVPrinter(new FileWriter("search-result-report.csv", true), CSVFormat.DEFAULT)) {
            csvPrinter.printRecord(currentTime, searchKey, userAgent);
            for (SearchResult searchResult : searchResults) {
                csvPrinter.printRecord(searchResult.getUrl(), searchResult.getTitle());
            }
        } catch (IOException e) {
            LOGGER.error("Error while writing CSV ", e);
        }
    }
}
