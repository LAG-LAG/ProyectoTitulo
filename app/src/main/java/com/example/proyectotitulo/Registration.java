package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    private Button mRegister;
    private EditText mEmail;
    private EditText mPassword,mPasswordConfirmation;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mRegister = (Button) findViewById(R.id.registroBtn);
        mPassword = (EditText) findViewById(R.id.passwordInputRegis);
        mEmail = (EditText) findViewById(R.id.emailInputRegis);
        mPasswordConfirmation = (EditText) findViewById(R.id.passwordInputRegis2);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(Registration.this, PaginaPrincipal.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (mEmail.getText().toString().trim().length() != 0 && mPassword.getText().toString().trim().length() != 0 && mPasswordConfirmation.getText().toString().trim().length() != 0) {

                    final String email = mEmail.getText().toString();
                    final String password = mPassword.getText().toString();
                    final String passwordConfirmation = mPasswordConfirmation.getText().toString();
                    if (password.equals(passwordConfirmation)) {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Registration.this, "Error en el registro.", Toast.LENGTH_SHORT).show();
                                } else {
                                }
                            }
                        });
                    } else {
                        Toast.makeText(Registration.this, "Contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Registration.this, "Debe rellenar los campos.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }
}