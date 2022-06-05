package com.example.projectakhir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectakhir.Database.dao.UserDao;
import com.example.projectakhir.Database.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private Button edit_btn;
    private ImageView back;
    private TextView username, email;
    private CircleImageView image_profile;
    private ImageView img_profile;
    private UserDao userDao = new UserDao();
    private User user;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        getProfile();
        edit_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
            intent.putExtra("ID", id);
            startActivity(intent);
        });
        back.setOnClickListener(view -> {
            finish();
        });
    }

    private void init(){
        img_profile = findViewById(R.id.img_profile);
        edit_btn = findViewById(R.id.edit_profile);
        back = findViewById(R.id.back);
        username = findViewById(R.id.username_data);
        email = findViewById(R.id.email_data);
        image_profile = findViewById(R.id.profile_picture);
    }
    private void getProfile(){
        userDao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    user = dataSnapshot.getValue(User.class);
                    user.setId(dataSnapshot.getKey());
                    id = user.getId();
                    getdata(user.getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getdata(String id){
        userDao.getall(id).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    user = task.getResult().getValue(User.class);
                    if (user.getImage_user() != null) {
                        Glide.with(getApplicationContext()).load(user.getImage_user()).into(image_profile);
                        image_profile.setVisibility(View.VISIBLE);
                        img_profile.setVisibility(View.INVISIBLE);
                    }else{
                        image_profile.setVisibility(View.INVISIBLE);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("image", user.getImage_user());
                    username.setText(user.getUsername());
                    email.setText(user.getEmail());
                }
            }
        });
    }
}