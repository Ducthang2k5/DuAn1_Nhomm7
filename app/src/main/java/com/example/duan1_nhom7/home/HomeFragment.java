package com.example.duan1_nhom7.home;

import android.content.Context;
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
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan1_nhom7.Cart.CartActivity;
import com.example.duan1_nhom7.Product.Product;
import com.example.duan1_nhom7.Product.ProductAdapter;
import com.example.duan1_nhom7.Product.SliderAdapter;
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

    private ViewPager2 viewPager2;

    private ImageButton btnCartt;

    private OnCartClickListener mListener;

    public interface OnCartClickListener {
        void onCartClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCartClickListener) {
            mListener = (OnCartClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCartClickListener");
        }
    }



    



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        viewPager2 = view.findViewById(R.id.viewPager);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productList = new ArrayList<>();
        adapter = new ProductAdapter(getActivity(), productList, this);
        recyclerView.setAdapter(adapter);


        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://gongcha.com.vn/wp-content/uploads/2022/06/Tra-sua-tran-chau-HK.png");
        imageUrls.add("https://maycha.com.vn/wp-content/uploads/2023/10/tra-sua-tran-chau-hoang-kim.png");
        imageUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLro0x-F5aknipUJxYv9cE3eOuctbuk6-Ymw&s");

        SliderAdapter sliderAdapter = new SliderAdapter(getContext(), imageUrls);
        viewPager2.setAdapter(sliderAdapter);


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
                // Gọi phương thức từ HomeActivity để mở CartActivity
                if (mListener != null) {
                    mListener.onCartClicked();
                }
            });
        } else {
            Log.e("HomeFragment", "btnCart is not found!");
        }


        return view;


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        sampleProducts.add(new Product("1", "Trà sữa hoàng kim ", "https://gongcha.com.vn/wp-content/uploads/2022/06/Tra-sua-tran-chau-HK.png", 25000));
        sampleProducts.add(new Product("2", "Trà sữa bạc hà ", "https://maycha.com.vn/wp-content/uploads/2023/10/tra-sua-tran-chau-hoang-kim.png", 30000));
        sampleProducts.add(new Product("3", "Trà sữa trân trâu đường đen ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLro0x-F5aknipUJxYv9cE3eOuctbuk6-Ymw&s", 36000));
        sampleProducts.add(new Product("4", "Trà sữa khoai môn ", "https://gongcha.com.vn/wp-content/uploads/2018/01/Tr%C3%A0-s%E1%BB%AFa-Khoai-m%C3%B4n-2.png", 35000));
        for (Product product : sampleProducts) {
            db.collection("products").document(product.getId())
                    .set(product)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Thêm sản phẩm thành công: " + product.getName()))
                    .addOnFailureListener(e -> Log.e("Firestore", "Lỗi khi thêm sản phẩm", e));
        }
    }

}
