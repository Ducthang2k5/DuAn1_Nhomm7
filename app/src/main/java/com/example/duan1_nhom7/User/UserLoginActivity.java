package com.example.duan1_nhom7.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1_nhom7.Admin.AdminRegisterActivity;
import com.example.duan1_nhom7.Home;
import com.example.duan1_nhom7.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserLoginActivity extends AppCompatActivity {
    private EditText edtUserEmail, edtUserPassword;
    private Button btnUserLogin;
    private TextView tvUserRegister;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        // Ánh xạ UI
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserPassword = findViewById(R.id.edtUserPassword);
        btnUserLogin = findViewById(R.id.btnUserLogin);
        tvUserRegister = findViewById(R.id.tvUserRegister);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Xử lý đăng nhập User
        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtUserEmail.getText().toString().trim();
                String password = edtUserPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(UserLoginActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Đăng nhập Firebase Authentication
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                if (firebaseUser != null) {
                                    String userId = firebaseUser.getUid();

                                    // Kiểm tra role trong Firestore
                                    db.collection("users").document(userId)
                                            .get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                if (documentSnapshot.exists()) {
                                                    String role = documentSnapshot.getString("role");

                                                    if ("user".equals(role)) {
                                                        Toast.makeText(UserLoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(UserLoginActivity.this, Home.class));
                                                        finish();
                                                    } else {
                                                        auth.signOut(); // Đăng xuất nếu không phải User
                                                        Toast.makeText(UserLoginActivity.this, "Tài khoản không có quyền User!", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    auth.signOut();
                                                    Toast.makeText(UserLoginActivity.this, "Lỗi dữ liệu người dùng!", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(UserLoginActivity.this, "Lỗi Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                }
                            } else {
                                Toast.makeText(UserLoginActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        // Chuyển sang màn hình đăng ký User
        tvUserRegister.setOnClickListener(v -> startActivity(new Intent(UserLoginActivity.this, AdminRegisterActivity.class)));
    }
}
