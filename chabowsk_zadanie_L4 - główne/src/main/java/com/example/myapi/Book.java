package com.example.myapi;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity // Wskazuje, że klasa jest encją JPA
@Table(name = "books") // Opcjonalnie określa nazwę tabeli w bazie danych
public class Book {

    @Id // Wskazuje, że pole jest kluczem głównym
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatyczne generowanie wartości ID
    private Long id;

    @NotBlank(message = "Tytuł nie może być pusty")
    @Column(nullable = false) // Wymóg niepustego pola w bazie danych
    private String title;
    
    @NotBlank(message = "Autor nie może być pusty")
    @Column(nullable = false)
    private String author;

    @NotNull(message = "Rok nie może być pusty")
    @Column(nullable = false)
    private Integer year;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CheckOut> checkouts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Set<CheckOut> getCheckouts() {
        return checkouts;
    }
    
    public void setCheckouts(Set<CheckOut> checkouts) {
        this.checkouts = checkouts;
    }
}
