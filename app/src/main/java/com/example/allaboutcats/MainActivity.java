package com.example.allaboutcats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    TextView id;
    private ProgressBar spinner;
    Button newimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=findViewById(R.id.imageView);
        newimg=findViewById(R.id.button2);
        spinner = (ProgressBar)findViewById(R.id.progressBar3);
        spinner.setVisibility(View.VISIBLE);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/v1/images/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CATAPI catapi=retrofit.create(CATAPI.class);
        Call<List<Random>> call=catapi.getRandom();
        call.enqueue(new Callback<List<Random>>() {
            @Override
            public void onResponse(Call<List<Random>> call, Response<List<Random>> response) {
                if(!response.isSuccessful()){
                    id.setText("Code: "+response.code());
                    return;
                }
                List<Random> random=response.body();
                Picasso.with(getBaseContext()).load(random.get(0).getUrl()).into(img);
                spinner.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<List<Random>> call, Throwable t) {
                id.setText(t.getMessage());
            }
        });

        newimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl("https://api.thecatapi.com/v1/images/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                CATAPI catapi=retrofit.create(CATAPI.class);
                Call<List<Random>> call=catapi.getRandom();
                call.enqueue(new Callback<List<Random>>() {
                    @Override
                    public void onResponse(Call<List<Random>> call, Response<List<Random>> response) {
                        if(!response.isSuccessful()){
                            id.setText("Code: "+response.code());
                            return;
                        }
                        List<Random> random=response.body();
                        Picasso.with(getBaseContext()).load(random.get(0).getUrl()).into(img);
                        spinner.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<List<Random>> call, Throwable t) {
                        id.setText(t.getMessage());
                    }
                });
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_breeds:
                        startActivity(new Intent(getApplicationContext(), Breeds.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_Category:
                        startActivity(new Intent(getApplicationContext(), Category.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

}