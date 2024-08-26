package com.example.minikube.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.minikube.core.BackgroundImgConstants;
import com.example.minikube.core.RecordConstants;
import com.example.minikube.model.Author;
import com.example.minikube.model.Quote;
import com.example.minikube.service.AuthorService;
import com.example.minikube.service.QuoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final QuoteService quoteService;
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView authors(ModelAndView modelAndView) {
        List<Author> authors = authorService.getAuthorByPage(RecordConstants.DEFAULT_PAGE,
                RecordConstants.DEFAULT_RECORD_PER_PAGE);
        modelAndView.addObject("subjectName", "Authors");
        modelAndView.addObject("backgroundImageURL", BackgroundImgConstants.AUTHOR_PAGE_BACKGROUND);
        modelAndView.addObject("authors", authors);
        modelAndView.setViewName("authors/authors");
        return modelAndView;
    }

    @RequestMapping(value = "/{authorName}", method = RequestMethod.GET)
    public ModelAndView individualAuthor(@PathVariable("authorName") String authorName,
            ModelAndView modelAndView, HttpServletRequest request) {
        String authorLink = request.getRequestURI().substring(1);
        List<Object[]> objects = authorService.getNameAndImageByLink(authorLink);
        String subjectName = objects.get(0)[0].toString();
        String backgroundImageURL = objects.get(0)[1].toString();
        List<Quote> quotes = quoteService.getByAuthorLink(authorLink, RecordConstants.DEFAULT_PAGE,
                RecordConstants.DEFAULT_RECORD_PER_PAGE);
        modelAndView.addObject("subjectName", subjectName);
        modelAndView.addObject("backgroundImageURL", backgroundImageURL);
        modelAndView.addObject("quotes", quotes);
        modelAndView.addObject("authorName", authorName);
        modelAndView.setViewName("authors/individual-author");
        return modelAndView;
    }
}