package com.example.minikube.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.minikube.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    @Query(value = """
            SELECT *
            FROM author
            ORDER BY featured DESC
            LIMIT :offset, :perPage
            """, nativeQuery = true)
    List<Author> findAuthorsByPage(@Param("offset") int offset, @Param("perPage") int perPage);

    @Query(value = """
            SELECT name, image
            FROM author a
            WHERE a.link =:authorLink
            """, nativeQuery = true)
    List<Object[]> findNameAndImageByLink(@Param("authorLink") String authorLink);
}