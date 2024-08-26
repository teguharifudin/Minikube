package com.example.minikube.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "quote")
@Getter
@Setter
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @Column(name = "content", columnDefinition = "text", length = 32500)
    public String content;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Author author;
    // when crawling, change fetch to EAGER
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToMany(cascade = CascadeType.MERGE, mappedBy = "quotes", fetch = FetchType.LAZY)
    private Set<Collection> collections = new HashSet<>();
    // when crawling, change fetch to EAGER
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToMany(cascade = CascadeType.MERGE, mappedBy = "quotes", fetch = FetchType.LAZY)
    private Set<Topic> topics = new HashSet<>();
}