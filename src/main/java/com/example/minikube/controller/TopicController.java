package com.example.minikube.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.minikube.core.BackgroundImgConstants;
import com.example.minikube.core.RecordConstants;
import com.example.minikube.model.Quote;
import com.example.minikube.model.Topic;
import com.example.minikube.service.QuoteService;
import com.example.minikube.service.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;
    private final QuoteService quoteService;
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView topics(ModelAndView modelAndView) {
        List<Topic> topics = topicService.getTopicsByPage(RecordConstants.DEFAULT_PAGE,
                RecordConstants.DEFAULT_RECORD_PER_PAGE);
        modelAndView.addObject("subjectName", "Topics");
        modelAndView.addObject("backgroundImageURL", BackgroundImgConstants.TOPIC_PAGE_BACKGROUND);
        modelAndView.addObject("topics", topics);
        modelAndView.setViewName("topics/topics");
        return modelAndView;
    }

    @RequestMapping(value = "/{topicName}", method = RequestMethod.GET)
    public ModelAndView individualTopic(@PathVariable("topicName") String topicName,
            ModelAndView modelAndView, HttpServletRequest request) {
        String topicLink = request.getRequestURI().substring(1);
        List<Object[]> objects = topicService.getNameAndImageByLink(topicLink);
        String subjectName = objects.get(0)[0].toString();
        String backgroundImageURL = objects.get(0)[1].toString();
        List<Quote> quotes = quoteService.getByTopicLink(topicLink, RecordConstants.DEFAULT_PAGE,
                RecordConstants.DEFAULT_RECORD_PER_PAGE);
        modelAndView.addObject("subjectName", subjectName);
        modelAndView.addObject("backgroundImageURL", backgroundImageURL);
        modelAndView.addObject("quotes", quotes);
        modelAndView.setViewName("topics/individual-topic");
        return modelAndView;
    }
}