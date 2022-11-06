package hu.otpbank.omnichannel.phishingfinder.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LinkParser {

    public static List<SearchResult> parseLinks(Document document) {
        List<SearchResult> usefulLinks = new ArrayList<>();
        Elements links = document.select("a[href]");

        for (Element link : links) {
            String title = link.text();
            String url = link.attr("href");
            if (url.contains("=") && url.contains("&")) {
                url = URLDecoder.decode(
                        url.substring(
                                url.indexOf('=') + 1,
                                url.indexOf('&')),
                        StandardCharsets.UTF_8);

                if (url.startsWith("http")) {
                    SearchResult searchResult = new SearchResult(title, url);
                    usefulLinks.add(searchResult);
                }
            }
        }
        return usefulLinks;
    }
}
