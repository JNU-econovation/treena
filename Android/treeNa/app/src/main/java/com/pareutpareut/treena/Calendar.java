package com.pareutpareut.treena;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashSet;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Calendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Button button_home = findViewById(R.id.button_move_home);
        Button button_write = findViewById(R.id.button_write);
        MaterialCalendarView calendarView = findViewById(R.id.calendarView);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        SimpleDateFormat dataformat = new SimpleDateFormat("yyyyMMdd");

        final HashSet<CalendarDay> dateSet = new HashSet<>();
        int[] date = new int[3];

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, Diary.class);
                startActivity(intent);
            }
        });

        //캘린더뷰 클릭 이벤트
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String calendarDate = dataformat.format(date.getDate());
                Log.d("calendarTest", calendarDate);
                Intent intent = new Intent(Calendar.this, CalendarDiary.class);
                intent.putExtra("calendarDate", calendarDate);
                startActivity(intent);
            }
        });

        //데이터베이스에서 일기 쓴 날짜들 불러와서 HashSet에 저장
        databaseReference.child("diary").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    String str = fileSnapshot.getKey();
                    Log.d("calendarTest", str);

                    date[0] = Integer.parseInt(str.substring(0, 4));
                    if (str.substring(4, 5).equals("0")) {
                        date[1] = Integer.parseInt(str.substring(5, 6)) - 1;
                    } else {
                        date[1] = Integer.parseInt(str.substring(4, 6)) - 1;
                    }

                    if (str.substring(6, 7).equals("0")) {
                        date[2] = Integer.parseInt(str.substring(7, 8));
                    } else {
                        date[2] = Integer.parseInt(str.substring(6, 8));
                    }
                    dateSet.add(new CalendarDay(date[0], date[1], date[2]));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //달력에 일기 쓴 날짜 데이터 UI 적용 (delay 2)
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("calendarTest", "set 마지막 추가");
                        calendarView.addDecorator(new CalendarDecorator(Color.RED, dateSet, Calendar.this));
                    }
                });
            }
        };
        timer.schedule(timerTask, 2000);
    }
}