package com.example.projectakhir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectakhir.Database.dao.BeritaDao;
import com.example.projectakhir.Database.dao.UserDao;
import com.example.projectakhir.Database.models.Berita;
import com.example.projectakhir.Database.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back, img_article, img_profile_dummy;
    private CircleImageView img_profile;
    private TextView title, content;
    private String id, idBerita;
    private Berita berita;
    private UserDao userDao = new UserDao();
    private BeritaDao beritaDao = new BeritaDao();
    private User user;
    private Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        getDataBerita();
        getProfile();
        back.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        img_profile_dummy.setOnClickListener(this);
    }
    private void init(){
        Intent intent = getIntent();
        intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
        idBerita = intent.getStringExtra("BERITA");
        back = findViewById(R.id.img_details_back);
        img_profile = findViewById(R.id.img_details_profile);
        img_article = findViewById(R.id.img_details_content);
        title = findViewById(R.id.txt_details_title);
        content = findViewById(R.id.txt_details_content);
        img_profile_dummy = findViewById(R.id.img_details_profile_dummy);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_details_back:
                finish();
                break;
            case R.id.img_details_profile:
                startActivity(intent1);
                break;
            case R.id.img_details_profile_dummy:
                startActivity(intent1);
        }
    }

    private void getProfile(){
        userDao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    user = dataSnapshot.getValue(User.class);
                    user.setId(dataSnapshot.getKey());
                    LoadProfile(user.getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadProfile(String id){
        userDao.getall(id).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    user = task.getResult().getValue(User.class);
                    if (user.getImage_user() != null) {
                        Glide.with(DetailActivity.this).load(user.getImage_user()).into(img_profile);
                        img_profile.setVisibility(View.VISIBLE);
                        img_profile_dummy.setVisibility(View.INVISIBLE);
                    }else{
                        img_profile.setVisibility(View.INVISIBLE);
                        img_profile_dummy.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void getDataBerita(){
        beritaDao.getall(idBerita).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    berita = task.getResult().getValue(Berita.class);
                    title.setText(berita.getJudul());
                    Glide.with(DetailActivity.this).load(berita.getImage_berita()).into(img_article);
                    content.setText(berita.getDetail_article());
                }
            }
        });
    }
}