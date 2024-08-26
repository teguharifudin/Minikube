package com.example.minikube.service.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import com.example.minikube.model.Collection;
import com.example.minikube.repository.CollectionRepository;
import com.example.minikube.service.CollectionService;
import lombok.RequiredArgsConstructor;

@Service
@EnableCaching
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    @Override
    @Cacheable(value = "collections", key = "{#page, #per_page}")
    public List<Collection> getCollectionsByPage(int page, int per_page) {
        int offset = (page - 1) * per_page;
        return collectionRepository.findCollectionsByPage(offset, per_page);
    }

    @Override
    @Cacheable(value = "collectionNameImage", key = "#collectionLink")
    public List<Object[]> getNameAndImageByLink(String collectionLink) {
        return collectionRepository.findNameAndImageByLink(collectionLink);
    }
}