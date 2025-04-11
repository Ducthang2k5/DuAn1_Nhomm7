package com.example.duan1_nhom7.AdminNavDrawer.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1_nhom7.AdminNavDrawer.Adapter.DuyetDonAdapter;
import com.example.duan1_nhom7.Payment.Model.Order;
import com.example.duan1_nhom7.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class DuyetDonFragment extends Fragment {

    private RecyclerView recyclerView;
    private DuyetDonAdapter adapter;
    private List<Order> orderList;
    private FirebaseFirestore db;

    public DuyetDonFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duyet_don, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDuyetDon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        orderList = new ArrayList<>();
        adapter = new DuyetDonAdapter(orderList, getContext(), this::handleOrderAction);
        recyclerView.setAdapter(adapter);

        loadOrders();

        return view;
    }

    private void loadOrders() {
        db.collection("orders")
                .whereEqualTo("status", "ordered")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    orderList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Order order = doc.toObject(Order.class);
                        order.setOrderId(doc.getId());
                        orderList.add(order);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi tải đơn hàng", Toast.LENGTH_SHORT).show());
    }

    private void handleOrderAction(Order order, String action) {
        String newStatus = action.equals("approve") ? "confirmed" : "canceled";

        db.collection("orders").document(order.getOrderId())
                .update("status", newStatus)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Đã " + (newStatus.equals("confirmed") ? "duyệt" : "hủy") + " đơn", Toast.LENGTH_SHORT).show();
                    loadOrders();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Thao tác thất bại", Toast.LENGTH_SHORT).show());
    }
}
