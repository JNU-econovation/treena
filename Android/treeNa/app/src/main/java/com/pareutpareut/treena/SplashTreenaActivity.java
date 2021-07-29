package com.pareutpareut.treena;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashTreenaActivity extends AppCompatActivity {
    long diaryNumber;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String uid;
    String emotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_treena);
        imageView = findViewById(R.id.imageView_loading2);
Intent intent =getIntent();
        emotion = intent.getExtras().getString("emotion");
        Glide.with(getApplicationContext()).load("https://postfiles.pstatic.net/MjAyMTA3MzBfMTA5/MDAxNjI3NTczNjQ4NTQ2.03TQpUXLylRdIJlRTmOsgYBllm5VmZyMb94YnxE2JU8g.pd3mBzzOCcvzPR-9vtfvFDYPQDtdQiCswHFMJ7lzjY4g.GIF.hahahafb/treena_loading(gif)2.gif?type=w966").into(imageView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        databaseReference.child("diary").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    diaryNumber = task.getResult().getChildrenCount();
                    Log.d("diaryTest", String.valueOf(diaryNumber));
                }
            }
        });
        Loadingstart();
    }

    private void Loadingstart() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(), ImageTreena.class);
                intent.putExtra("number", diaryNumber);
                intent.putExtra("emotion", emotion);
                Log.d("diaryTest", "intent put : "+String.valueOf(diaryNumber));
                startActivity(intent);
                finish();
            }
        }, 4000);
    }

}