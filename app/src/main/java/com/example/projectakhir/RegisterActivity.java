package com.example.projectakhir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectakhir.Database.dao.UserDao;
import com.example.projectakhir.Database.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText Username, Email, Password;
    private Button Register;
    private TextView Login;
    private FirebaseAuth mAuth;
    private UserDao userDao = new UserDao();
    private User user1;
    private ArrayList<User> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        Register.setOnClickListener(this);
        Login.setOnClickListener(this);
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        Username = findViewById(R.id.username);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        Register = findViewById(R.id.register);
        Login = findViewById(R.id.login);
    }
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        UpdateUI(firebaseUser);


    }
    private void UpdateUI(FirebaseUser user){
        if (user != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("username", Username.getText().toString());
            startActivity(intent);
        }else{
        }
    }
    private void register(String Email, String Password, String username){
        if (!validateForm()){
            return;
        }else{
            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(), "Account success Created", Toast.LENGTH_SHORT).show();
                        user1 = new User(username, Email);
                        user1.setRole("user");
                        userDao.insert(user1);
                        UpdateUI(user);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Account fail created", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(Username.getText().toString())){
            Username.setError("Required");
            result = false;
        }else{
            Username.setError(null);
        }
        if (TextUtils.isEmpty(Email.getText().toString())) {
            Email.setError("Required");
            result = false;
        } else {
            Email.setError(null);
        }
        if (TextUtils.isEmpty(Password.getText().toString())) {
            Password.setError("Required");
            result = false;
        } else {
            Password.setError(null);
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                register(Email.getText().toString().trim(), Password.getText().toString().trim(), Username.getText().toString());
                break;
            case R.id.login:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
    }
}