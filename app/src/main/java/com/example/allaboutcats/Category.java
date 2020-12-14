package com.example.allaboutcats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Category extends AppCompatActivity {
    ArrayList<String> categoryname = new ArrayList<>();
    HashMap<String, Integer> categorymap;
    String catnamecurrent;
    int catidcurrent;
    Button search;
    Spinner catnamespinner;
    ImageView demo;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_Category);
        search=findViewById(R.id.search2);
        categorymap = new HashMap<>();
        demo=findViewById(R.id.imageView2);
        spinner = (ProgressBar)findViewById(R.id.progressBar2);
        spinner.setVisibility(View.VISIBLE);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_breeds:
                        startActivity(new Intent(getApplicationContext(), Breeds.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_Category:
                        return true;
                }
                return false;
            }
        });

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CATAPI catapi=retrofit.create(CATAPI.class);
        Call<List<categorylist>> call=catapi.getcategorylist();
        call.enqueue(new Callback<List<categorylist>>() {
            @Override
            public void onResponse(Call<List<categorylist>> call, Response<List<categorylist>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Category.this,"Error loading Category list",Toast.LENGTH_SHORT).show();
                    return;
                }
                List<categorylist> categorylist=response.body();
                categoryname.add("Select category");
                for(categorylist cat:categorylist){
                    categoryname.add(cat.name);
                    categorymap.put(cat.name,cat.id);
                    showlistinspinner();
                }

            }

            @Override
            public void onFailure(Call<List<categorylist>> call, Throwable t) {

            }

        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                Retrofit retrofit2=new Retrofit.Builder()
                        .baseUrl("https://api.thecatapi.com/v1/images/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                CATAPI catimgapi=retrofit2.create(CATAPI.class);
                catidcurrent=categorymap.get(catnamecurrent);
                Call<List<catclass>> call2=catimgapi.getCategory(catidcurrent);
                call2.enqueue(new Callback<List<catclass>>() {
                    @Override
                    public void onResponse(Call<List<catclass>> call, Response<List<catclass>> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(Category.this,response.code(),Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<catclass> catcurrent=response.body();
                        Picasso.with(getBaseContext()).load(catcurrent.get(0).getUrl()).into(demo);
                        spinner.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onFailure(Call<List<catclass>> call, Throwable t) {
                        Toast.makeText(Category.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void showlistinspinner() {
        catnamespinner = findViewById(R.id.categorynamespinner);
        ArrayAdapter<String> catnameadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categoryname);
        catnameadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catnamespinner.setAdapter(catnameadapter);
        spinner.setVisibility(View.INVISIBLE);
        catnamespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catnamecurrent= adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Category.this,catnamecurrent,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Category.this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
