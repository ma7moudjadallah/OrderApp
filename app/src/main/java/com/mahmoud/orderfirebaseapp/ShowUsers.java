package com.mahmoud.orderfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.orderfirebaseapp.model.Users;

import java.util.ArrayList;

public class ShowUsers extends AppCompatActivity {

    ListView listView ;
    FirebaseDatabase database ;
    DatabaseReference reference;
    ArrayList<String> list ;
    ArrayAdapter<String> adapter ;
    Users users ;
    Button delete ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        users = new Users();
        delete = findViewById(R.id.delete_btn);
        listView = findViewById(R.id.listData);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,R.layout.item,R.id.userInfo,list);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren() )
                {
                    users = ds.getValue(Users.class);
                    list.add(users.getName()+ users.getPassword());
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
    }

    private void deleteData() {
        DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference("User");
        dbUser.removeValue();

        Toast.makeText(this, "Users Deleted", Toast.LENGTH_SHORT).show();
    }

}
