package com.example.duan1_nhom7.home;

import android.accounts.Account;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.duan1_nhom7.R;
import com.example.duan1_nhom7.home.HomeFragment;
import com.example.duan1_nhom7.home.ProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Load HomeFragment mặc định
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Thiết lập BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.topNav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_products) {
                selectedFragment = new ProductFragment();
            }else if (item.getItemId() == R.id.nav_cart) {
                selectedFragment = new CartFragment();
            }else if (item.getItemId() == R.id.nav_account) {
                selectedFragment = new AccountFragment();
            }
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
