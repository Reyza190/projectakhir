package com.example.projectakhir.Database.dao;

import com.example.projectakhir.Database.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class UserDao {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public UserDao(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    public Task<Void> insert(User user){
        return databaseReference.child(mAuth.getUid()).push().setValue(user);
    }

    public Task<Void> update(String id,User user){
        return databaseReference.child(mAuth.getUid()).child(id).setValue(user);
    }

    public Task<Void> delete(String id){
        return databaseReference.child(mAuth.getUid()).child(id).removeValue();
    }

    public Query get (){
        return databaseReference.child(mAuth.getUid()).orderByKey();
    }

    public Task<DataSnapshot> getall(String id){
        return databaseReference.child(mAuth.getUid()).child(id).get();
    }
}
