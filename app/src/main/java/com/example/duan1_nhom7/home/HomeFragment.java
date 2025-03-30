package com.example.duan1_nhom7.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan1_nhom7.Product.Product;
import com.example.duan1_nhom7.Product.ProductAdapter;
import com.example.duan1_nhom7.Product.SliderAdapter;
import com.example.duan1_nhom7.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ViewPager2 viewPager;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private View selectedView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Slideshow
        viewPager = view.findViewById(R.id.viewPager);
        List<String> sliderImages = Arrays.asList(
                "https://picsum.photos/400/200?random=1",
                "https://picsum.photos/400/200?random=2",
                "https://picsum.photos/400/200?random=3"
        );
        SliderAdapter sliderAdapter = new SliderAdapter(requireContext(), sliderImages);
        viewPager.setAdapter(sliderAdapter);

        // Danh sách sản phẩm
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        productList = new ArrayList<>();
        productList.add(new Product("Sản phẩm 1", 100, "https://picsum.photos/400/200?random=1"));
        productList.add(new Product("Sản phẩm 2", 200, "https://picsum.photos/400/200?random=1"));
        productList.add(new Product("Sản phẩm 3", 300, "https://picsum.photos/400/200?random=1"));
        productList.add(new Product("Sản phẩm 4", 400, "https://picsum.photos/400/200?random=1"));

        productAdapter = new ProductAdapter(getActivity(), productList, this);
        recyclerView.setAdapter(productAdapter);

        return view;
    }

    // Cập nhật selectedView khi nhấn vào ảnh
    public void setSelectedView(View view) {
        this.selectedView = view;
        openFileChooser();
    }

    // Mở thư viện ảnh để chọn ảnh mới
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Xử lý kết quả chọn ảnh từ thư viện
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                // 🔹 Tìm View cha của ImageView được nhấn
                View parentView = recyclerView.findContainingItemView(selectedView);
                if (parentView != null) {
                    int selectedPosition = recyclerView.getChildAdapterPosition(parentView);
                    if (selectedPosition != RecyclerView.NO_POSITION) {
                        productAdapter.updateProductImage(selectedPosition, imageUri.toString());
                    }
                }
            }
        }
    }

}
