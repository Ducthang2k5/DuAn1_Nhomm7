package com.example.duan1_nhom7.Admin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.duan1_nhom7.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdminUserEditFragment extends Fragment {

    private EditText edtUserName, edtEmail;
    private Button btnSave;
    private FirebaseFirestore db;
    private String userId = "userId1"; // TODO: truyền động userId

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user_edit, container, false);

        edtUserName = view.findViewById(R.id.edtUserName);
        edtEmail = view.findViewById(R.id.edtEmail);
        btnSave = view.findViewById(R.id.btnSave);
        db = FirebaseFirestore.getInstance();

        loadUserInfo();

        btnSave.setOnClickListener(v -> updateUserInfo());

        return view;
    }

    private void loadUserInfo() {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    edtUserName.setText(snapshot.getString("name"));
                    edtEmail.setText(snapshot.getString("email"));
                });
    }

    private void updateUserInfo() {
        String name = edtUserName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        db.collection("users")
                .document(userId)
                .update("name", name, "email", email)
                .addOnSuccessListener(unused ->
                        Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                );
    }
}
