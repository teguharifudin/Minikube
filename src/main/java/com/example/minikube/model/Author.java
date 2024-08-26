package com.example.minikube.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "author")
@Getter
@Setter
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @Column(name = "name", nullable = false)
    public String name;
    @Column(name = "link")
    public String link;
    @Column(name = "featured", nullable = false)
    public Integer featured;
    @Column(name = "count")
    public Integer count;
    @Column(name = "image")
    public String image;
    @Column(name = "birthday")
    public String birthday;
    @Column(name = "profile", columnDefinition = "text", length = 32500)
    public String profile;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Quote> quotes;
}