package com.example.navigationdrawerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
  EditText usernametxt;
  EditText passwordtxt;
  Button loginBtn;
  ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernametxt=findViewById(R.id.phone);
        passwordtxt=findViewById(R.id.password);
        loginBtn=findViewById(R.id.login);
        progressBar=findViewById(R.id.progress);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(usernametxt.getText().toString(),passwordtxt.getText().toString());
            }
        });

    }
    private void loginUser(String phone,String password){
        progressBar.setVisibility(View.VISIBLE);
        String realPhone="0789728209";
        String pass="123qwe";
        if(realPhone.equals(phone)&& pass.equals(password)){
            Intent mainActivityIntent=new Intent(LoginActivity.this,MainActivity.class);
            mainActivityIntent.putExtra("phone",phone);
            startActivity(mainActivityIntent);
            progressBar.setVisibility(View.GONE);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(),"Invalid username or password",Toast.LENGTH_SHORT);
            System.out.println("Invalid username or password");
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }


    }
}