package com.example.myapi;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

import javax.annotation.PostConstruct;

@Component
public class BookDataLoader {

    @Autowired
    private BookRepository bookRepository;

    private final Faker faker = new Faker(new Random(72)); // Ustaw stałe ziarno dla powtarzalności

    @PostConstruct
    private void loadData() {
        // Tworzenie danych testowych
        if (bookRepository.count() == 0) {
            for (int i = 0; i < 20; i++) {
                Book book = new Book();
                // Używaj metody faker do generowania danych
                String title = faker.book().title();
                String author = faker.name().fullName(); // Pełne imię i nazwisko
                int year = faker.number().numberBetween(1, 2021); // Zakładając, że książki są z lat 1 - 2021

                book.setTitle(title);
                book.setAuthor(author);
                book.setYear(year);
                bookRepository.save(book);
            }
        }
    }
}
