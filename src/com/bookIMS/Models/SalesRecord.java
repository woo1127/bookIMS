package com.bookIMS.Models;

import java.sql.Date;

public class SalesRecord {
    private Integer saleId;
    private String invoiceNo;
    private Date date;
    private String username;
    private Integer bookId;
    private String title;
    private Integer purchasedQty;
    private Double unitPrice;
    private Double amount;

    public SalesRecord(Integer saleId, String invoiceNo, Date date, String username, Integer bookId, String title, Integer purchasedQty, Double unitPrice, Double amount) {
        this.saleId = saleId;
        this.invoiceNo = invoiceNo;
        this.date = date;
        this.username = username;
        this.bookId = bookId;
        this.title = title;
        this.purchasedQty = purchasedQty;
        this.unitPrice = unitPrice;
        this.amount = amount;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public Date getDate() {
        return date;
    }

    public String getUsername() {
        return username;
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

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUsername(String username) {
        this.username = username;
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
