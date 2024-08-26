package com.example.minikube.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.minikube.model.Author;
import com.example.minikube.model.Collection;
import com.example.minikube.model.Quote;
import com.example.minikube.model.Topic;
import com.example.minikube.service.AuthorService;
import com.example.minikube.service.CollectionService;
import com.example.minikube.service.QuoteService;
import com.example.minikube.service.TopicService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/quote/v1")
@RequiredArgsConstructor
public class APIController {
    private final AuthorService authorService;
    private final CollectionService collectionService;
    private final QuoteService quoteService;
    private final TopicService topicService;
    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public ResponseEntity<List<Author>> authors(
            @RequestParam(value = "page", required = true, defaultValue = "1") int page,
            @RequestParam(value = "per_page", required = false, defaultValue = "60") int per_page) {
        try {
            List<Author> authors = authorService.getAuthorByPage(page, per_page);
            return new ResponseEntity<>(authors, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/collections", method = RequestMethod.GET)
    public ResponseEntity<List<Collection>> collections(
            @RequestParam(value = "page", required = true, defaultValue = "1") int page,
            @RequestParam(value = "per_page", required = false, defaultValue = "60") int per_page) {
        try {
            List<Collection> collections = collectionService.getCollectionsByPage(page, per_page);
            return new ResponseEntity<>(collections, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/quotes", method = RequestMethod.GET)
    public ResponseEntity<List<Quote>> quotes(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "collection", required = false) String collection,
            @RequestParam(value = "topic", required = false) String topic,
            @RequestParam(value = "page", required = true, defaultValue = "1") int page,
            @RequestParam(value = "per_page", required = false, defaultValue = "60") int per_page) {
        try {
            List<Quote> quotes = new ArrayList<>();
            if (StringUtils.isNotEmpty(author)) {
                quotes = quoteService.getByAuthorLink(author, page, per_page);
            } else if (StringUtils.isNotEmpty(collection)) {
                quotes = quoteService.getByCollectionLink(collection, page, per_page);
            } else if (StringUtils.isNotEmpty(topic)) {
                quotes = quoteService.getByTopicLink(topic, page, per_page);
            } else {
                quotes = quoteService.getQuotesByPage(page, per_page);
            }
            return new ResponseEntity<>(quotes, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/topics", method = RequestMethod.GET)
    public ResponseEntity<List<Topic>> getListTopics(
            @RequestParam(value = "page", required = true, defaultValue = "1") int page,
            @RequestParam(value = "per_page", required = false, defaultValue = "60") int per_page) {
        try {
            List<Topic> topics = topicService.getTopicsByPage(page, per_page);
            return new ResponseEntity<>(topics, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}