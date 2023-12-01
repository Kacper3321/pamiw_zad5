package com.example.myapi;

import java.time.LocalDate;

public class CheckOutDTO {
    private Long id;
    private BookDTO book;
    private LocalDate checkoutDate;
    private String borrowerFullName;
    private Integer returnDays;
    private Long bookId;
 
    public void setId(Long newId){
        this.id = newId;
    }

    public void setBook(BookDTO newBook){
        this.book = newBook;
    }

    public void setCheckoutDate(LocalDate newCheckoutDate){
        this.checkoutDate = newCheckoutDate;
    }

    public void setBorrowerFullName(String newBorrowerFullName){
        this.borrowerFullName = newBorrowerFullName;
    }

    public void setReturnDays(Integer newReturnDays){
        this.returnDays = newReturnDays;
    }

    public void setBookId(Long newBookId){
        this.bookId = newBookId;
    }

    public Long getId(){
        return id;
    }

    public BookDTO getBook(){
        return book;
    }

    public LocalDate getCheckoutDate(){
        return checkoutDate;
    }

    public String getBorrowerFullName(){
        return borrowerFullName;
    }

    public Integer getReturnDays(){
        return returnDays;
    }

    public Long getBookId(){
        return bookId;
    }

}