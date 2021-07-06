package com.pareutpareut.treena;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Calendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        SimpleDateFormat dataformat = new SimpleDateFormat("yyyyMMdd");

        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull  MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String test = dataformat.format(date.getDate());
                Log.d("calendarTest",test);
            }
        });

    }
}