package com.example.duan1_nhom7.Product;

public class Product {
    private String name;
    private int price;
    private String imageUrl;

    public Product(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Thêm phương thức này để sửa lỗi
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
