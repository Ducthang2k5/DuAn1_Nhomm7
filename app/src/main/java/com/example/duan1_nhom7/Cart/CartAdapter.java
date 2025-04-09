package com.example.duan1_nhom7.Cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.duan1_nhom7.Cart.Model.CartItem;
import com.example.duan1_nhom7.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {


    private Context context;
    private List<CartItem> cartItemList;
    private OnCartItemChangeListener listener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public interface OnCartItemChangeListener {
        void onQuantityChanged();
        void onItemDeleted();
    }

    public CartAdapter(Context context,List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;

    }


    public void setOnCartItemChangeListener(OnCartItemChangeListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);

        holder.productName.setText(cartItem.getProductName());
        holder.productPrice.setText(cartItem.getProductPrice() + " VNĐ");
        holder.productSugar.setText("Đường: " + cartItem.getProductSugar());
        holder.productSize.setText("Size: " + cartItem.getProductSize());
        holder.productQuantity.setText("x " + cartItem.getProductQuantity());

        Glide.with(holder.itemView.getContext())
                .load(cartItem.getProductImage())
                .into(holder.productImage);

        // Tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            int qty = cartItem.getProductQuantity() + 1;
            cartItem.setProductQuantity(qty);
            updateItem(cartItem);
            notifyItemChanged(holder.getAdapterPosition());
        });

        // Giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            int qty = cartItem.getProductQuantity();
            if (qty > 1) {
                cartItem.setProductQuantity(qty - 1);
                updateItem(cartItem);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (user != null) {
                            // Tạo documentId đúng chuẩn để xóa
                            String documentId = cartItem.getProductId() + "_" +
                                    cartItem.getProductSize() + "_" +
                                    cartItem.getProductSugar();

                            db.collection("carts")
                                    .document(user.getUid())
                                    .collection("items")
                                    .document(documentId)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        int realPos = holder.getAdapterPosition();
                                        if (realPos != RecyclerView.NO_POSITION) {
                                            cartItemList.remove(realPos);
                                            notifyItemRemoved(realPos);
                                            if (listener != null) listener.onItemDeleted();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(holder.itemView.getContext(),
                                                "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }



    private void updateItem(CartItem cartItem) {
        if (user != null) {
            String documentId = cartItem.getProductId() + "_" +
                    cartItem.getProductSize() + "_" +
                    cartItem.getProductSugar();

            db.collection("carts")
                    .document(user.getUid())
                    .collection("items")
                    .document(documentId)
                    .set(cartItem)
                    .addOnSuccessListener(aVoid -> {
                        if (listener != null) listener.onQuantityChanged();
                    });
        }
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productSize, productSugar, productQuantity;
        ImageView btnIncrease, btnDecrease, btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cartItemImage);
            productName = itemView.findViewById(R.id.cartItemName);
            productPrice = itemView.findViewById(R.id.cartItemPrice);
            productSize = itemView.findViewById(R.id.cartItemSize);
            productSugar = itemView.findViewById(R.id.cartItemSugar);
            productQuantity = itemView.findViewById(R.id.cartItemQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}