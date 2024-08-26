package com.example.minikube.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.minikube.core.BackgroundImgConstants;
import com.example.minikube.core.RecordConstants;
import com.example.minikube.model.Quote;
import com.example.minikube.service.QuoteService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/quotes")
@RequiredArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView quotes(ModelAndView modelAndView) {
        List<Quote> quotes = quoteService.getQuotesByPage(RecordConstants.DEFAULT_PAGE,
                RecordConstants.DEFAULT_RECORD_PER_PAGE);
        modelAndView.addObject("subjectName", "Quotes");
        modelAndView.addObject("backgroundImageURL", BackgroundImgConstants.QUOTE_PAGE_BACKGROUND);
        modelAndView.addObject("quotes", quotes);
        modelAndView.setViewName("quotes/quotes");
        return modelAndView;
    }
}