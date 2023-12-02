package com.example.myapi;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Random;

@Component
public class CheckOutDataLoader {

    @Autowired
    private CheckOutRepository checkOutRepository;

    @Autowired
    private BookRepository bookRepository;

    private final Faker faker = new Faker(new Random(72));

    @PostConstruct
    private void loadData() {
        // Zakładamy, że w bazie danych nie ma jeszcze książek
        if (checkOutRepository.count() == 0 && bookRepository.count() > 0) {
            for (int i = 0; i < 5; i++) {
                CheckOut checkOut = new CheckOut();
                Book randomBook = bookRepository.findAll().get(faker.random().nextInt((int) bookRepository.count()));
                
                checkOut.setBook(randomBook);
                checkOut.setCheckoutDate(LocalDate.now());
                checkOut.setReturnDays(faker.number().numberBetween(7, 30)); // Zakładając, że okres zwrotu to od 7 do 30 dni
                checkOut.setBorrowerFullName(faker.name().fullName());

                checkOutRepository.save(checkOut);
            }
        }
    }
}

