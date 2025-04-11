package com.example.duan1_nhom7.Admin.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.duan1_nhom7.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.*;

public class AdminDasFragment extends Fragment {

    private TextView tvTotalUsers, tvTotalProducts, tvTotalRevenue;
    private Button btnStartDate, btnEndDate, btnFilter;
    private FirebaseFirestore db;
    private Calendar startCal = Calendar.getInstance();
    private Calendar endCal = Calendar.getInstance();
    private BarChart barChart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_das, container, false);

        db = FirebaseFirestore.getInstance();

        tvTotalUsers = view.findViewById(R.id.tvTotalUsers);
        tvTotalProducts = view.findViewById(R.id.tvTotalProducts);
        tvTotalRevenue = view.findViewById(R.id.tvTotalRevenue);
        btnStartDate = view.findViewById(R.id.btnStartDate);
        btnEndDate = view.findViewById(R.id.btnEndDate);
        btnFilter = view.findViewById(R.id.btnFilter);
        barChart = view.findViewById(R.id.barChart);

        loadBasicStats();
        setupDatePickers();

        return view;
    }

    private void loadBasicStats() {
        db.collection("users").get()
                .addOnSuccessListener(snapshot -> tvTotalUsers.setText("Tổng người dùng: " + snapshot.size()))
                .addOnFailureListener(e -> tvTotalUsers.setText("Không tải được người dùng"));

        db.collection("products").get()
                .addOnSuccessListener(snapshot -> tvTotalProducts.setText("Tổng sản phẩm: " + snapshot.size()))
                .addOnFailureListener(e -> tvTotalProducts.setText("Không tải được sản phẩm"));
    }

    private void setupDatePickers() {
        btnStartDate.setOnClickListener(v -> showDatePickerDialog(startCal, btnStartDate));
        btnEndDate.setOnClickListener(v -> showDatePickerDialog(endCal, btnEndDate));

        btnFilter.setOnClickListener(v -> {
            if (startCal.after(endCal)) {
                tvTotalRevenue.setText("Ngày bắt đầu phải trước ngày kết thúc");
                return;
            }
            filterOrdersByDateRange(startCal.getTime(), endCal.getTime());
        });
    }

    private void showDatePickerDialog(Calendar calendar, Button button) {
        new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    button.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void filterOrdersByDateRange(Date startDate, Date endDate) {
        db.collection("orders")
                .whereGreaterThanOrEqualTo("createdAt", new Timestamp(startDate))
                .whereLessThanOrEqualTo("createdAt", new Timestamp(endDate))
                .get()
                .addOnSuccessListener(snapshot -> {
                    double totalRevenue = 0;
                    Map<String, Double> revenuePerDay = new TreeMap<>();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());

                    for (QueryDocumentSnapshot doc : snapshot) {
                        Timestamp ts = doc.getTimestamp("createdAt");
                        if (ts == null) continue;

                        double amount = doc.getDouble("totalAmount") != null ? doc.getDouble("totalAmount") : 0;
                        totalRevenue += amount;

                        String dateKey = sdf.format(ts.toDate());
                        revenuePerDay.put(dateKey, revenuePerDay.getOrDefault(dateKey, 0.0) + amount);
                    }

                    tvTotalRevenue.setText("Tổng doanh thu: " + totalRevenue + " đ");
                    showChart(revenuePerDay);
                })
                .addOnFailureListener(e -> tvTotalRevenue.setText("Lỗi tải đơn hàng"));
    }

    private void showChart(Map<String, Double> revenueData) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Double> entry : revenueData.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue().floatValue()));
            labels.add(entry.getKey());
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu theo ngày");
        dataSet.setColor(getResources().getColor(R.color.purple_700));

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value >= 0 && value < labels.size() ? labels.get((int) value) : "";
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        barChart.getAxisRight().setEnabled(false);

        barChart.invalidate();
    }
}
