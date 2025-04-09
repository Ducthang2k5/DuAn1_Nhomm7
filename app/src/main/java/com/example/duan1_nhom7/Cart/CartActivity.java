package com.example.duan1_nhom7.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom7.Cart.Model.CartItem;
import com.example.duan1_nhom7.Payment.CheckOutAcvivity;
import com.example.duan1_nhom7.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private ArrayList<CartItem> cartItemList;
    private TextView cartTotalPrice;

    private Button proceedToCheckoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        // Khởi tạo các view
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartTotalPrice = findViewById(R.id.cartTotalPrice);

        // Khởi tạo danh sách giỏ hàng và adapter
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(this,cartItemList);

        proceedToCheckoutButton = findViewById(R.id.btnProceedToCheckout);
        proceedToCheckoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckOutAcvivity.class);
            startActivity(intent);
        });

        // Lắng nghe thay đổi số lượng/xóa item để cập nhật tổng tiền
        cartAdapter.setOnCartItemChangeListener(new CartAdapter.OnCartItemChangeListener() {
            @Override
            public void onQuantityChanged() {
                updateCartTotal();
            }

            @Override
            public void onItemDeleted() {
                updateCartTotal();
            }
        });

        // Thiết lập RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        // Thiết lập window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tải dữ liệu giỏ hàng
        loadCartItems();
    }

    private void loadCartItems() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            db = FirebaseFirestore.getInstance();
            db.collection("carts").document(currentUser.getUid())
                    .collection("items")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        cartItemList.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            CartItem item = documentSnapshot.toObject(CartItem.class);
                            cartItemList.add(item);
                        }
                        cartAdapter.notifyDataSetChanged();
                        updateCartTotal();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CartActivity.this, "Không thể tải giỏ hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(CartActivity.this, "Vui lòng đăng nhập để xem giỏ hàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCartTotal() {
        int total = 0;
        for (CartItem cartItem : cartItemList) {
            String rawPrice = cartItem.getProductPrice().replaceAll("[^\\d]", "");
            int itemPrice = Integer.parseInt(rawPrice);
            int itemQuantity = cartItem.getProductQuantity();
            total += itemPrice * itemQuantity;
        }
        cartTotalPrice.setText(total + " VNĐ");
    }
}