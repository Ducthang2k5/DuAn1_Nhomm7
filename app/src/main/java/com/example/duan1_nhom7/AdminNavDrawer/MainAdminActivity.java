package com.example.duan1_nhom7.AdminNavDrawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.duan1_nhom7.AdminNavDrawer.Fragments.AdminProfileFragment;
import com.example.duan1_nhom7.AdminNavDrawer.Fragments.DuyetDonFragment;
import com.example.duan1_nhom7.AdminNavDrawer.Fragments.QuanLySanPhamFragment;
import com.example.duan1_nhom7.AdminNavDrawer.Fragments.QuanLyTaiKhoanFragment;
import com.example.duan1_nhom7.AdminNavDrawer.Fragments.ThongKeDonHangFragment;
import com.example.duan1_nhom7.R;
import com.example.duan1_nhom7.Welcome.WelcomeActivity;
import com.google.android.material.navigation.NavigationView;

public class MainAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(findViewById(R.id.toolbar));

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, findViewById(R.id.toolbar),
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Load màn hình đầu tiên - Duyệt đơn
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DuyetDonFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_duyet_don);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_duyet_don) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DuyetDonFragment())
                    .commit();
        } else if (id == R.id.nav_quan_ly_tai_khoan) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new QuanLyTaiKhoanFragment())
                    .commit();
        } else if (id == R.id.nav_thong_ke) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ThongKeDonHangFragment())
                    .commit();
        } else if (id == R.id.nav_quan_ly_san_pham) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new QuanLySanPhamFragment())
                    .commit();
        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AdminProfileFragment())
                    .commit();
        } else if (id == R.id.nav_dang_xuat) {
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
