package com.example.duan1_nhom7.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.duan1_nhom7.R;
import com.example.duan1_nhom7.User.UserLoginActivity;
import com.example.duan1_nhom7.Welcome.WelcomeActivity;
import com.example.duan1_nhom7.model.Account;
import com.example.duan1_nhom7.password.PasswordChangeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountFragment extends Fragment {
    private ImageView imgAvatar;
    private TextView txtFullName, txtEmail, txtPhone, txtBirthday, txtAddress;
    private LinearLayout btnLogout, btnChangePassword; // Thêm biến cho nút Đổi Mật Khẩu
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Khởi tạo FirebaseAuth và Firestore trước khi gọi addSampleUser()
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Kiểm tra xem người dùng đã đăng nhập chưa và in ra UID
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            Log.d("User UID", "UID của người dùng hiện tại: " + uid); // In ra UID
        } else {
            Log.e("Firebase", "Người dùng chưa đăng nhập!");
        }

        // Kiểm tra nếu chưa đăng nhập thì không gọi addSampleUser()
        if (auth.getCurrentUser() != null) {
            addSampleUser();
        } else {
            Log.e("Firebase", "Lỗi: Người dùng chưa đăng nhập!");
        }

        // Ánh xạ view
        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtFullName = view.findViewById(R.id.txtFullName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtBirthday = view.findViewById(R.id.txtBirthday);
        txtAddress = view.findViewById(R.id.txtAddress);
        btnLogout = view.findViewById(R.id.userFrgmDangXuat);
        btnChangePassword = view.findViewById(R.id.userFrgmDoiMK);  // Ánh xạ nút Đổi Mật Khẩu

        // Tải dữ liệu từ Firestore
        loadUserData();

        // Xử lý sự kiện đăng xuất
        btnLogout.setOnClickListener(v -> logout());

        // Xử lý sự kiện đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PasswordChangeActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            updateUserEmail(user);  // Cập nhật email khi người dùng đăng nhập
            loadUserData();  // Tải lại dữ liệu người dùng
        }
    }

    private void updateUserEmail(FirebaseUser user) {
        String userId = user.getUid();
        String email = user.getEmail();

        if (email != null && !email.isEmpty()) {
            db.collection("account").document(userId)
                    .update("email", email)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Cập nhật email thành công"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Lỗi khi cập nhật email", e));
        }
    }

    private void loadUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        db.collection("account").document(userId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(getContext(), "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        txtFullName.setText(documentSnapshot.getString("fullName"));
                        txtEmail.setText(documentSnapshot.getString("email"));  // Hiển thị email
                        txtPhone.setText(documentSnapshot.getString("phone"));
                        txtBirthday.setText(documentSnapshot.getString("birthday"));
                        txtAddress.setText(documentSnapshot.getString("address"));

                        // Hiển thị ảnh đại diện nếu có
                        String avatarUrl = documentSnapshot.getString("avatarUrl");
                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            Glide.with(getContext()).load(avatarUrl).into(imgAvatar);
                        }
                    }
                });
    }




    private void addSampleUser() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Log.e("Firebase", "Lỗi: Người dùng chưa đăng nhập!");
            return;
        }

        String userId = currentUser.getUid();

        db.collection("account").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        // Tạo tài khoản mới cho người dùng
                        Account newAccount = new Account(
                                userId,
                                "Tên Người Dùng",  // Bạn có thể lấy từ tài khoản Firebase hoặc yêu cầu người dùng nhập
                                "email@example.com", // Email của người dùng
                                "",  // Bạn có thể cung cấp ảnh đại diện mặc định
                                "Số điện thoại",  // Số điện thoại mặc định
                                "Ngày sinh ",  // Ngày sinh mặc định
                                "Địa chỉ giao hàng"   // Địa chỉ mặc định
                        );

                        db.collection("account").document(userId).set(newAccount)
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Tạo tài khoản người dùng mới thành công"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Lỗi khi tạo tài khoản mới", e));
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Lỗi khi kiểm tra dữ liệu", e));
    }






    private void logout() {
        auth.signOut();
        Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(getActivity(), WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

