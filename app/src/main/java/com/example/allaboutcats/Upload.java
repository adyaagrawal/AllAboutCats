package com.example.allaboutcats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class Upload extends AppCompatActivity {
    private static final int PERMISSION_CODE =1000 ;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PERMISSION_CODE2 = 100 ;
    private static final int IMAGE_PICK_CODE = 101;
    Button capture;
    Button upload;
    ImageView image;
    Uri image_uri;
    Uri gallery_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_Upload);

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
                        startActivity(new Intent(getApplicationContext(), Category.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_Upload:
                        return true;
                }
                return false;
            }
        });
        image=findViewById(R.id.imageView3);
        capture=findViewById(R.id.capture);
        upload=findViewById(R.id.upload);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    //check permissions
                    if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permission1={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //allow popop
                        requestPermissions(permission1,PERMISSION_CODE);
                    }
                    else{
                        //permisssion already given
                        openCamera();
                    }
                }
                else{
                    openCamera();
                }

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permissions
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permission2={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //allow popop
                        requestPermissions(permission2,PERMISSION_CODE2);
                    }
                    else{
                        //permisssion already given
                        pickImagefromgallery();
                    }
                }
                else{
                    pickImagefromgallery();
                }

            }
        });
    }

    private void pickImagefromgallery() {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Select picture"),IMAGE_PICK_CODE);

    }

    private void openCamera() {
        ContentValues values= new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else{
                    Toast.makeText(this,"Permission denied..",Toast.LENGTH_LONG).show();
                }
            }
            case PERMISSION_CODE2:{
                if (grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    pickImagefromgallery();
                }
                else{
                    Toast.makeText(this,"Permission denied..",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //set image in image view
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch(requestCode){
                case IMAGE_CAPTURE_CODE:
                    image.setImageURI(image_uri);
                    break;
                case IMAGE_PICK_CODE:
                    gallery_uri=data.getData();
                    try
                    {
                        Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),gallery_uri);
                        image.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }

}


