package com.example.duan1_nhom7.Admin.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.duan1_nhom7.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminConfirmProductFragment extends Fragment {

    private ListView lvPendingProducts;
    private ArrayList<String> pendingProducts;
    private ArrayList<String> productIds;
    private FirebaseFirestore db;
    private ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_confirm_product, container, false);

        lvPendingProducts = view.findViewById(R.id.lvPendingProducts);
        pendingProducts = new ArrayList<>();
        productIds = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, pendingProducts);
        lvPendingProducts.setAdapter(adapter);

        lvPendingProducts.setOnItemClickListener((parent, view1, position, id) -> {
            String productId = productIds.get(position);
            showConfirmDialog(productId, position);
        });

        loadPendingProducts();

        return view;
    }

    private void loadPendingProducts() {
        db.collection("products")
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(query -> {
                    pendingProducts.clear();
                    productIds.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        String name = doc.getString("name");
                        pendingProducts.add(name != null ? name : "Không tên");
                        productIds.add(doc.getId());
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void showConfirmDialog(String productId, int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận sản phẩm")
                .setMessage("Bạn có muốn xác nhận sản phẩm: " + pendingProducts.get(position) + "?")
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    db.collection("products")
                            .document(productId)
                            .update("status", "confirmed")
                            .addOnSuccessListener(unused -> {
                                pendingProducts.remove(position);
                                productIds.remove(position);
                                adapter.notifyDataSetChanged();
                            });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
