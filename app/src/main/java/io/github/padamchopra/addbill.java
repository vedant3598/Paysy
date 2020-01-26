package io.github.padamchopra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algorand.algosdk.kmd.client.model.SignTransactionRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class addbill extends AppCompatActivity {

    public TextView items;
    public LinearLayout cartView;
    public TextView algos;
    public int items_int;
    public Float algos_int;
    public Map<String, Float> item_names;
    public int transactionID;
    public FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbill);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //initialise variables
        item_names = new HashMap<>();
        cartView = findViewById(R.id.bottom_cart);
        items = findViewById(R.id.item_cart);
        algos = findViewById(R.id.algos_cart);
        items_int = 0;
        algos_int = 0.0f;

        db = FirebaseFirestore.getInstance();
    }

    public void createEnvelope(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("Paysy", Context.MODE_PRIVATE);
        transactionID = sharedPreferences.getInt("id",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id",sharedPreferences.getInt("id",0)+1);
        editor.apply();
        Map<String, Object> billDetails = new HashMap<>();
        billDetails.put("to",getResources().getString(R.string.restarurant_address));
        billDetails.put("from",getResources().getString(R.string.consumer_address));
        billDetails.put("date",new Date());
        billDetails.put("status","unsigned");
        billDetails.put("items",items_int);
        billDetails.put("algos",algos_int);
        billDetails.put("item_names",item_names);
        db.collection("payments").add(billDetails)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Bill sent to customer!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Could not share bill. Try again!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void toggleNachos(View view){
        toggleItem("Loaded Nachos",4.5f,R.id.add_button_nachos);
    }

    public void toggleBurrito(View view){
        toggleItem("Chicken Burrito",9.0f,R.id.add_button_burritos);
    }

    public void toggleFajita(View view){
        toggleItem("Veggie Fajita",12.0f,R.id.add_button_fajita);
    }

    public void toggleGuacamole(View view){
        toggleItem("Guacamole",2.0f,R.id.add_button_guacamole);
    }

    public void toggleDrink(View view){
        toggleItem("Soft Drink",3.0f,R.id.add_button_drink);
    }

    public void toggleItem(String name, Float price, int buttonId){
        MaterialButton button = findViewById(buttonId);
        if(item_names.containsKey(name)){
            button.setText("Add");
            updateItems(-1, name, price*(-1));
            button.setTextColor(getResources().getColor(R.color.green));
            button.setStrokeColorResource(R.color.green);
        }else{
            updateItems(1,name,price);
            button.setText("Remove");
            button.setTextColor(getResources().getColor(R.color.colorAccent));
            button.setStrokeColorResource(R.color.colorAccent);
        }
    }

    public void updateItems(int num, String name, Float price){
        items_int = items_int + num;
        algos_int = algos_int + price;
        if(items_int>0){
            cartView.setVisibility(View.VISIBLE);
        }else{
            cartView.setVisibility(View.GONE);
        }
        if(num>0){
            item_names.put(name, price);
        }else{
            item_names.remove(name);
        }
        items.setText(items_int + " Items");
        algos.setText(algos_int + " Algos");
    }

    public void payments(View view){
        startActivity(new Intent(addbill.this, MainActivity.class));
    }

    public void profile(View view){
        startActivity(new Intent(addbill.this, profile.class));
    }
}
