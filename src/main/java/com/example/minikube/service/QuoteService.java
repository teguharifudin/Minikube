package com.example.minikube.service;

import java.util.List;

import com.example.minikube.model.Quote;

public interface QuoteService {
    List<Quote> getQuotesByPage(int page, int per_page);

    List<Quote> getByCollectionLink(String collectionLink, int page, int per_page);

    List<Quote> getByTopicLink(String topicLink, int page, int per_page);

    List<Quote> getByAuthorLink(String authorLink, int page, int per_page);
}