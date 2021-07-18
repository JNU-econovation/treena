package com.pareutpareut.treena;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Setting extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button buttonLogout, buttonExit;
    Button button_find_password;
    FirebaseUser user;
    final String[] userInfo = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
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

        buttonLogout = (Button) findViewById(R.id.button_logout);
        buttonExit = (Button) findViewById(R.id.button_exit);
        button_find_password = findViewById(R.id.button_find_password);

        button_find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPassword();
            }
        });
        buttonLogout.setOnClickListener(this::onClick);
        buttonExit.setOnClickListener(this::onClick);
    }

    private void findPassword() {
        String emailAddress = userInfo[0];
        mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Setting.this, "이메일을 보냈습니다.", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    Toast.makeText(Setting.this, "메일 보내기에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                    ;

                }
            }
        });
    }

    private void signOut() {
        Toast.makeText(Setting.this, "로그아웃하였습니다.", Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
    }

    private void revokeAccess() {
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Setting.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_logout:
                signOut();
                finishAffinity();
                break;
            case R.id.button_exit:
                revokeAccess();
                finishAffinity();
                break;
        }
    }
}