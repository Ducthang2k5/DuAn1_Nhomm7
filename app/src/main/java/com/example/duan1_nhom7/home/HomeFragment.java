package com.example.duan1_nhom7.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom7.Product.Product;
import com.example.duan1_nhom7.Product.ProductAdapter;
import com.example.duan1_nhom7.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productList = new ArrayList<>();
        adapter = new ProductAdapter(getActivity(), productList, this);
        recyclerView.setAdapter(adapter);



        db = FirebaseFirestore.getInstance();

        // Gọi phương thức để lắng nghe thay đổi trong Firestore

        // Thêm sản phẩm chỉ khi Firestore đang rỗng (tránh thêm lặp lại)
        db.collection("products").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().isEmpty()) {
                addSampleProducts(); // ✅ Chỉ thêm sản phẩm nếu Firestore trống
            }
        });
        listenForProductUpdates();

        return view;

//        btnMenu.setOnClickListener(v -> {
//            Toast.makeText(getContext(), "Mở Menu", Toast.LENGTH_SHORT).show();
//        });
//
//// Khi nhấn vào ô tìm kiếm
//        searchBar.setOnClickListener(v -> {
//            searchBar.setFocusableInTouchMode(true);
//            searchBar.requestFocus();
//        });
    }

    private void listenForProductUpdates() {
        db.collection("products")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        productList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void addSampleProducts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<Product> sampleProducts = new ArrayList<>();
        sampleProducts.add(new Product("1", "Trà sữa hoàng kim ", "https://www.google.com/imgres?q=tr%C3%A0%20s%E1%BB%AFa&imgurl=https%3A%2F%2Fsimexcodl.com.vn%2Fwp-content%2Fuploads%2F2024%2F05%2Ftra-sua-ca-phe-2.jpg&imgrefurl=https%3A%2F%2Fsimexcodl.com.vn%2Ftra-sua-ca-phe%2F&docid=HG6V-UU1u-J2QM&tbnid=eh9vxXloKwdGtM&vet=12ahUKEwjmy6HF07OMAxWNdfUHHTyMNjMQM3oECE8QAA..i&w=800&h=450&hcb=2&ved=2ahUKEwjmy6HF07OMAxWNdfUHHTyMNjMQM3oECE8QAA", 100000));
        sampleProducts.add(new Product("2", "Trà sữa hoàng kim ", "https://www.google.com/imgres?q=tr%C3%A0%20s%E1%BB%AFa&imgurl=https%3A%2F%2Fsimexcodl.com.vn%2Fwp-content%2Fuploads%2F2024%2F05%2Ftra-sua-ca-phe-2.jpg&imgrefurl=https%3A%2F%2Fsimexcodl.com.vn%2Ftra-sua-ca-phe%2F&docid=HG6V-UU1u-J2QM&tbnid=eh9vxXloKwdGtM&vet=12ahUKEwjmy6HF07OMAxWNdfUHHTyMNjMQM3oECE8QAA..i&w=800&h=450&hcb=2&ved=2ahUKEwjmy6HF07OMAxWNdfUHHTyMNjMQM3oECE8QAA", 100000));
        sampleProducts.add(new Product("3", "Trà sữa hoàng kim ", "https://www.google.com/imgres?q=tr%C3%A0%20s%E1%BB%AFa&imgurl=https%3A%2F%2Fsimexcodl.com.vn%2Fwp-content%2Fuploads%2F2024%2F05%2Ftra-sua-ca-phe-2.jpg&imgrefurl=https%3A%2F%2Fsimexcodl.com.vn%2Ftra-sua-ca-phe%2F&docid=HG6V-UU1u-J2QM&tbnid=eh9vxXloKwdGtM&vet=12ahUKEwjmy6HF07OMAxWNdfUHHTyMNjMQM3oECE8QAA..i&w=800&h=450&hcb=2&ved=2ahUKEwjmy6HF07OMAxWNdfUHHTyMNjMQM3oECE8QAA", 100000));
        sampleProducts.add(new Product("4", "Trà sữa hoàng kim ", "https://www.google.com/imgres?q=tr%C3%A0%20s%E1%BB%AFa&imgurl=https%3A%2F%2Fsimexcodl.com.vn%2Fwp-content%2Fuploads%2F2024%2F05%2Ftra-sua-ca-phe-2.jpg&imgrefurl=https%3A%2F%2Fsimexcodl.com.vn%2Ftra-sua-ca-phe%2F&docid=HG6V-UU1u-J2QM&tbnid=eh9vxXloKwdGtM&vet=12ahUKEwjmy6HF07OMAxWNdfUHHTyMNjMQM3oECE8QAA..i&w=800&h=450&hcb=2&ved=2ahUKEwjmy6HF07OMAxWNdfUHHTyMNjMQM3oECE8QAA", 100000));

        for (Product product : sampleProducts) {
            db.collection("products").document(product.getId())
                    .set(product)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Thêm sản phẩm thành công: " + product.getName()))
                    .addOnFailureListener(e -> Log.e("Firestore", "Lỗi khi thêm sản phẩm", e));
        }
    }

}
