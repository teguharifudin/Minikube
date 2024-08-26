package com.example.minikube.crawl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.minikube.crawl.core.ClientAPIConstants;
import com.example.minikube.model.Author;
import com.example.minikube.model.Collection;
import com.example.minikube.model.Quote;
import com.example.minikube.model.Topic;
import com.example.minikube.repository.QuoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * For a deeper understanding, check out the {@link TasksSchedule} class.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "crawling.executor.enabled", havingValue = "true")
public class QuoteCrawler {
    private final QuoteRepository quoteRepository;
    @Async
    public CompletableFuture<List<Quote>> quoteAPIWithAuthor(String nickName, int page,
            Author author) {
        HttpURLConnection getConnection = null;
        // Max Per Page is 120
        String url = ClientAPIConstants.QUOTE_API
                + URLEncoder.encode("author", StandardCharsets.UTF_8) + "=" + nickName + "&"
                + URLEncoder.encode("orderby", StandardCharsets.UTF_8) + "=popular" + "&"
                + URLEncoder.encode("page", StandardCharsets.UTF_8) + "=" + page + "&"
                + URLEncoder.encode("per_page", StandardCharsets.UTF_8) + "=" + 50;
        List<Quote> crawlQuotes = new ArrayList<>();
        try {
            URL quoteRequest = new URL(url);
            String readLine = null;
            getConnection = (HttpURLConnection) quoteRequest.openConnection();
            getConnection.setRequestMethod("GET");
            getConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36");
            if (getConnection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(getConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                while ((readLine = reader.readLine()) != null) {
                    response.append(readLine);
                }
                reader.close();
                JSONObject objResponse = new JSONObject(response.toString());
                Object quotes = objResponse.get("quotes");
                JSONArray objResult = new JSONArray(quotes.toString());
                for (int i = 0; i < objResult.length(); i++) {
                    JSONObject jsonobject = objResult.getJSONObject(i);
                    Quote quote = new Quote();
                    quote.setContent(jsonobject.getString("body"));
                    quote.setAuthor(author);
                    crawlQuotes.add(quote);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (getConnection != null) {
                getConnection.disconnect();
            }
        }
        return CompletableFuture.completedFuture(crawlQuotes);
    }

    @Transactional
    @Async
    public CompletableFuture<List<Quote>> quoteAPIWithCollection(String collectionSlugName,
            int page, Collection collection) {
        HttpURLConnection getConnection = null;
        // Max Per Page is 120
        String url = ClientAPIConstants.QUOTE_API
                + URLEncoder.encode("collection", StandardCharsets.UTF_8) + "=" + collectionSlugName
                + "&" + URLEncoder.encode("orderby", StandardCharsets.UTF_8) + "=popular" + "&"
                + URLEncoder.encode("page", StandardCharsets.UTF_8) + "=" + page + "&"
                + URLEncoder.encode("per_page", StandardCharsets.UTF_8) + "=" + 50;
        List<Quote> lst = new ArrayList<>();
        try {
            URL quoteRequest = new URL(url);
            String readLine = null;
            getConnection = (HttpURLConnection) quoteRequest.openConnection();
            getConnection.setRequestMethod("GET");
            getConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36");
            if (getConnection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(getConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                while ((readLine = reader.readLine()) != null) {
                    response.append(readLine);
                }
                reader.close();
                JSONArray objResult = new JSONArray(response.toString());
                for (int i = 0; i < objResult.length(); i++) {
                    JSONObject jsonobject = objResult.getJSONObject(i);
                    String content = jsonobject.getString("body");
                    List<Quote> existQuote = quoteRepository.findByContent(content);
                    if (0 != existQuote.size()) {
                        existQuote.get(0).getCollections().add(collection);
                        lst.add(existQuote.get(0));
                    } else {
                        Quote quote = new Quote();
                        quote.setContent(content);
                        quote.getCollections().add(collection);
                        lst.add(quote);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (getConnection != null) {
                getConnection.disconnect();
            }
        }
        return CompletableFuture.completedFuture(lst);
    }

    @Transactional
    @Async
    public CompletableFuture<List<Quote>> quoteAPIWithTopic(String topicSlugName, int page,
            Topic topic) {
        HttpURLConnection getConnection = null;
        // Max Per Page is 120
        String url = ClientAPIConstants.QUOTE_API
                + URLEncoder.encode("topic", StandardCharsets.UTF_8) + "=" + topicSlugName + "&"
                + URLEncoder.encode("orderby", StandardCharsets.UTF_8) + "=popular" + "&"
                + URLEncoder.encode("page", StandardCharsets.UTF_8) + "=" + page + "&"
                + URLEncoder.encode("per_page", StandardCharsets.UTF_8) + "=" + 50;
        List<Quote> lst = new ArrayList<>();
        try {
            URL quoteRequest = new URL(url);
            String readLine = null;
            getConnection = (HttpURLConnection) quoteRequest.openConnection();
            getConnection.setRequestMethod("GET");
            getConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36");
            if (getConnection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(getConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                while ((readLine = reader.readLine()) != null) {
                    response.append(readLine);
                }
                reader.close();
                JSONObject objResponse = new JSONObject(response.toString());
                Object quotes = objResponse.get("quotes");
                JSONArray objResult = new JSONArray(quotes.toString());
                for (int i = 0; i < objResult.length(); i++) {
                    JSONObject jsonobject = objResult.getJSONObject(i);
                    String content = jsonobject.getString("body");
                    List<Quote> existQuote = quoteRepository.findByContent(content);
                    if (0 != existQuote.size()) {
                        existQuote.get(0).getTopics().add(topic);
                        lst.add(existQuote.get(0));
                    } else {
                        Quote quote = new Quote();
                        quote.setContent(content);
                        quote.getTopics().add(topic);
                        lst.add(quote);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (getConnection != null) {
                getConnection.disconnect();
            }
        }
        return CompletableFuture.completedFuture(lst);
    }
}