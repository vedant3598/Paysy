package io.github.padamchopra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class profile extends AppCompatActivity {

    public FirebaseFirestore db;
    public TextView orders_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        orders_tv = findViewById(R.id.profile_orders);

        db = FirebaseFirestore.getInstance();
        db.collection("payments").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        orders_tv.setText(String.valueOf(queryDocumentSnapshots.getDocuments().size()));
                    }
                });
    }

    public void addBill(View view){
        startActivity(new Intent(profile.this, addbill.class));
    }

    public void payments(View view){
        startActivity(new Intent(profile.this, MainActivity.class));
    }
}
