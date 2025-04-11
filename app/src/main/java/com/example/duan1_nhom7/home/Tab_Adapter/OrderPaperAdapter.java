package com.example.duan1_nhom7.home.Tab_Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.duan1_nhom7.home.Tab_ProductFragment.CanceledFragment;
import com.example.duan1_nhom7.home.Tab_ProductFragment.ConfirmedFragment;
import com.example.duan1_nhom7.home.Tab_ProductFragment.OrderedFragment;

public class OrderPaperAdapter extends FragmentStateAdapter {
    public OrderPaperAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OrderedFragment();
            case 1:
                return new ConfirmedFragment();
            case 2:
                return new CanceledFragment();
            default:
                return new OrderedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Đã đặt, Đã duyệt, Đã hủy
    }

}
