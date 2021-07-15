package com.pareutpareut.treena;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Diary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);

        String today = year + "년 " + month + "월 " + day + "일 ";
        String date = year + month + day;

        TextView textView = findViewById(R.id.textView);
        textView.setText(today);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userReference = databaseReference.child("Users").child(uid);
        final String[] userInfo = new String[2];

        userReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                userInfo[0] = task.getResult().child("email").getValue(String.class);
                userInfo[1] = task.getResult().child("name").getValue(String.class);
                Log.d("userTest", userInfo[0] + " " + userInfo[1]);
            }
        });

        EditText editText = findViewById(R.id.editText);
        Button button_save = findViewById(R.id.button_save);

        //데이터베이스에 일기 저장
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("diary").child(uid).child(date).setValue(editText.getText().toString());
                Toast.makeText(getApplicationContext(), "일기가 저장되었습니다.", Toast.LENGTH_SHORT).show();
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
                        editText.setText("오늘의 일기를 작성해 보세요!");
                    } else {
                        editText.setText(diary);
                    }
                }
            }
        });

    }
}