package com.example.duan1_nhom7.Product;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1_nhom7.R;
import com.example.duan1_nhom7.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Activity activity;
    private HomeFragment homeFragment;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private Button btnViewDetails;

    public ProductAdapter(Activity activity, List<Product> productList, HomeFragment homeFragment) {
        this.activity = activity;
        this.productList = productList;
        this.homeFragment = homeFragment;
        this.db = FirebaseFirestore.getInstance();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(product.getPrice() + " VNĐ");

        // Load ảnh sản phẩm từ Firestore
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_default_image)
                .into(holder.imgProduct);

        // Xử lý thêm vào giỏ hàng
        holder.btnViewDetails.setOnClickListener(v -> {
            if (currentUser != null) {
                String userId = currentUser.getUid();

                db.collection("users").document(userId)
                        .collection("cart")
                        .document(product.getId())
                        .set(product)
                        .addOnSuccessListener(aVoid -> Toast.makeText(activity, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(activity, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(activity, "Bạn cần đăng nhập để thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });

        // Gán sự kiện click cho nút "Xem chi tiết"
        holder.btnViewDetails.setOnClickListener(v -> {
            // Sử dụng activity thay vì context
            Intent intent = new Intent(activity, ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());  // Truyền productId qua Intent
            activity.startActivity(intent);  // Dùng activity.startActivity thay vì context.startActivity
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imgProduct;
        Button btnViewDetails;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tvProductName);
            txtPrice = itemView.findViewById(R.id.tvProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetail);
        }
    }
}
