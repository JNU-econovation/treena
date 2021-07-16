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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalendarDiary extends AppCompatActivity {
    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<Emotion> mCallEmotionList;
    private Gson mGson;
    private int year;
    private int month;
    private int day;
    private String calendarDate;
    private String monthStr;
    private String dayStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_diary);

        TextView textView_date = findViewById(R.id.textView_clendar_date);
        EditText editText = findViewById(R.id.editText_calendar_diary_content);
        Button button_save = findViewById(R.id.button_calendar_diary_save);
        Button button_temporary_save = findViewById(R.id.button_calendar_diary_temporay_save);
        Button button_calendar = findViewById(R.id.button_move_calendar);
        Button button_before = findViewById(R.id.button_before);
        Button button_next = findViewById(R.id.button_next);

        Intent intent = getIntent();
        String calendarDate = intent.getExtras().getString("calendarDate");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        year = Integer.parseInt(calendarDate.substring(0,4));
        month = Integer.parseInt(calendarDate.substring(4,6));
        day = Integer.parseInt(calendarDate.substring(6));
        monthStr = String.valueOf(month);
        dayStr = String.valueOf(day);

        String date = year+"년 "+month+"월 "+day+"일";

        textView_date.setText(date);

        //retrofit
        setRetrofitInit();

        //캘린더로 이동
        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarDiary.this, Calendar.class);
                startActivity(intent);
            }
        });

        //전 날의 일기 보기
        button_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day -= 1;
                if(day <10){
                    dayStr = "0" + day;
                }else{
                    dayStr = ""+day;
                }
                if(month < 10){
                    monthStr = "0"+month;
                }else{
                    monthStr = ""+month;
                }
                String queryDate = year+""+monthStr+dayStr;
                String beforeDate = year+"년 "+month+"월 "+day+"일";
                textView_date.setText(beforeDate);

                databaseReference.child("diary").child(uid).child(queryDate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            String diary = String.valueOf(task.getResult().getValue());
                            if (!task.getResult().exists()) {
                                editText.setText("일기를 작성해 보세요!");
                            } else {
                                editText.setText(diary);
                            }
                        }
                    }
                });
            }
        });

        //다음 날의 일기 보기
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day += 1;
                if(day <10){
                    dayStr = "0" + day;
                }else{
                    dayStr = ""+day;
                }
                if(month < 10){
                    monthStr = "0"+month;
                }else{
                    monthStr = ""+month;
                }
                String queryDate = year+""+monthStr+dayStr;
                String nextDate = year+"년 "+month+"월 "+day+"일";
                textView_date.setText(nextDate);

                databaseReference.child("diary").child(uid).child(queryDate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            String diary = String.valueOf(task.getResult().getValue());
                            if (!task.getResult().exists()) {
                                editText.setText("일기를 작성해 보세요!");
                            } else {
                                editText.setText(diary);
                            }
                        }
                    }
                });
            }
        });

        //데이터베이스에서 일기 읽어오기
        databaseReference.child("diary").child(uid).child(calendarDate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String diary = String.valueOf(task.getResult().getValue());
                    if (!task.getResult().exists()) {
                        editText.setText("일기를 작성해 보세요!");
                    } else {
                        editText.setText(diary);
                    }
                }
            }
        });

        //데이터베이스에 일기 저장
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("diary").child(uid).child(calendarDate).setValue(editText.getText().toString());
                callEmotionList(editText.getText().toString());
                Toast.makeText(getApplicationContext(), "일기가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //데이터베이스에 일기 일 저장
        button_temporary_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("diary").child(uid).child(calendarDate).setValue(editText.getText().toString());
                Toast.makeText(getApplicationContext(), "일기가 임시 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setRetrofitInit() {
        mRetrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
        mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);
    }

    private void callEmotionList(String text){
        Emotion emo = new Emotion();
        emo.setContext(text);
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