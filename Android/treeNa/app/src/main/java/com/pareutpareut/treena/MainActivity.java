package com.pareutpareut.treena;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    long diaryNumber;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String uid;
    String imageUrl;
    String[] imageUrlList = {"https://postfiles.pstatic.net/MjAyMTA3MjlfMjA3/MDAxNjI3NTY4NjE3Mjcx.DAnQNog3Cyr18qAv4ke17sWV0Ye3R1bBpiyilMO7J0Qg.dVECs9WmdPKnlH-8DODBy-aqXcA5wBPoVGSHeXcVMyMg.JPEG.hahahafb/wood1.jpg?type=w966",
   "https://postfiles.pstatic.net/MjAyMTA3MjlfMTg0/MDAxNjI3NTY4NjE3MzYw.LBABIbXm8OOg_7IkyTfFWN09L5MKulEW8sQe37ivtHQg.-eRZWUupFMLdMiXeGOA5JxB682YG6OSXvmIiufHMPxIg.JPEG.hahahafb/wood2.jpg?type=w966",
            "https://postfiles.pstatic.net/MjAyMTA3MjlfMjEx/MDAxNjI3NTcwMjQwMTky.7FTr3ZXlKzqjDNJFpIozdB0tqhyWhHFPrIg-TxeqsQgg.zzg8zUuKkdcbKct-OYveJOW66nslK5kVqCMEmeiD9JEg.JPEG.hahahafb/wood3.jpg?type=w966",
    "https://postfiles.pstatic.net/MjAyMTA3MjlfMjE1/MDAxNjI3NTY4NjE3MzM1.zkDUA6_N9VZVeVvBs1Fq_P9Jarbd1t0gn5HNKfU4Yjcg.vBk5MFsLsDkjR-CaW2W4FC3xkV5bLDVcJ1iSs1kfTeMg.JPEG.hahahafb/wood4.jpg?type=w966",
    "https://postfiles.pstatic.net/MjAyMTA3MjlfMjA5/MDAxNjI3NTY4NjE3MzUx.cjKKjFNR1MVkP7jStnxaD5n4TwTzSxZ27m7e4UFaLG8g.3CXyfBGR_oz-chD5Fy9clqgwOBLZ7JOWGDZ6wFtfYO8g.JPEG.hahahafb/wood5.jpg?type=w966",
    "https://postfiles.pstatic.net/MjAyMTA3MjlfNjcg/MDAxNjI3NTY4NjE3MzYw.Pjru3AoxNWqDOIhv6Nou9r2L6nSphVWNEMey3v-5704g.Q431roZ5UeD_2te-GnJ1mWERCiYqXgOnvqk9LhC3fbwg.JPEG.hahahafb/wood6.jpg?type=w966",
    "https://postfiles.pstatic.net/MjAyMTA3MjlfNTEg/MDAxNjI3NTY4NjE3Mzg1.Em290aKXCBpxn1cqGNH3qt7d-aDEBljoZCaXtsNBfhog.HYBkVjxHr3KbbUGeEbY_igJFJNIw7OWaOPJDVvXOYCcg.JPEG.hahahafb/wood7.jpg?type=w966",
   "https://postfiles.pstatic.net/MjAyMTA3MjlfMjAx/MDAxNjI3NTY4NjE3NTkx.7n5dJb1vZXaZ2CMLMjAMRpnB0hfLs0aYYUDJzI3d_aEg.tbvVgbOKnaFw60NgmwBDWlPGh9efxFyMiXfnUsISM_Ig.JPEG.hahahafb/wood8.jpg?type=w966",
    "https://postfiles.pstatic.net/MjAyMTA3MjlfMjUg/MDAxNjI3NTY4NjE3Njk1.diWQ2q_gQza_Ll8twVi-eDMNQ-qj534u8HfeyYEbXhQg.vP9slLlDMUibCxJRTzvsn6mtBIXlKANOiiuxJ8mozp8g.JPEG.hahahafb/wood9.jpg?type=w966",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            diaryNumber = intent.getExtras().getLong("number");
        }

        Log.d("diaryTest", "get " + String.valueOf(diaryNumber));
        Log.d("diaryTest", "intent get");

        Button button = (Button) findViewById(R.id.button_diary);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Diary.class);
                startActivity(intent);
            }
        });

        Button button2 = (Button) findViewById(R.id.button_calendar);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Calendar.class);
                startActivity(intent);
            }
        });

        Button button3 = (Button) findViewById(R.id.button_setting);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
            }
        });

        imageView = findViewById(R.id.imageView_main);

    }

    @Override
    public void onResume() {
        super.onResume();
        imageUrl = "";
        if (diaryNumber <= 2) {
            Log.d("diaryTest", "number" + String.valueOf(diaryNumber));
            Log.d("diaryTest", "다이어리 이미지 적용");
            imageUrl = imageUrlList[0];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
            return;
        }
        if (diaryNumber > 2 && diaryNumber <= 7) {
            Log.d("diaryTest", "다이어리 이미지 적용");
            imageUrl = imageUrlList[1];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
            return;
        }
        if (diaryNumber > 7 && diaryNumber <= 14) {
            Log.d("diaryTest", "다이어리 이미지 적용");
            imageUrl = imageUrlList[2];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
            return;
        }
        if (diaryNumber > 14 && diaryNumber <= 21) {
            Log.d("diaryTest", "다이어리 이미지 적용");
            imageUrl = imageUrlList[3];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
            return;
        }
        if (diaryNumber > 21 && diaryNumber <= 28) {
            Log.d("diaryTest", "다이어리 이미지 적용");
            imageUrl = imageUrlList[4];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
            return;
        }
        if (diaryNumber > 28 && diaryNumber <= 35) {
            Log.d("diaryTest", "다이어리 이미지 적용");
            imageUrl = imageUrlList[5];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
            return;
        }
        if (diaryNumber > 35 && diaryNumber <= 42) {
            Log.d("diaryTest", "다이어리 이미지 적용");
            imageUrl = imageUrlList[6];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
            return;
        }
        if (diaryNumber > 42 && diaryNumber <= 49) {
            Log.d("diaryTest", "다이어리 이미지 적용");
            imageUrl = imageUrlList[7];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
            return;
        }
        if (diaryNumber > 49 && diaryNumber <= 56) {
            Log.d("diaryTest", "다이어리 이미지 적용");
            imageUrl = imageUrlList[8];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
            return;
        }
    }
}