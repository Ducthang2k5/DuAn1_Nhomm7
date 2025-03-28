package com.example.duan1_nhom7.Welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1_nhom7.Admin.AdminLoginActivity;
import com.example.duan1_nhom7.R;
import com.example.duan1_nhom7.User.UserLoginActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        Button btnUserLogin = findViewById(R.id.btnUserLogin);
        Button btnAdminLogin = findViewById(R.id.btnAdminLogin);

        // Khi bấm Đăng nhập User, chuyển sang màn hình User Login
        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });

        // Khi bấm Đăng nhập Admin, chuyển sang màn hình Admin Login
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });
    }



}
