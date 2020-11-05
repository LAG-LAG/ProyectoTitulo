package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetPassword extends AppCompatActivity {

    private Button enviarCorreo;
    private EditText mEmail;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Cambiar Contraseña");
        }
        mEmail = (EditText) findViewById(R.id.emailChange);
        enviarCorreo = (Button) findViewById(R.id.changeBtn);
        fAuth = FirebaseAuth.getInstance();
        enviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmail.getText().toString().trim().length() != 0) {
                    String email = mEmail.getText().toString();
                    fAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(resetPassword.this, "Reset password link enviado a " + email, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(resetPassword.this, email + " No existe", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

                
                else{
                    Toast.makeText(resetPassword.this, "Debe rellenar el campo de email.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Login.class);
        startActivity(myIntent);
        finish();
        return true;
    }
}