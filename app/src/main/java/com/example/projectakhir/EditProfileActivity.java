package com.example.projectakhir;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectakhir.Database.dao.UserDao;
import com.example.projectakhir.Database.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Button submit;
    private ImageView back;
    private EditText username, email;
    private CircleImageView image_profile;
    private ImageView img_add_dummy;
    private UserDao userDao = new UserDao();
    private User user;
    private String id;
    private Uri image_url;
    private String sa, role;
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth mAuth;
    private StorageReference fileRef;
    private TextView change_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
        getData();
        back.setOnClickListener(this);
        image_profile.setOnClickListener(this);
        submit.setOnClickListener(this);
        img_add_dummy.setOnClickListener(this);
        change_photo.setOnClickListener(this);
    }

    private void init(){
        img_add_dummy = findViewById(R.id.img_add_profile);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        image_profile = findViewById(R.id.profile_picture);
        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user1 = mAuth.getCurrentUser();
        fileRef = reference.child("User").child(user1.getEmail()).child("images/");
        change_photo = findViewById(R.id.change_photo);

    }

    private void getData(){
        userDao.getall(id).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    user = task.getResult().getValue(User.class);
                    if (user.getImage_user() != null) {
                        change_photo.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
                        Glide.with(getApplicationContext()).load(user.getImage_user()).into(image_profile);
                        image_profile.setVisibility(View.VISIBLE);
                        img_add_dummy.setVisibility(View.INVISIBLE);
                    }else{
                        img_add_dummy.setVisibility(View.VISIBLE);
                        image_profile.setVisibility(View.INVISIBLE);
                    }
                    sa = user.getImage_user();
                    email.setText(user.getEmail());
                    username.setText(user.getUsername());
                    role = user.getRole();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.profile_picture:
                getImage();
                break;
            case R.id.submit:
                Upload();
                break;
            case R.id.img_add_profile:
                getImage();
                break;
            case R.id.change_photo:
                getImage();
                break;
        }
    }

    private void getImage(){
        final String[] items = {"Take Photo", "Choose from Library", "Delete", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Upload Image");
        builder.setIcon(R.drawable.ic_baseline_list_24);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Take Photo")) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 202);
                } else if (items[i].equals("Choose from Library")) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, 101);
                } else if (items[i].equals("Delete")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                    builder.setTitle("Delete");
                    builder.setMessage("Anda yakin delete gambar ini ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userDao.deletePhoto(id).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Delete Berhasil", Toast.LENGTH_SHORT).show();
                                    img_add_dummy.setVisibility(View.VISIBLE);
                                    image_profile.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "Delete dibatalkan", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    });builder.show();
                } else if (items[i].equals("Cancel")) {
                dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null){
            image_url = data.getData();
            image_profile.setImageURI(image_url);
            img_add_dummy.setVisibility(View.INVISIBLE);
            image_profile.setVisibility(View.VISIBLE);
        } else if (requestCode == 202 && resultCode == RESULT_OK && data != null){
            final Bundle bundle = data.getExtras();
            Thread thread = new Thread(() -> {
                Bitmap bitmap = (Bitmap) bundle.get("data");
                image_profile.post(() ->{
                    image_profile.setImageBitmap(bitmap);
                });
                image_url = getImageUri(getApplicationContext(), bitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img_add_dummy.setVisibility(View.INVISIBLE);
                        image_profile.setVisibility(View.VISIBLE);
                    }
                });
            });
            thread.start();
        }
    }

    private void Upload(){
        if (image_url == null){
            User user1 = new User();
            user1.setImage_user(sa);
            user1.setRole(role);
            user1.setEmail(email.getText().toString());
            user1.setUsername(username.getText().toString());
            userDao.update(id, user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), "Update succesfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
                UploadToFirebase(image_url, username.getText().toString(), email.getText().toString(), id);
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void UploadToFirebase(Uri uri, String username, String email, String id) {
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        User user = new User(username, email);
                        user.setImage_user(uri.toString());
                        user.setRole(role);
                        userDao.update(id, user);
                        Toast.makeText(getApplicationContext(), "Uploaded succesfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}