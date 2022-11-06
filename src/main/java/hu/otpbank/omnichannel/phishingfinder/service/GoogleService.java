package hu.otpbank.omnichannel.phishingfinder.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
public class GoogleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleService.class);
    private static final String GOOGLE_COM = "https://www.google.com";
    private static final String GOOGLE_CONSENT = "https://consent.google.com";

    private final CloseableHttpClient httpClient;
    private final CookieStore cookieStore;
    private String userAgent;

    public GoogleService() {
        this.userAgent = "Example Bot 1.0";
        this.cookieStore = new BasicCookieStore();
        this.httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
    }

    public Document search(String searchKey) {
        try {
            HttpGet googleSearchRequest = new HttpGet(getSearchUri(searchKey));
            googleSearchRequest.setHeader(HTTP.USER_AGENT, userAgent);

            if (cookieStore.getCookies().isEmpty()) {
                LOGGER.info("No available cookies for search. Consent request required.");
                HttpPost consentRequest = new HttpPost(getConsentUri());
                HttpResponse consentResponse = httpClient.execute(consentRequest);
                int statusCode = consentResponse.getStatusLine().getStatusCode();
                if (statusCode == 204) {
                    LOGGER.info("Consent accepted");
                } else {
                    LOGGER.warn("Consent acceptance has failed");
                }
            }

            HttpResponse httpResponse = httpClient.execute(googleSearchRequest);
            String html = EntityUtils.toString(httpResponse.getEntity());
            return getDocument(html);
        } catch (IOException e) {
            throw new RuntimeException("Http client error", e);
        }
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        return userAgent;
    }

    private Document getDocument(String html) {
        return Jsoup.parse(html);
    }

    private String getConsentUri() {
        return UriComponentsBuilder
                .fromUriString(GOOGLE_CONSENT)
                .pathSegment("save")
                .queryParam("gl", "HU")
                .queryParam("pc", "srp")
                .queryParam("x", 5)
                .queryParam("hl", "hu")
                .queryParam("bl", "gws_20221102-0_RC2")
                .queryParam("set_eom", true)
                .build()
                .toUriString();
    }

    private String getSearchUri(String searchKey) {
        return UriComponentsBuilder
                .fromUriString(GOOGLE_COM)
                .path("search")
                .queryParam("q", searchKey)
                .build()
                .toUriString();
    }
}
