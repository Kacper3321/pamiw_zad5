package com.example.myapi;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
// wykorzystujac metody JpaRepository jestem w stanie obsluzyc baze danych
}
