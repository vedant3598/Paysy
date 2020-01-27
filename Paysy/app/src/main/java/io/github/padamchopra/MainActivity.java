package io.github.padamchopra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.algorand.algosdk.algod.client.AlgodClient;
import com.algorand.algosdk.algod.client.ApiException;
import com.algorand.algosdk.algod.client.api.AlgodApi;
import com.algorand.algosdk.algod.client.model.Block;
import com.algorand.algosdk.algod.client.model.NodeStatus;
import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.algod.client.auth.ApiKeyAuth;
import com.algorand.algosdk.crypto.Address;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private GestureDetectorCompat gestureDetectorCompat;
    //private static final String APP_SERVICE_HOST = "app-service-toronto-hackathon-1811157952.us-east-2.elb.amazonaws.com";
    //private static final int APP_SERVICE_PORT = 80;
    //private static final String SUBMIT_UNSIGNED_TRANSACTION_URL = "http://" + APP_SERVICE_HOST + ":" + APP_SERVICE_PORT + "/transaction-envelope/submit-unsigned-transaction";

    public FirebaseFirestore db;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e2.getY() - e1.getY() > 50) {
            //swipe down
            db.collection("payments").whereEqualTo("from", getResources().getString(R.string.consumer_address)).whereEqualTo("status", "unsigned")
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.getDocuments().size()>0){
                        startActivity(new Intent(MainActivity.this, DetailedBill.class));
                    }else{
                        Toast.makeText(getApplicationContext(), "No new payments to make.", Toast.LENGTH_SHORT).show();
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Could not fetch receipts. Try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
            return true;
        } else if (e2.getX() - e1.getX() > 50) {
            //swipe right
            System.out.println("right");
            return true;
        } else if (e1.getX() - e2.getX() > 50) {
            //swipe left
            System.out.println("left");
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.gestureDetectorCompat = new GestureDetectorCompat(this, this);

        db = FirebaseFirestore.getInstance();

        //Algorand client connection starts
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //final String ALGOD_API_ADDR = "https://testnet-algorand.api.purestake.io/ps1";
                    //final String ALGOD_API_TOKEN = "H9IknTatc8DgHA3kOa3m4YyCvQCOZV7xUtWvq640";
                    final String ALGOD_API_ADDR = "http://hackathon.algodev.network:9100/";
                    final String ALGOD_API_TOKEN = "ef920e2e7e002953f4b29a8af720efe8e4ecc75ff102b165e0472834b25832c1";

                    AlgodClient client = new AlgodClient();
                    client.setBasePath(ALGOD_API_ADDR);
                    ApiKeyAuth api_key = (ApiKeyAuth) client.getAuthentication("api_key");
                    api_key.setApiKey(ALGOD_API_TOKEN);

                    AlgodApi algodApiInstance = new AlgodApi(client);
                    try {
                        NodeStatus status = algodApiInstance.getStatus();
                        System.out.println("Algorand network status: " + status);
                        System.out.println(algodApiInstance.getBlock(status.getLastRound()).getProposer());
                    } catch (ApiException e) {
                        System.err.println("Exception when calling algod#getStatus");
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        //Ends the client connection
    }

    public void addBill(View view) {
        startActivity(new Intent(MainActivity.this, addbill.class));
    }

    public void profile(View view) {
        startActivity(new Intent(MainActivity.this, profile.class));
    }
}
