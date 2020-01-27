package io.github.padamchopra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.algod.client.AlgodClient;
import com.algorand.algosdk.algod.client.ApiException;
import com.algorand.algosdk.algod.client.api.AlgodApi;
import com.algorand.algosdk.algod.client.auth.ApiKeyAuth;
import com.algorand.algosdk.algod.client.model.AssetParams;
import com.algorand.algosdk.algod.client.model.NodeStatus;
import com.algorand.algosdk.algod.client.model.TransactionID;
import com.algorand.algosdk.algod.client.model.TransactionParams;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.crypto.Digest;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.util.Encoder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

public class DetailedBill extends AppCompatActivity {

    public FirebaseFirestore db;
    public String transactionId;

    static class ChangingBlockParms {
        public BigInteger fee;
        public BigInteger firstRound;
        public BigInteger lastRound;
        public String genID;
        public Digest genHash;

        public ChangingBlockParms() {
            this.fee = BigInteger.valueOf(0);
            this.firstRound = BigInteger.valueOf(0);
            this.lastRound = BigInteger.valueOf(0);
            this.genID = "";
            this.genHash = null;
        }
    }

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_bill);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final TextView textView1 = findViewById(R.id.transaction_id);
        final TextView textView2 = findViewById(R.id.ordered_items);
        final TextView textView3 = findViewById(R.id.ordered_items_cost);

        db = FirebaseFirestore.getInstance();
        db.collection("payments").whereEqualTo("from", getResources().getString(R.string.consumer_address)).whereEqualTo("status", "unsigned")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                Map<String, Object> details = documentSnapshot.getData();
                transactionId = documentSnapshot.getId();
                textView1.setText("TX: " + transactionId.toUpperCase() + "\n" + details.get("date"));
                Map<String, Float> itemNames = (Map) details.get("item_names");
                String textfortwo = "";
                String textforthree = "";
                for (String key : itemNames.keySet()) {
                    textfortwo += key + "\n1 Item\n\n";
                    textforthree += itemNames.get(key) + " Algos\n\n\n";
                }
                textView2.setText(textfortwo);
                textView3.setText(textforthree);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Could not fetch receipts. Try again!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void payTransaction(View view) {
        db.collection("payments").document(transactionId).update("status", "signed")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Added to the ledger!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Couldn't pay. Try Again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
