package com.example.minikube.service;

import java.util.List;

import com.example.minikube.model.Author;

public interface AuthorService {
    List<Author> getAuthorByPage(int page, int per_page);

    List<Object[]> getNameAndImageByLink(String authorLink);
}