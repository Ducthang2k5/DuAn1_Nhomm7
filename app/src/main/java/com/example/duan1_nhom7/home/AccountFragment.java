package com.example.duan1_nhom7.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.duan1_nhom7.home.model.Account;
import com.example.duan1_nhom7.password.PasswordChangeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AccountFragment extends Fragment {
    private ImageView imgAvatar;
    private TextView txtFullName, txtEmail, txtPhone, txtBirthday, txtAddress;
    private LinearLayout btnLogout, btnChangePassword;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            Log.d("User UID", "UID của người dùng hiện tại: " + uid);
        } else {
            Log.e("Firebase", "Người dùng chưa đăng nhập!");
        }

        if (auth.getCurrentUser() != null) {
            addSampleUser();
        } else {
            Log.e("Firebase", "Lỗi: Người dùng chưa đăng nhập!");
        }

        Button btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());

        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtFullName = view.findViewById(R.id.txtFullName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtBirthday = view.findViewById(R.id.txtBirthday);
        txtAddress = view.findViewById(R.id.txtAddress);
        btnLogout = view.findViewById(R.id.userFrgmDangXuat);
        btnChangePassword = view.findViewById(R.id.userFrgmDoiMK);

        loadUserData();

        btnLogout.setOnClickListener(v -> logout());
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PasswordChangeActivity.class);
            startActivity(intent);
        });

        imgAvatar.setOnClickListener(v -> openImageChooser());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(getContext()).load(imageUri).into(imgAvatar);
            uploadImageToFirebase();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadImageToFirebase() {
        if (imageUri == null) return;

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("avatars/" + uid + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            db.collection("account").document(uid)
                                    .update("avatarUrl", imageUrl)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Ảnh đại diện đã cập nhật", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi lưu ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Upload ảnh thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            updateUserEmail(user);
            loadUserData();
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
                        txtEmail.setText(documentSnapshot.getString("email"));
                        txtPhone.setText(documentSnapshot.getString("phone"));
                        txtBirthday.setText(documentSnapshot.getString("birthday"));
                        txtAddress.setText(documentSnapshot.getString("address"));

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
                        Account newAccount = new Account(
                                userId,
                                "Tên Người Dùng",
                                "email@example.com",
                                "",
                                "Số điện thoại",
                                "Ngày sinh ",
                                "Địa chỉ giao hàng"
                        );

                        db.collection("account").document(userId).set(newAccount)
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Tạo tài khoản người dùng mới thành công"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Lỗi khi tạo tài khoản mới", e));
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Lỗi khi kiểm tra dữ liệu", e));
    }

    private void showEditProfileDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_profile, null);
        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtPhone = dialogView.findViewById(R.id.edtPhone);
        EditText edtBirthday = dialogView.findViewById(R.id.edtBirthday);
        EditText edtAddress = dialogView.findViewById(R.id.edtAddress);

        edtName.setText(txtFullName.getText().toString());
        edtPhone.setText(txtPhone.getText().toString());
        edtBirthday.setText(txtBirthday.getText().toString());
        edtAddress.setText(txtAddress.getText().toString());

        new AlertDialog.Builder(getContext())
                .setTitle("Sửa thông tin")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String uid = user.getUid();
                        db.collection("account").document(uid)
                                .update(
                                        "fullName", edtName.getText().toString(),
                                        "phone", edtPhone.getText().toString(),
                                        "birthday", edtBirthday.getText().toString(),
                                        "address", edtAddress.getText().toString()
                                )
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    loadUserData();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void logout() {
        auth.signOut();
        Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
