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
import com.example.minikube.model.Author;
import com.example.minikube.model.Quote;
import com.example.minikube.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * For a deeper understanding, check out the {@link TasksSchedule} class.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "crawling.executor.enabled", havingValue = "true")
public class AuthorCrawler {
    private final AuthorRepository authorRepository;
    private final QuoteCrawler quoteCrawler;
    @Async
    public void authorAPI(int pageAuthor) {
        HttpURLConnection getConnection = null;
        // Author: Max Per Page is 120
        String url = ClientAPIConstants.AUTHOR_API
                + URLEncoder.encode("page", StandardCharsets.UTF_8) + "=" + pageAuthor + "&"
                + URLEncoder.encode("per_page", StandardCharsets.UTF_8) + "=" + 50;
        try {
            URL authorRequest = new URL(url);
            String readLine = null;
            getConnection = (HttpURLConnection) authorRequest.openConnection();
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
            Set<Author> authors = new HashSet<>();
            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject jsonobject = jsonObject.getJSONObject(i);
                Author author = new Author();
                author.setName(jsonobject.getString("name"));
                author.setLink(CrawlerUtils.removeLastSlash(jsonobject.getString("link")));
                author.setFeatured(jsonobject.getInt("featured"));
                author.setCount(jsonobject.getInt("count"));
                author.setImage(jsonobject.getJSONObject("image").get("url").toString());
                author.setBirthday(jsonobject.getString("birthday"));
                author.setProfile(jsonobject.getString("body"));
                Set<Quote> quotesByAuthor = new HashSet<>();
                // Quote with Author: Max Page is 18
                for (int pageQuote = 1; pageQuote <= 1; pageQuote++) {
                    List<Quote> lst = quoteCrawler.quoteAPIWithAuthor(
                            slugName(jsonobject.getString("link")), pageQuote, author).get();
                    if (!lst.isEmpty()) {
                        quotesByAuthor.addAll(lst);
                    } else {
                        break;
                    }
                }
                author.setQuotes(quotesByAuthor);
                authors.add(author);
            }
            synchronized (this) {
                authorRepository.saveAll(authors);
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

    private String slugName(String link) {
        int lastIndex = link.lastIndexOf("/");
        int nearLastIndex = link.lastIndexOf("/", lastIndex - 1);
        return link.substring(nearLastIndex + 1, lastIndex);
    }
}