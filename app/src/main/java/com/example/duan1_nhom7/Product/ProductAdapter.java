package com.example.duan1_nhom7.Product;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1_nhom7.R;
import com.example.duan1_nhom7.home.HomeFragment;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Activity activity;
    private HomeFragment homeFragment;

    public ProductAdapter(Activity activity, List<Product> productList, HomeFragment homeFragment) {
        this.activity = activity;
        this.productList = productList;
        this.homeFragment = homeFragment;
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

        // Load ảnh sản phẩm từ URL hoặc URI đã chọn
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_default_image) // Ảnh mặc định khi tải ảnh
                .into(holder.imgProduct);

        // Xử lý khi nhấn vào ảnh để chọn ảnh mới
        holder.imgProduct.setOnClickListener(v -> homeFragment.setSelectedView(v));
    }

    // Cập nhật ảnh của sản phẩm khi người dùng chọn ảnh mới
    public void updateProductImage(int position, String imageUrl) {
        productList.get(position).setImageUrl(imageUrl);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}
