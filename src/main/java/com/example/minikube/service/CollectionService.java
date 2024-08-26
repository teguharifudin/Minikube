package com.example.minikube.service;

import java.util.List;

import com.example.minikube.model.Collection;

public interface CollectionService {
    List<Collection> getCollectionsByPage(int page, int per_page);

    List<Object[]> getNameAndImageByLink(String collectionLink);
}