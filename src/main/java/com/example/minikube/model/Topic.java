package com.example.minikube.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "topic")
@Getter
@Setter
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @Column(name = "name", nullable = false)
    public String name;
    @Column(name = "link")
    public String link;
    @Column(name = "count")
    public Integer count;
    @Column(name = "image")
    public String image;
    @Column(name = "profile", columnDefinition = "text", length = 32500)
    public String profile;
    // when crawling, change fetch to EAGER
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "topic_quote",
            joinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "quote_id", referencedColumnName = "id"))
    @JsonIgnore
    private Set<Quote> quotes = new HashSet<>();
}