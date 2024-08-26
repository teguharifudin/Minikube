package com.example.minikube.crawl.config;

import java.util.concurrent.Executor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.minikube.crawl.TasksSchedule;

/**
 * For a deeper understanding, check out the {@link TasksSchedule} class.
 */
@Configuration
@ConditionalOnProperty(name = "crawling.executor.enabled", havingValue = "true")
public class TasksExecutor {
    @Bean
    public Executor crawlingDataExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Crawling-Data");
        executor.initialize();
        return executor;
    }
}