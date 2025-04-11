package com.example.duan1_nhom7.Admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.duan1_nhom7.R;
import com.example.duan1_nhom7.Admin.fragments.AdminConfirmProductFragment;
import com.example.duan1_nhom7.Admin.fragments.AdminDasFragment;
import com.example.duan1_nhom7.Admin.fragments.AdminUserEditFragment;

public class AdminActivity extends AppCompatActivity {

    Toolbar adminToolbar;
    String userId = "user123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        adminToolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(adminToolbar);

        adminToolbar.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.menu_admin, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.menu_statistics:
                        selectedFragment = new AdminDasFragment();
                        break;
                    case R.id.menu_confirm:
                        selectedFragment = new AdminConfirmProductFragment();
                        break;
                    case R.id.menu_edit_user:
                        selectedFragment = AdminUserEditFragment.newInstance(userId);
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.adminFragmentContainer, selectedFragment)
                            .commit();
                }
                return true;
            });

            popup.show();
        });

        // mặc định load fragment thống kê
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.adminFragmentContainer, new AdminDasFragment())
                .commit();
    }
}
