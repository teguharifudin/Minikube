package com.example.minikube.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.minikube.model.Quote;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Integer> {
    @Query(value = """
            SELECT
                q.id AS id, q.content AS content,
                a.id AS author_id, a.name AS author_name
            FROM quote q
            LEFT JOIN author a ON q.author_id = a.id
            LIMIT :offset, :perPage
            """, nativeQuery = true)
    List<Quote> findQuotesByPage(@Param("offset") int offset, @Param("perPage") int perPage);

    List<Quote> findByContent(String content);

    @Query(value = """
            SELECT
                q.id AS id, q.content AS content,
                a.id AS author_id, a.name AS author_name
            FROM quote q
            INNER JOIN collection_quote cq ON cq.quote_id = q.id
            INNER JOIN collection c ON c.id = cq.collection_id
            LEFT JOIN author a ON a.id = q.author_id
            WHERE c.link = :collectionLink
            LIMIT :offset, :perPage
            """, nativeQuery = true)
    List<Quote> findByCollectionLink(@Param("collectionLink") String collectionLink,
            @Param("offset") int offset, @Param("perPage") int perPage);

    @Query(value = """
            SELECT
                q.id AS id, q.content AS content,
                a.id AS author_id, a.name AS author_name
            FROM quote q
            INNER JOIN topic_quote tq ON tq.quote_id = q.id
            INNER JOIN topic t ON t.id = tq.topic_id
            LEFT JOIN author a ON a.id = q.author_id
            WHERE t.link = :topicLink
            LIMIT :offset, :perPage
            """, nativeQuery = true)
    List<Quote> findByTopicLink(@Param("topicLink") String topicLink, @Param("offset") int offset,
            @Param("perPage") int perPage);

    @Query(value = """
            SELECT *
            FROM quote q
            WHERE q.author_id IN (
                SELECT a.id
                FROM author a
                WHERE a.link = :authorLink
            )
            LIMIT :offset, :perPage
            """, nativeQuery = true)
    List<Quote> findByAuthorLink(@Param("authorLink") String authorLink,
            @Param("offset") int offset, @Param("perPage") int perPage);
}