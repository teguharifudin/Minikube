package com.example.minikube.service.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import com.example.minikube.model.Quote;
import com.example.minikube.repository.QuoteRepository;
import com.example.minikube.service.QuoteService;
import lombok.RequiredArgsConstructor;

@Service
@EnableCaching
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {
    private final QuoteRepository quoteRepository;
    @Override
    @Cacheable(value = "quotes", key = "{#page, #per_page}")
    public List<Quote> getQuotesByPage(int page, int per_page) {
        int offset = (page - 1) * per_page;
        return quoteRepository.findQuotesByPage(offset, per_page);
    }

    @Override
    @Cacheable(value = "collectionQuotes", key = "{#collectionLink, #page, #per_page}")
    public List<Quote> getByCollectionLink(String collectionLink, int page, int per_page) {
        int offset = (page - 1) * per_page;
        return quoteRepository.findByCollectionLink(collectionLink, offset, per_page);
    }

    @Override
    @Cacheable(value = "topicQuotes", key = "{#topicLink, #page, #per_page}")
    public List<Quote> getByTopicLink(String topicLink, int page, int per_page) {
        int offset = (page - 1) * per_page;
        return quoteRepository.findByTopicLink(topicLink, offset, per_page);
    }

    @Override
    @Cacheable(value = "authorQuotes", key = "{#authorLink, #page, #per_page}")
    public List<Quote> getByAuthorLink(String authorLink, int page, int per_page) {
        int offset = (page - 1) * per_page;
        return quoteRepository.findByAuthorLink(authorLink, offset, per_page);
    }
}