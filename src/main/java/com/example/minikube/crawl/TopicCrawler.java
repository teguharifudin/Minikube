package com.example.minikube.crawl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.PersistentObjectException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.minikube.crawl.core.ClientAPIConstants;
import com.example.minikube.crawl.util.CrawlerUtils;
import com.example.minikube.model.Quote;
import com.example.minikube.model.Topic;
import com.example.minikube.repository.TopicRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class TopicCrawler {
    public static final Set<String> targetTopics = Set.of("Love", "Pain", "Success", "Motivation",
            "Dreams", "Fear", "Quitting");
    private final TopicRepository topicRepository;
    private final QuoteCrawler quoteCrawler;
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    @Async
    public void topicAPI(int pageTopic) {
        HttpURLConnection getConnection = null;
        // Topic: Max Per Page is 120
        String url = ClientAPIConstants.TOPIC_API
                + URLEncoder.encode("page", StandardCharsets.UTF_8) + "=" + pageTopic + "&"
                + URLEncoder.encode("per_page", StandardCharsets.UTF_8) + "=" + 50;
        try {
            URL topicRequest = new URL(url);
            String readLine = null;
            getConnection = (HttpURLConnection) topicRequest.openConnection();
            getConnection.setRequestMethod("GET");
            getConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((readLine = reader.readLine()) != null) {
                response.append(readLine);
            }
            reader.close();
            JSONArray jsonObject = new JSONArray(response.toString());
            Set<Topic> topics = new HashSet<>();
            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject jsonobject = jsonObject.getJSONObject(i);
                if (!targetTopics.contains(jsonobject.getString("name"))) {
                    continue;
                }
                Topic topic = new Topic();
                topic.setName(jsonobject.getString("name"));
                topic.setLink(CrawlerUtils.removeLastSlash(jsonobject.getString("link")));
                topic.setCount(jsonobject.getInt("count"));
                topic.setImage(jsonobject.getJSONObject("image").get("url").toString());
                topic.setProfile(jsonobject.getString("body"));
                Set<Quote> quotesByTopic = new HashSet<>();
                // Quote with Topic: Max Page is 20
                for (int pageQuote = 1; pageQuote <= 1; pageQuote++) {
                    List<Quote> lst = quoteCrawler.quoteAPIWithTopic(
                            getSlugName(jsonobject.getString("link")), pageQuote, topic).get();
                    if (!lst.isEmpty()) {
                        quotesByTopic.addAll(lst);
                    } else {
                        break;
                    }
                }
                topic.getQuotes().addAll(quotesByTopic);
                topics.add(topic);
            }
            synchronized (this) {
                for (Topic topic : topics) {
                    Set<Quote> quotes = topic.getQuotes();
                    for (Quote quote : quotes) {
                        // Either save or merge the detached quote entities
                        entityManager.merge(quote);
                    }
                }
                topicRepository.saveAll(topics);
            }
        } catch (InvalidDataAccessApiUsageException ex) {
            log.error("InvalidDataAccessApiUsageException: {}", ex.getMessage());
            ex.printStackTrace();
        } catch (PersistentObjectException ex) {
            log.error("PersistentObjectException: {}", ex.getMessage());
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (getConnection != null) {
                getConnection.disconnect();
            }
        }
    }

    public String getSlugName(String link) {
        int lastIndex = link.lastIndexOf("/");
        int nearLastIndex = link.lastIndexOf("/", lastIndex - 1);
        return link.substring(nearLastIndex + 1, lastIndex);
    }
}