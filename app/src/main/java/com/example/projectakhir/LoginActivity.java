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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText Email, Password;
    private Button Login;
    private TextView Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
    }
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        UpdateUI(firebaseUser);
    }
    private void init(){
        mAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.login);
        Register = findViewById(R.id.register);
    }

    private void UpdateUI(FirebaseUser user){
        if (user != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else{

        }
    }

    private void LogIn(String email, String password){
        if (!validateForm()){
            return;
        }else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                        UpdateUI(user);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private boolean validateForm() {
        boolean result = true;
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
            case R.id.login:
                LogIn(Email.getText().toString().trim(), Password.getText().toString().trim());
                break;
            case R.id.register:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
        }
    }
}