package com.example.duan1_nhom7.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom7.Cart.CartActivity;
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

    private ImageButton btnCartt;



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



        btnCartt = view.findViewById(R.id.btnCart);

        if (btnCartt != null) {
            btnCartt.setOnClickListener(v -> {
                // Khi nhấn nút giỏ hàng, chuyển sang CartActivity
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            });
        } else {
            Log.e("HomeFragment", "btnAddToCart is not found!");
        }



        return view;





    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment); // Thay thế fragment hiện tại bằng CartFragment
        transaction.addToBackStack(null); // Nếu muốn có thể quay lại fragment trước đó
        transaction.commit(); // Áp dụng transaction
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
        sampleProducts.add(new Product("1", "Trà sữa hoàng kim ", "https://simexcodl.com.vn/wp-content/uploads/2024/05/tra-sua-ca-phe-2.jpg", 25000));
        sampleProducts.add(new Product("2", "Trà sữa bạc hà ", "https://simexcodl.com.vn/wp-content/uploads/2024/05/tra-sua-ca-phe-2.jpg", 30000));
        sampleProducts.add(new Product("3", "Trà sữa trân trâu đường đen ", "https://simexcodl.com.vn/wp-content/uploads/2024/05/tra-sua-ca-phe-2.jpg", 36000));
        sampleProducts.add(new Product("4", "Trà sữa khoai môn ", "https://simexcodl.com.vn/wp-content/uploads/2024/05/tra-sua-ca-phe-2.jpg", 35000));
        for (Product product : sampleProducts) {
            db.collection("products").document(product.getId())
                    .set(product)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Thêm sản phẩm thành công: " + product.getName()))
                    .addOnFailureListener(e -> Log.e("Firestore", "Lỗi khi thêm sản phẩm", e));
        }
    }

}
