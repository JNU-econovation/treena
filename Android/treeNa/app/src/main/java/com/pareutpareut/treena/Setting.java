package com.pareutpareut.treena;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Setting extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    Button buttonLogout, buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();
        buttonLogout = (Button) findViewById(R.id.button_logout);
        buttonExit = (Button) findViewById(R.id.button_exit);

        buttonLogout.setOnClickListener(this::onClick);
        buttonExit.setOnClickListener(this::onClick);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
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