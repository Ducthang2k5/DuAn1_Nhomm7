package com.example.duan1_nhom7.Voucher;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.duan1_nhom7.R;

public class VoucherActitity extends AppCompatActivity {
    CardView cardNoVoucher, cardVoucher50;
    CardView tvNoVoucher, tvVoucher50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voucher_actitity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cardNoVoucher = findViewById(R.id.cardNoVoucher);
        cardVoucher50 = findViewById(R.id.cardVoucher50);
        tvNoVoucher = findViewById(R.id.cardVoucher50);
        tvVoucher50 = findViewById(R.id.cardNoVoucher);

        cardNoVoucher.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("voucherName", "Không dùng voucher");
            resultIntent.putExtra("voucherDiscount", 0);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        cardVoucher50.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("voucherName", "Giảm 50.000₫");
            resultIntent.putExtra("voucherDiscount", 50000);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}