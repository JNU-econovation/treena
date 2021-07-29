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
    String[] imageUrlList = {"https://postfiles.pstatic.net/MjAyMTA3MjlfNTUg/MDAxNjI3NTI2MjkyNTk3.nLUJEO5Lc0CCNAhAe7b3YQxGKubIRwCh_eIXVt6vFzYg.ap32hwgoeLQrRyHUVImsc8ero3oIUTuwrnEr1035D0Ag.JPEG.hahahafb/wood3.jpg?type=w966", "https://postfiles.pstatic.net/MjAyMTA3MjlfNTUg/MDAxNjI3NTI2MjkyNTk3.nLUJEO5Lc0CCNAhAe7b3YQxGKubIRwCh_eIXVt6vFzYg.ap32hwgoeLQrRyHUVImsc8ero3oIUTuwrnEr1035D0Ag.JPEG.hahahafb/wood3.jpg?type=w966", "https://postfiles.pstatic.net/MjAyMTA3MjlfNTUg/MDAxNjI3NTI2MjkyNTk3.nLUJEO5Lc0CCNAhAe7b3YQxGKubIRwCh_eIXVt6vFzYg.ap32hwgoeLQrRyHUVImsc8ero3oIUTuwrnEr1035D0Ag.JPEG.hahahafb/wood3.jpg?type=w966", "https://thumb.ac-illust.com/t/91/910e0bfe6530d8e87ce1fbff5a8686c6_t.jpeg", "https://thumb.ac-illust.com/t/91/910e0bfe6530d8e87ce1fbff5a8686c6_t.jpeg", "https://thumb.ac-illust.com/t/91/910e0bfe6530d8e87ce1fbff5a8686c6_t.jpeg", "https://thumb.ac-illust.com/t/91/910e0bfe6530d8e87ce1fbff5a8686c6_t.jpeg", "https://thumb.ac-illust.com/t/91/910e0bfe6530d8e87ce1fbff5a8686c6_t.jpeg", "https://thumb.ac-illust.com/t/91/910e0bfe6530d8e87ce1fbff5a8686c6_t.jpeg"};

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