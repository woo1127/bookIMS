package com.bookIMS.Models;

public class Cart {
    private Integer bookId;
    private String title;
    private Integer purchasedQty;
    private Double unitPrice;
    private Double amount;

    public Cart(Integer bookId, String title, Integer purchasedQty, Double unitPrice, Double amount) {
        this.bookId = bookId;
        this.title = title;
        this.purchasedQty = purchasedQty;
        this.unitPrice = unitPrice;
        this.amount = amount;
    }

    public Integer getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getPurchasedQty() {
        return purchasedQty;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public Double getAmount() {
        return amount;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPurchasedQty(Integer purchasedQty) {
        this.purchasedQty = purchasedQty;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
