package com.example.duan1_nhom7.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1_nhom7.AdminNavDrawer.MainAdminActivity;
import com.example.duan1_nhom7.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText edtAdminEmail, edtAdminPassword;
    private Button btnAdminLogin;
    private TextView tvAdminRegister;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Ánh xạ UI
        edtAdminEmail = findViewById(R.id.edtEmailLogin);
        edtAdminPassword = findViewById(R.id.edtPasswordLogin);
        btnAdminLogin = findViewById(R.id.btnLogin);
        tvAdminRegister = findViewById(R.id.tvRegister);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Xử lý đăng nhập Admin
        btnAdminLogin.setOnClickListener(v -> {
            String email = edtAdminEmail.getText().toString().trim();
            String password = edtAdminPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(AdminLoginActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                checkAdminRole(user.getUid());
                            }
                        } else {
                            Toast.makeText(AdminLoginActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Chuyển đến màn đăng ký Admin
        tvAdminRegister.setOnClickListener(v -> startActivity(new Intent(AdminLoginActivity.this, AdminRegisterActivity.class)));
    }

    private void checkAdminRole(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        if ("admin".equals(role)) {
                            // Đăng nhập thành công, chuyển sang Admin Dashboard
                            Toast.makeText(AdminLoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminLoginActivity.this, MainAdminActivity.class));
                            finish();
                        } else {
                            // Không phải Admin => Đăng xuất
                            auth.signOut();
                            Toast.makeText(AdminLoginActivity.this, "Bạn không có quyền truy cập!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(AdminLoginActivity.this, "Lỗi Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
