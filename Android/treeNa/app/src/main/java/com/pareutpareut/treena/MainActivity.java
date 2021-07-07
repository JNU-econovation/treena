package com.pareutpareut.treena;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<Emotion> mCallEmotionList;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //retrofit
        setRetrofitInit();
        callEmotionList();

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
    }

    private void setRetrofitInit() {
        mRetrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
        mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);
    }

    private void callEmotionList(){
        Emotion emo = new Emotion();
        emo.setContext("행복한 하루");
        mCallEmotionList = mRetrofitAPI.getEmotionList(emo);
        mCallEmotionList.enqueue(mRetrofitCallback);
    }

    private Callback<Emotion> mRetrofitCallback = new Callback<Emotion>() {
        @Override
        public void onResponse(Call<Emotion> call, Response<Emotion> response) {
            Emotion result = response.body();
            Log.d("emotion", result.getAnswer());
        }

        @Override
        public void onFailure(Call<Emotion> call, Throwable t) {
            t.printStackTrace();
            System.out.println("결과 실");
            Log.d("emotion", t.getMessage());
        }
    };

}