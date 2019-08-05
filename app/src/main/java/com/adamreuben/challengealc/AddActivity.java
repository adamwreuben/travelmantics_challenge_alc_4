package com.adamreuben.challengealc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private EditText hotelName,hotelInfo,hotelPrice;
    private ImageView imageView;
    private Button selectBtn;
    private String hotelNameStr,info,price;
    private Uri filePath;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initFirestore();

        hotelName = findViewById(R.id.hotelNameEdit);
        hotelInfo = findViewById(R.id.hotelInfoEdit);
        hotelPrice = findViewById(R.id.hotelPriceEdit);
        imageView = findViewById(R.id.hotelImages);
        selectBtn = findViewById(R.id.selectBtn);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save){

            upload();

        }
        return true;
    }

    private void upload() {

        hotelNameStr = hotelName.getText().toString().trim();
        info = hotelInfo.getText().toString().trim();
        price = hotelPrice.getText().toString().trim();


        mStorage = FirebaseStorage.getInstance().getReference();
        final ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setMessage("Uploading ...");
        mProgress.show();
        final StorageReference filepath = mStorage.child("HotelPics").child(UUID.randomUUID().toString()); //Create Firebase Reference;
        filepath.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {

                    mProgress.dismiss();

                        Map<String,String> map = new HashMap<>();
                        map.put("hotelName",hotelNameStr);
                        map.put("hotelInfo",info);
                        map.put("hotelPrice",price);
                        map.put("hotelImage",downloadUri.toString());

                        CollectionReference collectionReference = mFirestore.collection("Hotel");
                        collectionReference.add(map);


                    }
                });
            }
        });



    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

}
