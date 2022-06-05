package com.example.projectakhir.Database.dao;

import com.example.projectakhir.Database.models.Berita;
import com.example.projectakhir.Database.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class BeritaDao {
    private DatabaseReference databaseReference;

    public BeritaDao(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Berita.class.getSimpleName());
    }

    public Task<Void> insert(Berita berita){
        return databaseReference.push().setValue(berita);
    }

    public Task<Void> update(String id,Berita berita){
        return databaseReference.child(id).setValue(berita);
    }

    public Task<Void> delete(String id){
        return databaseReference.child(id).removeValue();
    }

    public Query get (){
        return databaseReference.orderByKey();
    }

    public Task<DataSnapshot> getall(String id){
        return databaseReference.child(id).get();
    }
}
