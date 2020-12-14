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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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

public class Breeds extends AppCompatActivity {
    ArrayList<String> breedname = new ArrayList<>();
    HashMap<String, String> breedmap;
    String breednamecurrent;
    String breedidcurrent;
    Spinner breedspinner;
    Button search;
    TextView des;
    ImageView breedimg;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breeds);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_breeds);
        breedmap=new HashMap<>();
        search=findViewById(R.id.search2);
        des=findViewById(R.id.textView5);
        breedimg=findViewById(R.id.imageView5);
        spinner = (ProgressBar)findViewById(R.id.progressBar3);
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
                        return true;
                    case R.id.nav_Category:
                        startActivity(new Intent(getApplicationContext(), Category.class));
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
        Call<List<breedclass>> call=catapi.getbreedlist();
        call.enqueue(new Callback<List<breedclass>>() {
            @Override
            public void onResponse(Call<List<breedclass>> call, Response<List<breedclass>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Breeds.this,"Error loading Breed list",Toast.LENGTH_SHORT).show();
                    return;
                }
                List<breedclass> breedlist=response.body();
                breedname.add("Select breed");
                for(breedclass breed:breedlist){
                    breedname.add(breed.name);
                    breedmap.put(breed.name,breed.id);
                    showlistinspinner();
                }

            }

            @Override
            public void onFailure(Call<List<breedclass>> call, Throwable t) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl("https://api.thecatapi.com/v1/images/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                CATAPI catapi=retrofit.create(CATAPI.class);
                breedidcurrent=breedmap.get(breednamecurrent);
                Call<List<breedinfoclass>> call=catapi.getBreed(breedidcurrent);
                call.enqueue(new Callback<List<breedinfoclass>>() {

                    @Override
                    public void onResponse(Call<List<breedinfoclass>> call, Response<List<breedinfoclass>> response) {
                        if(!response.isSuccessful()) {
                            Toast.makeText(Breeds.this, "Error loading Breed information", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<breedinfoclass> breed=response.body();
                        for(breedinfoclass b:breed){
                            List<breedclass> b2 = b.getBreeds();
                            for(breedclass b3:b2)
                            des.setText(b3.name+"\n"+"Origin: "+b3.origin+"\n\n"+"Temperament: "+b3.temperament+"\n\n"+
                                    "Life span: "+b3.life_span+"\n"+"Adaptability: "+b3.adaptability+"\n\n"+
                                    "Description: "+b3.description);
                            Picasso.with(getBaseContext()).load(breed.get(0).getUrl()).into(breedimg);
                            spinner.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onFailure(Call<List<breedinfoclass>> call, Throwable t) {

                    }
                });
        }
    });

    }

    private void showlistinspinner() {
        breedspinner = findViewById(R.id.breedsnamespinner);
        ArrayAdapter<String> breedsnameadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, breedname);
        breedsnameadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breedspinner.setAdapter(breedsnameadapter);
        spinner.setVisibility(View.INVISIBLE);
        breedspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                breednamecurrent= adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Breeds.this,breednamecurrent,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Breeds.this,"Nothing selected",Toast.LENGTH_SHORT).show();
            }
        });
    }
    }
