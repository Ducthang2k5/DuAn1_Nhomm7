//package com.example.duan1_nhom7.User;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.duan1_nhom7.R;
//import com.example.duan1_nhom7.User.UserLoginActivity;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import java.util.HashMap;
//import java.util.Map;
//
//public class UserRegisterActivity extends AppCompatActivity {
//    private EditText edtUserFullName, edtUserEmail, edtUserPassword;
//    private Button btnUserRegister;
//    private TextView tvUserLogin;
//    private FirebaseAuth auth;
//    private FirebaseFirestore db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_register);
//
//        // Ánh xạ UI
//        edtUserFullName = findViewById(R.id.edtUserFullName);
//        edtUserEmail = findViewById(R.id.edtUserEmail);
//        edtUserPassword = findViewById(R.id.edtUserPassword);
//        btnUserRegister = findViewById(R.id.btnUserRegister);
//        tvUserLogin = findViewById(R.id.tvUserLogin);
//
//        auth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        // Xử lý đăng ký User
//        btnUserRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String fullName = edtUserFullName.getText().toString().trim();
//                String email = edtUserEmail.getText().toString().trim();
//                String password = edtUserPassword.getText().toString().trim();
//
//                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(UserRegisterActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Đăng ký Firebase Authentication
//                auth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                FirebaseUser firebaseUser = auth.getCurrentUser();
//                                if (firebaseUser != null) {
//                                    String userId = firebaseUser.getUid();
//
//                                    // Lưu thông tin User vào Firestore
//                                    Map<String, Object> userMap = new HashMap<>();
//                                    userMap.put("fullName", fullName);
//                                    userMap.put("email", email);
//                                    userMap.put("role", "user"); // Phân quyền User
//
//                                    db.collection("users").document(userId)
//                                            .set(userMap)
//                                            .addOnSuccessListener(aVoid -> {
//                                                Toast.makeText(UserRegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
//                                                startActivity(new Intent(UserRegisterActivity.this, UserLoginActivity.class));
//                                                finish();
//                                            })
//                                            .addOnFailureListener(e -> Toast.makeText(UserRegisterActivity.this, "Lỗi Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//                                }
//                            } else {
//                                Toast.makeText(UserRegisterActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        });
//            }
//        });
//
//        // Chuyển về màn hình đăng nhập
//        tvUserLogin.setOnClickListener(v -> startActivity(new Intent(UserRegisterActivity.this, UserLoginActivity.class)));
//    }
//}
