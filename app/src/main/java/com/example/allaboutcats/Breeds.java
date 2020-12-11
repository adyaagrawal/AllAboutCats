package com.example.allaboutcats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Breeds extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeds);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_breeds);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_breeds:
                        return true;
                    case R.id.nav_Category:
                        startActivity(new Intent(getApplicationContext(), Category.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_Upload:
                        startActivity(new Intent(getApplicationContext(), Upload.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
    }
