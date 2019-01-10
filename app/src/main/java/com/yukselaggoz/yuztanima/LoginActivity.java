package com.yukselaggoz.yuztanima;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button btnLogin = (Button) findViewById(R.id.bLogin);
        final Button btnSingup = (Button) findViewById(R.id.bRegisterHere);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this," " + etUsername.getText() + etPassword.getText(), Toast.LENGTH_SHORT).show();
                Intent registerIntent = new Intent(LoginActivity.this, SelectActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        btnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this," " + etUsername.getText() + etPassword.getText(), Toast.LENGTH_SHORT).show();
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });


    }
}
