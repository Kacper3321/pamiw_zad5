package com.example.myapi;

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private Integer year;
    
    public void setId(Long newId){
        this.id = newId;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    public void setAuthor(String newAuthor){
        this.author = newAuthor;
    }

    public void setYear(Integer newYear){
        this.year = newYear;
    }

    public Long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public Integer getYear(){
        return year;
    }
}
