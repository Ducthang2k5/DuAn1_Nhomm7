package com.example.duan1_nhom7;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan1_nhom7.Product.Product;
import com.example.duan1_nhom7.Product.ProductAdapter;
import com.example.duan1_nhom7.Product.SliderAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {

    private ViewPager2 viewPager;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        // Slideshow
        viewPager = findViewById(R.id.viewPager);
        List<String> sliderImages = Arrays.asList(
                "https://via.placeholder.com/400x200/FF5733/FFFFFF?text=Slide1",
                "https://via.placeholder.com/400x200/33FF57/FFFFFF?text=Slide2",
                "https://via.placeholder.com/400x200/3357FF/FFFFFF?text=Slide3"
        );
        sliderAdapter = new SliderAdapter(this, sliderImages);
        viewPager.setAdapter(sliderAdapter);

        // Danh sách sản phẩm
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Sản phẩm 1", 100, "https://via.placeholder.com/150"));
        productList.add(new Product("Sản phẩm 2", 200, "https://via.placeholder.com/150"));
        productList.add(new Product("Sản phẩm 3", 300, "https://via.placeholder.com/150"));

        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);
    }
}