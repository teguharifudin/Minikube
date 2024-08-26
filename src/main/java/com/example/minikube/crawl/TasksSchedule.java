package com.example.minikube.crawl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The crawling executor tool is used to initially crawl and populate the project's database.
 * It's necessary only during the project setup.
 * To activate it, simply set crawling.executor.enabled to true in the application.yaml file.
 * <p>
 * The crawling approach can be enhanced and made more efficient.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "crawling.executor.enabled", havingValue = "true")
public class TasksSchedule {
    private final AuthorCrawler authorCrawler;
    private final CollectionCrawler collectionCrawler;
    private final TopicCrawler topicCrawler;
    @Scheduled(fixedRate = 1000000000)
    public void reportCurrentTime() {
        long start = System.currentTimeMillis();
        log.debug("the crawl is started: <{}>", start);
        try {
            // Max Page of Author is 156
            for (int pageAuthor = 1; pageAuthor <= 1; pageAuthor++) {
                authorCrawler.authorAPI(pageAuthor);
            }
            // Max Page of Collection is 1
            collectionCrawler.collectionAPI();
            // Max Page of Topic is 21
            for (int pageTopic = 1; pageTopic <= 1; pageTopic++) {
                topicCrawler.topicAPI(pageTopic);
            }
            log.debug("the crawl is finished: <{}>", (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error <{}> happens when configuring crawler4j", e.getMessage());
        }
    }
}