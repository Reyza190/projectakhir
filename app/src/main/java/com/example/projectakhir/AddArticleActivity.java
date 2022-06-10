package com.example.projectakhir;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projectakhir.Database.dao.BeritaDao;
import com.example.projectakhir.Database.dao.UserDao;
import com.example.projectakhir.Database.models.Berita;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddArticleActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText tite, content;
    private ImageView img_article, back;
    private Button submit;
    private BeritaDao beritaDao = new BeritaDao();
    private StorageReference storage;
    private Berita berita;
    private Uri uri;
    private String id, imageold;
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);
        init();
        getData();
        img_article.setOnClickListener(this);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private void init(){
        tite = findViewById(R.id.edt_addArticle_title);
        content = findViewById(R.id.edt_addArticle_content);
        submit = findViewById(R.id.submit);
        img_article = findViewById(R.id.img_addArticle);
        back = findViewById(R.id.back);
        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        storage = reference.child("Berita").child("img"+ new Date().getTime() + ".jpg");
    }

    private void UploadImage(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 105);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 105 && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            img_article.setImageURI(uri);
        }
    }

    private void UploadToFirebase(Uri uri) {
        if (id != null && uri.toString() == imageold) {
            Berita berita = new Berita(uri.toString(), tite.getText().toString(), content.getText().toString());
            beritaDao.update(id, berita);
            Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_SHORT).show();
        }else if (id != null && uri.toString() != imageold){
            storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Berita berita = new Berita(uri.toString(), tite.getText().toString(), content.getText().toString());
                            beritaDao.update(id, berita);
                            Toast.makeText(getApplicationContext(), "Update Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Berita berita = new Berita(uri.toString(), tite.getText().toString(), content.getText().toString());
                            beritaDao.insert(berita);
                            Toast.makeText(getApplicationContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_addArticle:
                UploadImage();
                break;
            case R.id.submit:
                UploadToFirebase(uri);
                break;
            case R.id.back:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
    }

    private void getData(){
        if (id != null) {
            beritaDao.getall(id).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        berita = task.getResult().getValue(Berita.class);
                        imageold = berita.getImage_berita();
                        uri = Uri.parse(berita.getImage_berita());
                        Glide.with(AddArticleActivity.this).load(berita.getImage_berita()).into(img_article);
                        tite.setText(berita.getJudul());
                        content.setText(berita.getDetail_article());
                    }
                }
            });
        }
    }
}
