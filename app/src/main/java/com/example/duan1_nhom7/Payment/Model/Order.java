package com.example.duan1_nhom7.Payment.Model;

import com.example.duan1_nhom7.Cart.Model.CartItem;

import java.util.List;

public class Order {
    private String userId;
    private String name;
    private String phone;
    private String address;
    private List<CartItem> items;
    private String paymentMethod;
    private String voucher;
    private String note;
    private int subtotal;
    private int shippingFee;
    private int discount;
    private int total;
    private long timestamp;

    public Order() {}

    public Order(String userId, String name, String phone, String address, List<CartItem> items,
                 String paymentMethod, String voucher, String note,
                 int subtotal, int shippingFee, int discount, int total, long timestamp) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.items = items;
        this.paymentMethod = paymentMethod;
        this.voucher = voucher;
        this.note = note;
        this.subtotal = subtotal;
        this.shippingFee = shippingFee;
        this.discount = discount;
        this.total = total;
        this.timestamp = timestamp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
