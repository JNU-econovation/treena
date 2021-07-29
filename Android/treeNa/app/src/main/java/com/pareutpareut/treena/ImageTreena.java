package com.pareutpareut.treena;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ImageTreena extends AppCompatActivity {
    long diaryNumber;
    int type;
    int id;
    ArrayList<EmotionCommentVO> commentList;
    String ment;
    String imageUrl;
    int treeLevel;
    String[][] imageUrlList = {{"http://www.skillagit.com/data/product/1543845794.jpg", "http://www.skillagit.com/data/product/1543845794.jpg"},
            {"http://www.skillagit.com/data/product/1543845794.jpg"},
            {"http://www.skillagit.com/data/product/1543845794.jpg"}, {"http://www.skillagit.com/data/product/1543845794.jpg"},
            {"http://www.skillagit.com/data/product/1543845794.jpg"}, {"http://www.skillagit.com/data/product/1543845794.jpg"},
            {"http://www.skillagit.com/data/product/1543845794.jpg"}, {"http://www.skillagit.com/data/product/1543845794.jpg"}, {"http://www.skillagit.com/data/product/1543845794.jpg"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_treena);
        ImageView imageView = findViewById(R.id.imageView_treena);
        TextView textView = findViewById(R.id.textView_ment);

        Intent intent = getIntent();
        String emotion = intent.getExtras().getString("emotion");
        imageUrl = "";
        ment = "";
        commentList = new ArrayList<>();
        commentList = getCommentList();
        type = 0;
        diaryNumber = 0;
        id = 0;

        //쓴 일기 개수 체크
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        databaseReference.child("diary").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    diaryNumber = task.getResult().getChildrenCount();
                    Log.d("emotion", "다이어리 양 : " + String.valueOf(diaryNumber));

                }
            }
        });

        treeLevel = getTreeLevel();
        //받아온 감정 타입 체크
        for (int i = 0; i < commentList.size(); i++) {
            if (emotion.equals(commentList.get(i).getName())) {
                type = commentList.get(i).getType();
                Log.d("emotion", "type : " + String.valueOf(type));
                id = i;
                break;
            }
        }

        int mentRandom = (int) ((Math.random() * 10000) % (commentList.get(id).getResponse().size()));
        Log.d("emotion", "랜덤 멘트 : " + String.valueOf(mentRandom));
        ment = commentList.get(id).getResponse().get(mentRandom);
        textView.setText(ment);

        if (type == 0) {
            int imageRandom = (int) ((Math.random() * 10000) % 2);
            imageUrl = imageUrlList[imageRandom][treeLevel];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
        } else if (type == 1) {
            int imageRandom = (int) ((Math.random() * 10000) % 3);
            imageUrl = imageUrlList[imageRandom][treeLevel];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
        } else if (type == -1) {
            imageUrl = imageUrlList[5][treeLevel];
            Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
        }

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("emotion", "timer 작동");
                Intent intent1 = new Intent(ImageTreena.this, MainActivity.class);
                intent1.putExtra("number", diaryNumber);
                startActivity(intent1);
            }
        };
        timer.schedule(timerTask, 6000);
    }

    private ArrayList<EmotionCommentVO> getCommentList() {
        ArrayList<EmotionCommentVO> list = new ArrayList<>();
        Gson gson = new Gson();

        try {
            InputStream is = getAssets().open("emotion_result 2.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("emotions");

            int index = 0;

            while (index < jsonArray.length()) {
                EmotionCommentVO commentVO = gson.fromJson(jsonArray.get(index).toString(), EmotionCommentVO.class);
                list.add(commentVO);

                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private int getTreeLevel() {
        if (diaryNumber <= 2) {
            return 0;
        }
        if (diaryNumber > 2 && diaryNumber <= 7) {
            return 1;
        }
        if (diaryNumber > 7 && diaryNumber <= 14) {
            return 2;
        }
        if (diaryNumber > 14 && diaryNumber <= 21) {
            return 3;
        }
        if (diaryNumber > 21 && diaryNumber <= 28) {
            return 4;
        }
        if (diaryNumber > 28 && diaryNumber <= 35) {
            return 5;
        }
        if (diaryNumber > 35 && diaryNumber <= 42) {
            return 6;
        }
        if (diaryNumber > 42 && diaryNumber <= 49) {
            return 7;
        }
        if (diaryNumber > 49 && diaryNumber <= 56) {
            return 8;
        }
        return 0;
    }
}