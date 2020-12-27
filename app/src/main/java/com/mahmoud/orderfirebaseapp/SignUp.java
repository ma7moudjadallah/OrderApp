package com.mahmoud.orderfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.orderfirebaseapp.model.Users;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtphone , edtname , edtpass ;
    Button btnSignUp;
    TextView encrypt ;
    FirebaseDatabase database;
    DatabaseReference tablr_user;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        edtname = findViewById(R.id.edtName);
        edtphone = findViewById(R.id.edtPhone);
        edtpass = findViewById(R.id.edtPass);

        btnSignUp=findViewById(R.id.btnSignUp);

        encrypt = findViewById(R.id.encrypt);

        database = FirebaseDatabase.getInstance();
        tablr_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password= computeMD5Hash(edtpass.getText().toString());
                Log.e("dfdf",edtpass.getText().toString());
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please Wait ... ");
                mDialog.show();

                tablr_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(edtphone.getText().toString()).exists()){
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Phone Number already Registered ", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            mDialog.dismiss();
                            Users users = new Users(edtname.getText().toString(),password);
                            tablr_user.child(edtphone.getText().toString()).setValue(users);
                            Toast.makeText(SignUp.this, "Sign Up Successfully  !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this,SignIn.class));
                            finish();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }


    public String computeMD5Hash(String password) {

        try {

            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }

            return MD5Hash.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return password;
    }
}
