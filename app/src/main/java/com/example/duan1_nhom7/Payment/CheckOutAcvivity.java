package com.example.duan1_nhom7.Payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom7.Cart.CartAdapter;
import com.example.duan1_nhom7.Cart.Model.CartItem;
import com.example.duan1_nhom7.R;
import com.example.duan1_nhom7.Voucher.VoucherActitity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CheckOutAcvivity extends AppCompatActivity {

    private EditText edtName, edtPhone, edtAddress, edtNote;
    private TextView tvSubtotal, tvShippingFee, tvVoucherDiscount, tvTotalPayment, tvSelectedVoucher;
    private RecyclerView recyclerViewCart;
    private RadioGroup radioGroupPayment;
    private Button btnPlaceOrder;
    private LinearLayout btnChooseVoucher;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<CartItem> cartItemList;

    private int subtotal = 0;
    private int shippingFee = 20000;
    private int voucherDiscount = 0;
    private String selectedVoucher = "Không dùng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out_acvivity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        loadCartItems();
        handleEvents();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtNote = findViewById(R.id.edtNote);

        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvVoucherDiscount = findViewById(R.id.tvVoucherDiscount);
        tvTotalPayment = findViewById(R.id.tvTotalPayment);
        tvSelectedVoucher = findViewById(R.id.tvSelectedVoucher);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnChooseVoucher = findViewById(R.id.btnChooseVoucher);

        cartItemList = new ArrayList<>();
    }

    private void loadCartItems() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("carts")
                .document(userId)
                .collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cartItemList.clear();
                    subtotal = 0;

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        CartItem item = doc.toObject(CartItem.class);
                        cartItemList.add(item);

                        String rawPrice = item.getProductPrice().replaceAll("[^\\d]", "");
                        int price = Integer.parseInt(rawPrice);
                        subtotal += price * item.getProductQuantity();
                    }

                    recyclerViewCart.setAdapter(new CartAdapter(this, cartItemList));
                    updatePriceViews();
                });
    }

    private void updatePriceViews() {
        tvSubtotal.setText(formatCurrency(subtotal));
        tvShippingFee.setText(formatCurrency(shippingFee));
        tvVoucherDiscount.setText("-" + formatCurrency(voucherDiscount));

        int totalPayment = subtotal + shippingFee - voucherDiscount;
        tvTotalPayment.setText(formatCurrency(totalPayment));
    }

    private void handleEvents() {
        btnChooseVoucher.setOnClickListener(v -> {
            Intent intent = new Intent(this, VoucherActitity.class);
            startActivityForResult(intent, 1);
        });

        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String note = edtNote.getText().toString().trim();
        String paymentMethod = radioGroupPayment.getCheckedRadioButtonId() == R.id.radioCOD ? "COD" : "QR Code";

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin người nhận", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        String orderId = UUID.randomUUID().toString();

        Map<String, Object> order = new HashMap<>();
        order.put("orderId", orderId);
        order.put("userId", userId);
        order.put("name", name);
        order.put("phone", phone);
        order.put("address", address);
        order.put("note", note);
        order.put("paymentMethod", paymentMethod);
        order.put("voucher", selectedVoucher);
        order.put("subtotal", subtotal);
        order.put("shippingFee", shippingFee);
        order.put("voucherDiscount", voucherDiscount);
        order.put("totalPayment", subtotal + shippingFee - voucherDiscount);
        order.put("timestamp", FieldValue.serverTimestamp());
        order.put("status", "ordered");

        db.collection("orders")
                .document(orderId)
                .set(order)
                .addOnSuccessListener(unused -> {
                    for (CartItem item : cartItemList) {
                        db.collection("orders")
                                .document(orderId)
                                .collection("items")
                                .add(item);
                    }

                    deleteCart(userId);
                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void deleteCart(String userId) {
        db.collection("carts")
                .document(userId)
                .collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        doc.getReference().delete();
                    }
                });
    }

    private String formatCurrency(int amount) {
        return String.format("%,d₫", amount).replace(",", ".");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedVoucher = data.getStringExtra("voucher_name");
            voucherDiscount = data.getIntExtra("voucher_discount", 0);

            tvSelectedVoucher.setText(selectedVoucher);
            updatePriceViews();
        }
    }
}
