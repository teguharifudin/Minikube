package com.example.minikube.service;

import java.util.List;

import com.example.minikube.model.Topic;

public interface TopicService {
    List<Topic> getTopicsByPage(int page, int per_page);

    List<Object[]> getNameAndImageByLink(String topicLink);
}