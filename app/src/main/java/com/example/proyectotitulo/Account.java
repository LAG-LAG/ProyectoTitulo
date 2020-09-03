package com.example.proyectotitulo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }

    public void atras(View view) {
        Intent intent = new Intent(Account.this, PaginaPrincipal.class);
        startActivity(intent);
        finish();
        return;
    }
}