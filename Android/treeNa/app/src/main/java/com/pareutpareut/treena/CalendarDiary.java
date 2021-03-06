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
        Button button_before = findViewById(R.id.button_before);
        Button button_next = findViewById(R.id.button_next);
        Button button_home = findViewById(R.id.button_home);
        Button button_setting = findViewById(R.id.button_setting);
        Button button_delete = findViewById(R.id.button_delete);

        Intent intent = getIntent();
        calendarDate = intent.getExtras().getString("calendarDate");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        year = Integer.parseInt(calendarDate.substring(0, 4));
        month = Integer.parseInt(calendarDate.substring(4, 6));
        day = Integer.parseInt(calendarDate.substring(6));
        monthStr = String.valueOf(month);
        dayStr = String.valueOf(day);

        String date = year + "??? " + month + "??? " + day + "???";

        textView_date.setText(date);

        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarDiary.this, Setting.class));
            }
        });

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarDiary.this, SplashActivity.class);
                startActivity(intent);
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("diary").child(uid).child(calendarDate).removeValue();
                Toast.makeText(getApplicationContext(), "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
            }
        });

        //retrofit
        setRetrofitInit();

        //??? ?????? ?????? ??????
        button_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day -= 1;
                if(day == 0){
                    month -= 1;
                    day = 31;
                }
                if (day < 10) {
                    dayStr = "0" + day;
                } else {
                    dayStr = "" + day;
                }
                if (month < 10) {
                    monthStr = "0" + month;
                } else {
                    monthStr = "" + month;
                }
                String queryDate = year + "" + monthStr + dayStr;
                String beforeDate = year + "??? " + month + "??? " + day + "???";
                calendarDate = queryDate;
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
                                editText.setText("");
                            } else {
                                editText.setText(diary);
                            }
                        }
                    }
                });
            }
        });

        //?????? ?????? ?????? ??????
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day += 1;
                if(day ==32){
                    month += 1;
                    day = 1;
                }
                if (day < 10) {
                    dayStr = "0" + day;
                } else {
                    dayStr = "" + day;
                }
                if (month < 10) {
                    monthStr = "0" + month;
                } else {
                    monthStr = "" + month;
                }

                String queryDate = year + "" + monthStr + dayStr;
                calendarDate = queryDate;
                String nextDate = year + "??? " + month + "??? " + day + "???";
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
                                editText.setText("");
                            } else {
                                editText.setText(diary);
                            }
                        }
                    }
                });
            }
        });

        //???????????????????????? ?????? ????????????
        databaseReference.child("diary").child(uid).child(calendarDate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

        //????????????????????? ?????? ??????
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("diary").child(uid).child(calendarDate).setValue(editText.getText().toString());
                callEmotionList(editText.getText().toString());
                Toast.makeText(getApplicationContext(), "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
            }
        });

        //????????????????????? ?????? ?????? ??????
        button_temporary_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("diary").child(uid).child(calendarDate).setValue(editText.getText().toString());
                Toast.makeText(getApplicationContext(), "????????? ?????? ?????????????????????.", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(CalendarDiary.this, SplashTreenaActivity.class);
            intent.putExtra("emotion", emotion);
            startActivity(intent);
            Log.d("emotion", result.getAnswer());
        }

        @Override
        public void onFailure(Call<Emotion> call, Throwable t) {
            t.printStackTrace();
            System.out.println("?????? ??????");
            Log.d("emotion", t.getMessage());
        }
    };
}