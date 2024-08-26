package com.example.minikube.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.minikube.model.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    @Query(value = """
            SELECT *
            FROM topic
            LIMIT :offset, :perPage
            """, nativeQuery = true)
    List<Topic> findTopicsByPage(@Param("offset") int offset, @Param("perPage") int perPage);

    @Query(value = """
            SELECT name, image
            FROM topic t
            WHERE t.link =:topicLink
            """, nativeQuery = true)
    List<Object[]> findNameAndImageByLink(@Param("topicLink") String topicLink);
}