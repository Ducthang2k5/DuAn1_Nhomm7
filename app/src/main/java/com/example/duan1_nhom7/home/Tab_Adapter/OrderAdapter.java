package com.example.duan1_nhom7.home.Tab_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom7.Payment.Model.Order;
import com.example.duan1_nhom7.R;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void setOrderList(List<Order> newList) {
        this.orderList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // In log để kiểm tra toàn bộ dữ liệu đơn hàng
        Log.d("ORDER_DEBUG", "Đang bind dữ liệu cho order: " + order.toString());

        holder.tvOrderId.setText("Mã đơn: " + order.getUserId().substring(0, 28) + "...");

        try {
            holder.tvTotal.setText("Tổng tiền: " + formatCurrency(order.getTotalPayment()));
        } catch (Exception e) {
            holder.tvTotal.setText("Tổng tiền: Đang cập nhật");
            Log.e("ORDER_ERROR", "Lỗi hiển thị tổng tiền cho đơn hàng: " + order.toString(), e);
        }

        holder.tvTime.setText("Thời gian: " + getTimeFromTimestamp(order.getTimestamp()));
        holder.tvStatus.setText("Trạng thái: " + convertStatus(order.getStatus()));
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvTotal, tvTime, tvStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvTime = itemView.findViewById(R.id.tvOrderTime);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }

    private String formatCurrency(int amount) {
        return String.format("%,d₫", amount).replace(",", ".");
    }

    private String convertStatus(String status) {
        switch (status) {
            case "ordered":
                return "Đã đặt";
            case "confirmed":
                return "Đã duyệt";
            case "canceled":
                return "Đã hủy";
            default:
                return "Không rõ";
        }
    }

    private String getTimeFromTimestamp(Timestamp timestamp) {
        if (timestamp == null) return "Không rõ";
        Date date = timestamp.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(date);
    }
}
