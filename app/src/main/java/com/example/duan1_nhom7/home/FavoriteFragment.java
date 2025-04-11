package com.example.duan1_nhom7.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom7.Product.Product;
import com.example.duan1_nhom7.Product.ProductAdapter;
import com.example.duan1_nhom7.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerFavorite;
    private ProductAdapter adapter;
    private List<Product> favoriteList;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public FavoriteFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerFavorite = view.findViewById(R.id.recyclerFavorite);
        recyclerFavorite.setLayoutManager(new GridLayoutManager(getContext(), 2));

        favoriteList = new ArrayList<>();
        adapter = new ProductAdapter(getActivity(), favoriteList, null); // Không cần homeFragment ở đây
        recyclerFavorite.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        if (currentUser == null) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        db.collection("users")
                .document(userId)
                .collection("favorites")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    favoriteList.clear();
                    if (querySnapshot != null) {
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            Product product = doc.toObject(Product.class);
                            if (product != null) {
                                favoriteList.add(product);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
