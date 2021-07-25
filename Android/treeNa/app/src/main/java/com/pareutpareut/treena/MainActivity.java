package com.pareutpareut.treena;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        Intent intent = getIntent();
        diaryNumber = intent.getExtras().getInt("number");
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

        String imageUrl = "";

        if (diaryNumber <= 5) {
            Log.d("diaryTest", "다이어리 이미지 적용");
            imageUrl = "https://en.pimg.jp/067/180/868/1/67180868.jpg";
            Glide.with(this).load(imageUrl).into(imageView);
            return;
        }
        if (diaryNumber > 5) {
            Log.d("diaryTest", "다이어리 이미지 적용");
            imageUrl = "https://image.onstove.com/850x0/d3kxs6kpbh59hp.cloudfront.net/community/COMMUNITY/8d39624cccf24f2e8ca0ca9bdc80689d/fa91de2892f647f89999a59714bc906f_1595446556.jpg";
            Glide.with(this).load(imageUrl).into(imageView);
            return;
        }

    }
}