package com.example.allaboutcats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Category extends AppCompatActivity {
    ArrayList<String> categoryname = new ArrayList<>();
    ArrayList<Integer> categoryid = new ArrayList<Integer>();
    String catnamecurrent;
    int catidcurrent;
    EditText cat;
    ImageView demo;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_Category);
        search=findViewById(R.id.search1);
        demo=findViewById(R.id.imageView2);
        cat=findViewById(R.id.editTextTextPersonName);

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
                    case R.id.nav_Upload:
                        startActivity(new Intent(getApplicationContext(), Upload.class));
                        overridePendingTransition(0, 0);
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
                for(categorylist cat:categorylist){
                    categoryname.add(cat.name+" Id: "+cat.id);
                }

            }

            @Override
            public void onFailure(Call<List<categorylist>> call, Throwable t) {

            }

        });

        final Spinner catnamespinner = findViewById(R.id.categorynamespinner);
        ArrayAdapter<String> catnameadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categoryname);
        catnameadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catnamespinner.setAdapter(catnameadapter);
        catnamespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                catnamecurrent= adapterView.getItemAtPosition(i).toString();
                catnamespinner.setSelection(i);
                Toast.makeText(Category.this,catnamecurrent,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Category.this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit2=new Retrofit.Builder()
                        .baseUrl("https://api.thecatapi.com/v1/images/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                CATAPI catimgapi=retrofit2.create(CATAPI.class);
                catnamecurrent=cat.getText().toString();
                catidcurrent=Integer.parseInt(catnamecurrent);
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

                    }

                    @Override
                    public void onFailure(Call<List<catclass>> call, Throwable t) {
                        Toast.makeText(Category.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


}
