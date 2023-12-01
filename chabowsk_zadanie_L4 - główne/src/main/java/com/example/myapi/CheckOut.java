package com.example.myapi;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "checkouts")
public class CheckOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull(message = "Data nie może być pusta")
    @Column(nullable = false)
    private LocalDate checkoutDate;

    @NotNull(message = "Liczba dni nie może być pusta")
    @Column(nullable = false)
    private Integer returnDays;

    @NotBlank(message = "Osoba wypożyczająca nie może być pusta")
    @Column(nullable = false)
    private String borrowerFullName;

    @NotBlank(message = "Tytuł nie może być pusty")
    @Column(name = "book_title")
    private String bookTitle;
    

    // Gettery i settery

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBook(Book book) {
        this.book = book;
        if (book != null) {
            this.bookTitle = book.getTitle();
        } else {
            this.bookTitle = null;
        }
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public int getReturnDays() {
        return returnDays;
    }

    public void setReturnDays(int returnDays) {
        this.returnDays = returnDays;
    }

    public String getBorrowerFullName() {
        return borrowerFullName;
    }

    public void setBorrowerFullName(String borrowerFullName) {
        this.borrowerFullName = borrowerFullName;
    }

    @PrePersist
    private void prePersist() {
        this.checkoutDate = LocalDate.now(); // Ustawia datę wypożyczenia na bieżącą datę przed zapisaniem do bazy
    }
}
