package com.example.duan1_nhom7.Product;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.duan1_nhom7.Cart.Model.CartItem;
import com.example.duan1_nhom7.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailActivity";
    private ImageView productImage;
    private TextView productName, productPrice;
    private RadioGroup sizeGroup;

    private RadioGroup sugarGroup;
    private Button btnAddToCart;
    private String productId;
    private String selectedSize = "S";  // Mặc định size là S

    private String selectedSugar = "100%";

    private FirebaseFirestore db;
    private String currentImageUrl = ""; // Lưu URL ảnh hiện tại
    private Map<String, String> sizeImageUrls = new HashMap<>(); // Lưu URL ảnh cho mỗi size
    private double basePrice = 0; // Giá cơ bản

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        sugarGroup = findViewById(R.id.sugarGroup);
        sizeGroup = findViewById(R.id.sizeGroup);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        db = FirebaseFirestore.getInstance();
        productId = getIntent().getStringExtra("productId");

        // Tải thông tin sản phẩm và lưu trữ
        loadProductDetails();

        sizeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.sizeS) {
                selectedSize = "S";
            } else if (checkedId == R.id.sizeM) {
                selectedSize = "M";
            } else if (checkedId == R.id.sizeL) {
                selectedSize = "L";
            }
            updatePriceAndImage(); // Cập nhật giá và ảnh dựa trên size
        });

        sugarGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.sugar100) {
                selectedSugar = "100%";
            } else if (checkedId == R.id.sugar75) {
                selectedSugar = "75%";
            } else if (checkedId == R.id.sugar50) {
                selectedSugar = "50%";
            }
        });

        btnAddToCart.setOnClickListener(v -> addToCart());
    }

    private void loadProductDetails() {
        db.collection("products").document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Lưu dữ liệu sản phẩm
                        storeProductData(documentSnapshot);

                        // Hiển thị dữ liệu cho size mặc định S
                        updatePriceAndImage();
                    } else {
                        Toast.makeText(this, "Không tìm thấy thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading product details", e);
                    Toast.makeText(this, "Lỗi khi tải thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                });
    }

    private void storeProductData(DocumentSnapshot document) {
        // Lưu tên sản phẩm
        String name = document.getString("name");
        productName.setText(name);

        // Lưu giá cơ bản
        Double price = document.getDouble("price");
        if (price != null) {
            basePrice = price;
        }

        // Lưu URL ảnh cho mỗi size
        String imageS = document.getString("imageUrl_S");
        String imageM = document.getString("imageUrl_M");
        String imageL = document.getString("imageUrl_L");

        if (imageS != null && !imageS.isEmpty()) {
            sizeImageUrls.put("S", imageS);
            // Lưu URL ảnh mặc định
            currentImageUrl = imageS;
        }

        // Nếu không có ảnh riêng cho size M/L, sử dụng ảnh của size S
        if (imageM != null && !imageM.isEmpty()) {
            sizeImageUrls.put("M", imageM);
        } else if (imageS != null) {
            sizeImageUrls.put("M", imageS);
        }

        if (imageL != null && !imageL.isEmpty()) {
            sizeImageUrls.put("L", imageL);
        } else if (imageS != null) {
            sizeImageUrls.put("L", imageS);
        }

        Log.d(TAG, "Stored image URLs: S=" + sizeImageUrls.get("S") +
                ", M=" + sizeImageUrls.get("M") +
                ", L=" + sizeImageUrls.get("L"));
    }

    private void updatePriceAndImage() {
        // Cập nhật giá theo size
        int finalPrice = (int) basePrice;
        if (selectedSize.equals("M")) {
            finalPrice += 5000;
        } else if (selectedSize.equals("L")) {
            finalPrice += 10000;
        }
        productPrice.setText(finalPrice + " VNĐ");

        // Cập nhật ảnh theo size
        String imageUrl = sizeImageUrls.get(selectedSize);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            currentImageUrl = imageUrl;
            Log.d(TAG, "Loading image for size " + selectedSize + ": " + imageUrl);

            // Sử dụng Glide với các tùy chọn để đảm bảo ảnh luôn được tải lại
            Glide.with(ProductDetailActivity.this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)  // Không lưu cache
                    .skipMemoryCache(true)  // Không sử dụng bộ nhớ cache
                    .into(productImage);
        } else {
            Log.w(TAG, "No image URL found for size " + selectedSize);
        }
    }

    private void addToCart() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String productName = this.productName.getText().toString();
            String productPrice = this.productPrice.getText().toString();

            // Đảm bảo có URL ảnh
            String imageToSave = currentImageUrl;
            if (imageToSave == null || imageToSave.isEmpty()) {
                imageToSave = sizeImageUrls.get("S"); // Sử dụng ảnh mặc định nếu không có
            }

            CartItem cartItem = new CartItem(
                    productId,
                    productName,
                    productPrice,
                    imageToSave,
                    selectedSize,
                    selectedSugar,
                    1
            );

            String cartItemId = productId + "_" + selectedSize + "_" + selectedSugar;

            db.collection("carts").document(currentUser.getUid())
                    .collection("items").document(cartItemId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Sản phẩm đã tồn tại, chỉ cập nhật số lượng
                            Long currentQuantity = documentSnapshot.getLong("productQuantity");
                            if (currentQuantity != null) {
                                int newQuantity = currentQuantity.intValue() + 1;
                                cartItem.setProductQuantity(newQuantity);
                            } else {
                                // Trường productQuantity chưa tồn tại, khởi tạo với giá trị 1
                                cartItem.setProductQuantity(1);
                            }

                            db.collection("carts").document(currentUser.getUid())
                                    .collection("items").document(cartItemId)
                                    .set(cartItem, SetOptions.merge())
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Đã cập nhật số lượng trong giỏ hàng", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Cập nhật số lượng thất bại", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // Sản phẩm chưa tồn tại, thêm mới
                            db.collection("carts").document(currentUser.getUid())
                                    .collection("items").document(cartItemId)
                                    .set(cartItem)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi khi kiểm tra sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }
}