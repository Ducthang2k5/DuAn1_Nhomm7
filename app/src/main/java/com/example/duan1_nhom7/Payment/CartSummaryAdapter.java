package com.example.duan1_nhom7.Payment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1_nhom7.Cart.Model.CartItem;
import com.example.duan1_nhom7.R;

import java.util.List;

public class CartSummaryAdapter extends RecyclerView.Adapter<CartSummaryAdapter.ViewHolder> {

    private final List<CartItem> cartItemList;

    public CartSummaryAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartSummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartSummaryAdapter.ViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        holder.tvName.setText(item.getProductName());
        holder.tvQuantity.setText("x" + item.getProductQuantity());
        holder.tvSize.setText("Size: " + item.getProductSize());
        holder.tvSugar.setText("Đường: " + item.getProductSugar());
        holder.tvPrice.setText(item.getProductPrice());

        Glide.with(holder.itemView.getContext())
                .load(item.getProductImage())
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvQuantity, tvSize, tvSugar, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvSugar = itemView.findViewById(R.id.tvSugar);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}

