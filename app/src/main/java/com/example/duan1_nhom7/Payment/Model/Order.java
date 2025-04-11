package com.example.duan1_nhom7.Payment.Model;

import com.example.duan1_nhom7.Cart.Model.CartItem;
import com.google.firebase.Timestamp;
import java.util.List;

public class Order {

    private String orderId;
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
    private int voucherDiscount;
    private int totalPayment;

    private Timestamp timestamp;
    private String status;

    public Order() {
    }

    public Order(String orderId, String userId, String name, String phone, String address, List<CartItem> items,
                 String paymentMethod, String voucher, String note,
                 int subtotal, int shippingFee, int discount, int voucherDiscount, int totalPayment,
                 Timestamp timestamp, String status) {
        this.orderId = orderId;
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
        this.voucherDiscount = voucherDiscount;
        this.totalPayment = totalPayment;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Getter & Setter

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getVoucherDiscount() {
        return voucherDiscount;
    }

    public void setVoucherDiscount(int voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", items=" + (items != null ? items.size() + " sản phẩm" : "null") +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", voucher='" + voucher + '\'' +
                ", note='" + note + '\'' +
                ", subtotal=" + subtotal +
                ", shippingFee=" + shippingFee +
                ", discount=" + discount +
                ", voucherDiscount=" + voucherDiscount +
                ", totalPayment=" + totalPayment +
                ", timestamp=" + (timestamp != null ? timestamp.toDate().toString() : "null") +
                ", status='" + status + '\'' +
                '}';
    }
}
