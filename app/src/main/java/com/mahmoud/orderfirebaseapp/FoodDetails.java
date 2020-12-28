package com.mahmoud.orderfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.orderfirebaseapp.model.Food;
import com.squareup.picasso.Picasso;

public class FoodDetails extends AppCompatActivity {
TextView food_name,food_Price,food_description;
ImageView image_food;
CollapsingToolbarLayout collapsingToolbarLayout;
FloatingActionButton btn_Cart;
String foodId="";
FirebaseDatabase database;
DatabaseReference foods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        food_description = findViewById(R.id.food_description);
        food_name = findViewById(R.id.food_name);
        food_Price = findViewById(R.id.food_price);
        btn_Cart = findViewById(R.id.btn_cart);
        image_food = findViewById(R.id.image_food);


        if(getIntent()!=null){
            foodId=getIntent().getStringExtra("FoodId");
            if (!foodId.isEmpty()){
                getDetailsFood(foodId);
            }
        }

    }

    private void getDetailsFood(String foodId){
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Food food=dataSnapshot.getValue(Food.class);
                Picasso.get().load(food.getImage()).into(image_food);
                food_Price.setText(food.getPrise());
                food_name.setText(food.getName());

//                    food_description.setText("When it comes to writing a menu, word choice is very important. The words you use to describe food on your menu could entice customers and increase sales if you choose them well, but they could also turn c");


             }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}