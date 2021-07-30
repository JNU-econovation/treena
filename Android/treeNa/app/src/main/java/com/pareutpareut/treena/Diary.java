package com.pareutpareut.treena;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Diary extends AppCompatActivity {

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<Emotion> mCallEmotionList;
    private Gson mGson;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Button button_home = findViewById(R.id.button_home);
        TextView textView = findViewById(R.id.textView);
        EditText editText = findViewById(R.id.editText);
        Button button_save = findViewById(R.id.button_save);
        Button button_temporary_save = findViewById(R.id.button_temporary_save);
        Button button_setting = findViewById(R.id.button_setting);
        final String[] userInfo = new String[2];

        //retrofit
        setRetrofitInit();

        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Diary.this, Setting.class));
            }
        });

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Diary.this, SplashActivity.class);
                startActivity(intent);
            }
        });

        //현재 날짜
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);

        String today = year + "년 " + month + "월 " + day + "일 ";
        String date = year + month + day;
        textView.setText(today);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userReference = databaseReference.child("Users").child(uid);

        //유저 정보 얻어오기
        userReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                userInfo[0] = task.getResult().child("email").getValue(String.class);
                userInfo[1] = task.getResult().child("name").getValue(String.class);
                Log.d("userTest", userInfo[0] + " " + userInfo[1]);
            }
        });

        intent = new Intent();
        //데이터베이스에 일기 저장
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("diary").child(uid).child(date).setValue(editText.getText().toString());
                callEmotionList(editText.getText().toString());
                Toast.makeText(getApplicationContext(), "일기가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //데이터베이스에 일기 일시 저장
        button_temporary_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("diary").child(uid).child(date).setValue(editText.getText().toString());
                Toast.makeText(getApplicationContext(), "일기가 임시 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //데이터베이스에서 일기 읽어오기
        databaseReference.child("diary").child(uid).child(date).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String diary = String.valueOf(task.getResult().getValue());
                    if (!task.getResult().exists()) {
                        editText.setText("");
                    } else {
                        editText.setText(diary);
                    }
                }
            }
        });
    }

    private void setRetrofitInit() {
        mRetrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
        mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);
    }

    private void callEmotionList(String text) {
        Emotion emo = new Emotion();
        emo.setContext(text);
        mCallEmotionList = mRetrofitAPI.getEmotionList(emo);
        mCallEmotionList.enqueue(mRetrofitCallback);
    }

    private Callback<Emotion> mRetrofitCallback = new Callback<Emotion>() {
        @Override
        public void onResponse(Call<Emotion> call, Response<Emotion> response) {
            Emotion result = response.body();
            String emotion = result.getAnswer();
            intent = new Intent(Diary.this, SplashTreenaActivity.class);
            intent.putExtra("emotion", emotion);
            startActivity(intent);
            Log.d("emotion", result.getAnswer());
        }

        @Override
        public void onFailure(Call<Emotion> call, Throwable t) {
            t.printStackTrace();
            System.out.println("결과 실패");
            Log.d("emotion", t.getMessage());
        }
    };
}