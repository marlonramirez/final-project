package co.edu.usbcali.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText txtUser;
    private EditText txtPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        txtUser = findViewById(R.id.txt_user);
        txtPassword = findViewById(R.id.txt_password);
    }

    public void openRegister(View view) {
        Intent registerIntent = new Intent();
        registerIntent.setClass(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    public void login(View view) {
        String user = txtUser.getText().toString();
        String password = txtPassword.getText().toString();
        if (user == null || user.isEmpty() || password == null || password.isEmpty()) {
            showMessage("Por favor digite un usuario y contraseña");
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(user, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent loginIntent = new Intent();
                            loginIntent.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(loginIntent);
                        } else {
                            showMessage("Usuario o contraseña invalidos");
                        }
                    }
                });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
