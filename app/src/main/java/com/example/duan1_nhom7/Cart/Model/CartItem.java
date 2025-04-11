package com.example.duan1_nhom7.Cart.Model;

public class CartItem {
    private String productId;
    private String productName;
    private String productPrice;
    private String productImage;
    private String productSize;
    private String productSugar;
    private int productQuantity;

    public CartItem() {
    }

    // Constructor đầy đủ
    public CartItem(String productId, String productName, String productPrice, String productImage,
                    String productSize, String productSugar, int productQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productSize = productSize;
        this.productSugar = productSugar;
        this.productQuantity = productQuantity;
    }

    // Getter - Setter
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductSugar() {
        return productSugar;
    }

    public void setProductSugar(String productSugar) {
        this.productSugar = productSugar;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}