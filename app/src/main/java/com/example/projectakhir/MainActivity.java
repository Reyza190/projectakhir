package com.example.projectakhir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectakhir.Database.dao.BeritaDao;
import com.example.projectakhir.Database.dao.UserDao;
import com.example.projectakhir.Database.models.Berita;
import com.example.projectakhir.Database.models.User;
import com.example.projectakhir.adapter.AppAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView user;
    private CircleImageView img_profile;
    private ImageView img_profile_dummy;
    private User users;
    private UserDao userDao = new UserDao();
    private String role, idBerita;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Button logout;
    private Intent intent2;
    private BeritaDao beritaDao = new BeritaDao();
    private Berita berita;
    private ArrayList<Berita> beritas = new ArrayList<>();
    private AppAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getDataBerita();
        setRecycleview();
        logout.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        img_profile_dummy.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getalluser();

    }

    private void setRecycleview() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void init(){
        intent2 = new Intent(getApplicationContext(), ProfileActivity.class);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        logout = findViewById(R.id.logout);
        user = findViewById(R.id.txt_homepage_user);
        img_profile = findViewById(R.id.img_homepage_profile);
        recyclerView = findViewById(R.id.recyclerView_homepage);
        img_profile_dummy = findViewById(R.id.img_homepage_prfile_dummy);
    }

    private void getalluser(){
        userDao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    users = dataSnapshot.getValue(User.class);
                    users.setId(dataSnapshot.getKey());
                    userdata(users.getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void userdata(String id) {
        if (id != null) {
            userDao.getall(id).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        users = task.getResult().getValue(User.class);
                        user.setText("Hi " + users.getUsername());
                        role = users.getRole();
                        if (users.getImage_user() != null) {
                            Glide.with(getApplicationContext()).load(users.getImage_user()).into(img_profile);
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
    }

    private void getDataBerita(){
        beritaDao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                beritas = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    berita = dataSnapshot.getValue(Berita.class);
                    berita.setId(dataSnapshot.getKey());
                    idBerita = dataSnapshot.getKey();
                    beritas.add(berita);

                }
                adapter = new AppAdapter(MainActivity.this, beritas);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void PopupMenu(String role, ImageView imageView){
        if (role.equals("admin")){
            PopupMenu popupMenu = new PopupMenu(this, imageView);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.profile:
                            startActivity(intent2);
                            return true;
                        case R.id.dashboard:
                            startActivity(new Intent(getApplicationContext(), AddArticleActivity.class));
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.inflate(R.menu.menu);
            popupMenu.show();
        }else if (role.equals("user")){
            startActivity(intent2);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_homepage_profile:
                PopupMenu(role, img_profile);
                break;
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.img_homepage_prfile_dummy:
                PopupMenu(role, img_profile_dummy);
                break;
        }
    }
}