package com.example.minikube.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.minikube.model.Collection;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Integer> {
    @Query(value = """
            SELECT *
            FROM collection
            LIMIT :offset, :perPage
            """, nativeQuery = true)
    List<Collection> findCollectionsByPage(@Param("offset") int offset,
            @Param("perPage") int perPage);

    @Query(value = """
            SELECT name, image
            FROM collection c
            WHERE c.link =:collectionLink
            """, nativeQuery = true)
    List<Object[]> findNameAndImageByLink(@Param("collectionLink") String collectionLink);
}