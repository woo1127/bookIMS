package com.bookIMS.Models;

public class Book {
    private Integer bookId;
    private String title;
    private String genre;
    private String author;
    private int quantity;
    private Double unitPrice;

    public Book(Integer bookId, String title, String genre, String author, Integer quantity, Double unitPrice) {
        this.bookId = bookId;
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Integer getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
