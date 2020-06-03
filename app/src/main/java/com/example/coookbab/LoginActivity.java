package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private EditText et_email, et_password;
    private Button btn_newid, btn_login;
    private FirebaseAuth mAuth;

    private String email = "";
    private String password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        btn_newid=findViewById(R.id.btn_newid);
        btn_login=findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();

        btn_newid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser(et_email.getText().toString(), et_password.getText().toString());
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(et_email.getText().toString(), et_password.getText().toString());
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_LONG).show();
                            SavedSharedPrefrence.setUserName(LoginActivity.this, et_email.getText().toString());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean isValidEmail() {
        if(email.isEmpty()){
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        } else{
            return true;
        }
    }

    private boolean isValidPassword() {
        if(password.isEmpty()){
            return false;
        }else if(!PASSWORD_PATTERN.matcher(password).matches()){
            return false;
        }else{
            return true;
        }
    }

    private void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "회원가입 성공", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "회원가입 실패", Toast.LENGTH_LONG).show();
                        }
                        // ...
                    }
                });
    }
}
