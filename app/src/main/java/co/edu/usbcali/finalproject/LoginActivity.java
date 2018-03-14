package co.edu.usbcali.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void openRegister(View view) {
        Intent registerIntent = new Intent();
        registerIntent.setClass(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    public void login(View view) {
        Intent loginIntent = new Intent();
        loginIntent.setClass(this, MainActivity.class);
        startActivity(loginIntent);
    }
}
