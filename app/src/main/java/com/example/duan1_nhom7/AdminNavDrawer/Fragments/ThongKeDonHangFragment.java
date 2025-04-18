package com.example.duan1_nhom7.AdminNavDrawer.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1_nhom7.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class ThongKeDonHangFragment extends Fragment {

    private DatePicker datePicker;
    private Button btnThongKe;
    private TextView tvTongTien;

    private FirebaseFirestore db;

    public ThongKeDonHangFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thong_ke_don_hang, container, false);

        datePicker = view.findViewById(R.id.datePicker);
        btnThongKe = view.findViewById(R.id.btnThongKe);
        tvTongTien = view.findViewById(R.id.tvTongTien);

        db = FirebaseFirestore.getInstance();

        btnThongKe.setOnClickListener(v -> thongKeTuFirebase());

        return view;
    }

    private void thongKeTuFirebase() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth(); // Tháng tính từ 0
        int year = datePicker.getYear();

        // Tính ngày bắt đầu và kết thúc
        Calendar calStart = Calendar.getInstance();
        calStart.set(year, month, day, 0, 0, 0);
        calStart.set(Calendar.MILLISECOND, 0);
        Date startDate = calStart.getTime();

        Calendar calEnd = (Calendar) calStart.clone();
        calEnd.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calEnd.getTime();

        CollectionReference ordersRef = db.collection("orders");

        ordersRef
                .whereGreaterThanOrEqualTo("timestamp", startDate)
                .whereLessThan("timestamp", endDate)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    long tongTien = 0;
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Long total = doc.getLong("totalPayment");
                        if (total != null) {
                            tongTien += total;
                        }
                    }
                    tvTongTien.setText("Tổng tiền: " + tongTien + " VND");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
