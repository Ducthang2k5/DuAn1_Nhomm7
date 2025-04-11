package com.example.duan1_nhom7.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan1_nhom7.R;
import com.example.duan1_nhom7.home.Tab_Adapter.OrderPaperAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProductFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private OrderPaperAdapter orderPagerAdapter;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        tabLayout = view.findViewById(R.id.tabLayoutHistory);
        viewPager = view.findViewById(R.id.viewPagerHistory);

        orderPagerAdapter = new OrderPaperAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(orderPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Đã đặt");
                    break;
                case 1:
                    tab.setText("Đã duyệt");
                    break;
                case 2:
                    tab.setText("Đã hủy");
                    break;
            }
        }).attach();

        return view;
    }
}

