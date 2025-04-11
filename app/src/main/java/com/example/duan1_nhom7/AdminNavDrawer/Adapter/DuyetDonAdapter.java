package com.example.duan1_nhom7.AdminNavDrawer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom7.Payment.Model.Order;
import com.example.duan1_nhom7.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DuyetDonAdapter extends RecyclerView.Adapter<DuyetDonAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onAction(Order order, String action);
    }

    public DuyetDonAdapter(List<Order> orderList, Context context, OnOrderActionListener listener) {
        this.orderList = orderList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_duyet_don, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txtName.setText(order.getName());
        holder.txtPhone.setText(order.getPhone());
        holder.txtTotal.setText("Tổng tiền: " + order.getTotalPayment() + "đ");
        holder.txtTime.setText("Thời gian: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(order.getTimestamp().toDate()));

        holder.btnApprove.setOnClickListener(v -> listener.onAction(order, "approve"));
        holder.btnCancel.setOnClickListener(v -> listener.onAction(order, "cancel"));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone, txtTotal, txtTime;
        Button btnApprove, btnCancel;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtTime = itemView.findViewById(R.id.txtTime);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
