package com.mahmoud.orderfirebaseapp;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoud.orderfirebaseapp.ViewHolder.CartAdapter;
import com.mahmoud.orderfirebaseapp.common.Common;
import com.mahmoud.orderfirebaseapp.model.Food;
import com.mahmoud.orderfirebaseapp.model.Order;
import com.mahmoud.orderfirebaseapp.model.Receipt;
import com.squareup.picasso.Request;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;
    float totalPrice;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;


    static List<List<Order>> orderList = new ArrayList<>();
    static List<Food> inventoryList = new ArrayList<>();
    static Food inventory;
    static List<String> requestId  = new ArrayList<>();
    static List<Request> requestList = new ArrayList<>();
    static float total;

    private boolean partial = false;

    static String unavailablefoodnames="";
    static float unavailablefoodprice=0;

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        database = FirebaseDatabase.getInstance();
        requests =  database.getReference("Requests");

        //Init
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkavailability(cart)) {

                    showAlertDialog();

                }else {

                    System.out.println("Food Unavailble");
                    AlertDialog.Builder alertPartialDialog = new AlertDialog.Builder(Cart.this);
                    alertPartialDialog.setTitle(unavailablefoodnames + " is unavailable");
                    unavailablefoodnames="";
                    alertPartialDialog.setMessage("Do you accept partial order ?");

                    alertPartialDialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            partial = true;
                            showAlertDialog();
                        }
                    });

                    alertPartialDialog.show();

                }
            }
        });

        loadListFood();

    }

    private boolean checkavailability(List<Order> cart){
        boolean partial = false;
        if(cart.size()==0){
            partial = true;
        }
        for(Order order : cart){
            for(Food food: inventoryList){

            }
        }
        return partial;
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your address");
        System.out.println("email address ");
        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if(partial) {


                    partial = false;

                    Locale locale = new Locale("en","US");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                    unavailablefoodprice = 0;



                }else {
                }


                requestId.add(String.valueOf(System.currentTimeMillis()));



                Toast.makeText(Cart.this, "Thank you, Order placed", Toast.LENGTH_SHORT).show();
                finish();

                Intent intent = new Intent();
                PendingIntent pendingIntent = PendingIntent.getActivity(Cart.this,0,intent,0);
                Notification.Builder notificationBuilder = new Notification.Builder(Cart.this)
                        .setTicker("Order Placed").setContentTitle("Order Placed")
                        .setContentText("Your order is processing now").setSmallIcon(R.drawable.logo)
                        .setContentIntent(pendingIntent);
                Notification notification = notificationBuilder.build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                assert nm != null;
                nm.notify(0,notification);
            }
        });


        alertDialog.show();


    }

    class KitchenThread implements Runnable{

        @Override
        public void run() {
            while (true) {
                while (requestList.size() > 0) {
                    DatabaseReference requests = FirebaseDatabase.getInstance().getReference("Requests");
                    requests.child(requestId.get(0)).child("status").setValue("1");
                    System.out.println("The chef is working on requests!");

                    Intent intent = new Intent();
                    PendingIntent pendingIntent = PendingIntent.getActivity(Cart.this,0,intent,0);
                    Notification.Builder notificationBuilder = new Notification.Builder(Cart.this)
                            .setTicker("Order Accepted").setContentTitle("Order Accepted")
                            .setContentText("The Chef is working on your order").setSmallIcon(R.drawable.logo)
                            .setContentIntent(pendingIntent);
                    Notification notification = notificationBuilder.build();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    assert nm != null;
                    nm.notify(0,notification);

                    try {
                        //cooking time: 180 sec
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Order Prepared!");

                    Intent intent1 = new Intent();
                    PendingIntent pendingIntent1 = PendingIntent.getActivity(Cart.this,0,intent1,0);
                    Notification.Builder notificationBuilder1 = new Notification.Builder(Cart.this)
                            .setTicker("Order Prepared").setContentTitle("Order Prepared")
                            .setContentText("Your order is prepared").setSmallIcon(R.drawable.logo)
                            .setContentIntent(pendingIntent1);
                    Notification notification1 = notificationBuilder1.build();
                    notification1.flags = Notification.FLAG_AUTO_CANCEL;
                    NotificationManager nm1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    assert nm1 != null;
                    nm1.notify(0,notification1);

                    requests.child(requestId.get(0)).child("status").setValue("2");
                    try {
                        //packaging time: 180 sec
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Order Packaged!");

                    Intent intent2 = new Intent();
                    PendingIntent pendingIntent2 = PendingIntent.getActivity(Cart.this,0,intent2,0);
                    Notification.Builder notificationBuilder2 = new Notification.Builder(Cart.this)
                            .setTicker("Order Packaged").setContentTitle("Order Packaged")
                            .setContentText("Your order is packaged and ready to pick up!").setSmallIcon(R.drawable.logo)
                            .setContentIntent(pendingIntent2);
                    Notification notification2 = notificationBuilder2.build();
                    notification2.flags = Notification.FLAG_AUTO_CANCEL;
                    NotificationManager nm2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    assert nm2 != null;
                    nm2.notify(0,notification2);

                    requests.child(requestId.get(0)).child("status").setValue("3");

                    //The first request finished, generate receipt, then remove the finished request, working on next request in list
                    Looper.prepare();
                    Toast.makeText(Cart.this,GenerateReceipt(requestList.get(0)),Toast.LENGTH_SHORT).show();
                    requestList.remove(0);
                    requestId.remove(0);

                    Looper.loop();

                }
                System.out.println("there are no requests yet, what a terrible day!");
            }
        }

        public String GenerateReceipt(Request request){
            Receipt receipt = new Receipt();

            String message="---------Food Ready! Thank you!---------\n";
            for(Order i:receipt.items){
                message+=i.getProductName()+" :"+i.getQuanlity()+"\n";
            }
            message+="Total: "+total;
            return message;


        }
    }


    private void loadListFood() {
        orderList.add(cart);
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        //Calculate total price
        total = 0;
        for(Order order:cart)
            total+=(float) (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuanlity()));
        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);


        float tax= (float) (total*0.06);
        float profit = (float) (total*0.3);
        total+=tax+profit;

        totalPrice =total;

        txtTotalPrice.setText(fmt.format(total));

    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        System.out.println("I CAME HERE?");
        System.out.println(item.getTitle());
        return super.onContextItemSelected(item);
    }

    private void deleteFoodItem(int ord) {
        String order1 = cart.get(ord).getProductId();
        System.out.println("The order is " +order1);
        for(Order order:cart){
            System.err.println("The order id is " + order.getProductId());
            if(order.getProductId().equals(order1)){
                System.err.println(order.getProductName());
                System.err.println("");
            }
        }
        loadListFood();
    }

}
