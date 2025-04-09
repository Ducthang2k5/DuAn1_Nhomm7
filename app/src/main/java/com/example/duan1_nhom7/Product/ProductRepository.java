package com.example.duan1_nhom7.Product;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository {
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public ProductRepository() {
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void addProductToUser(String productId, String name, double price, String imageUrl) {
        if (currentUser == null) {
            return; // User chưa đăng nhập
        }
        String userId = currentUser.getUid();

        Map<String, Object> productData = new HashMap<>();
        productData.put("name", name);
        productData.put("price", price);
        productData.put("imageUrl", imageUrl);

        db.collection("users").document(userId).collection("products").document(productId)
                .set(productData)
                .addOnSuccessListener(aVoid -> System.out.println("Sản phẩm đã được thêm!"))
                .addOnFailureListener(e -> System.err.println("Lỗi khi thêm sản phẩm: " + e.getMessage()));
    }

    public void getUserProducts(OnProductsLoadedListener listener) {
        if (currentUser == null) {
            return; // User chưa đăng nhập
        }
        String userId = currentUser.getUid();

        db.collection("users").document(userId).collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> products = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            products.add(document.getData());
                        }
                        listener.onProductsLoaded(products);
                    } else {
                        System.err.println("Lỗi khi lấy sản phẩm: " + task.getException().getMessage());
                    }
                });
    }

    public interface OnProductsLoadedListener {
        void onProductsLoaded(List<Map<String, Object>> products);
    }
}

