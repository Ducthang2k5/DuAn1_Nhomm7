package com.example.duan1_nhom7.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.duan1_nhom7.R;
import com.example.duan1_nhom7.User.UserLoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminRegisterActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Spinner spinnerRole;
    private Button btnRegister;
    private TextView tvLogin;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.admin_register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ánh xạ UI
        edtEmail = findViewById(R.id.edtEmailRegister);
        edtPassword = findViewById(R.id.edtPasswordRegister);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Cấu hình dropdown Role (User / Admin)
        String[] roles = {"user", "admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        spinnerRole.setAdapter(adapter);

        // Xử lý đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String role = spinnerRole.getSelectedItem().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(AdminRegisterActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Đăng ký tài khoản Firebase
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                if (firebaseUser != null) {
                                    String userId = firebaseUser.getUid();

                                    // Tạo object lưu vào Firestore
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("email", email);
                                    userData.put("role", role);

                                    db.collection("users").document(userId)
                                            .set(userData)
                                            .addOnSuccessListener(aVoid -> {
                                                // Gửi email xác thực
                                                firebaseUser.sendEmailVerification()
                                                        .addOnSuccessListener(unused -> Toast.makeText(AdminRegisterActivity.this, "Vui lòng xác nhận email!", Toast.LENGTH_LONG).show())
                                                        .addOnFailureListener(e -> Toast.makeText(AdminRegisterActivity.this, "Lỗi gửi email xác thực: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                                                // Chuyển màn hình theo vai trò
                                                if ("admin".equals(role)) {
                                                    startActivity(new Intent(AdminRegisterActivity.this, AdminLoginActivity.class));
                                                } else {
                                                    startActivity(new Intent(AdminRegisterActivity.this, UserLoginActivity.class));
                                                }
                                                finish();
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(AdminRegisterActivity.this, "Lỗi Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                }
                            } else {
                                Toast.makeText(AdminRegisterActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        // Chuyển về màn hình Login
        tvLogin.setOnClickListener(v -> startActivity(new Intent(AdminRegisterActivity.this, AdminLoginActivity.class)));
    }
}
