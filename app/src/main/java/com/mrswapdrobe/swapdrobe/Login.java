package com.mrswapdrobe.swapdrobe;
/*
esta clase corresponde al activity_login. es la primera vista que se ve al ingresar a la app si no se esta logueado.
entrada: revisa si esta logueado, si lo esta se salta esta vista y va a pagina principal.
salida: al presionar en registrar, se puede crear una cuenta. al presionar en los logos de facebook o google, se registra o se loguea si es necesario mediante esos sistemas.
 */
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    private Button mIngresarBtn;
    private Button mRregistrarBtn;
    private ImageView mFacebookBtn;
    private ImageView mGoogleBtn;
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private EditText mEmail;
    private EditText mPassword;
    private TextView cambiarContraseña;
    int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mIngresarBtn = (Button) findViewById(R.id.ingresarBtn);
        mRregistrarBtn = (Button) findViewById(R.id.registrarBtn);
        mFacebookBtn = (ImageView) findViewById(R.id.FacebookBtn);
        mGoogleBtn = (ImageView) findViewById(R.id.GoogleBtn);
        mPassword = (EditText) findViewById(R.id.passwordInput);
        mEmail = (EditText) findViewById(R.id.emailInput);
        cambiarContraseña = (TextView) findViewById(R.id.olvidaste);
        //Login de google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(Login.this, PaginaPrincipal.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        cambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, resetPassword.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mIngresarBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (mEmail.getText().toString().trim().length() != 0 && mPassword.getText().toString().trim().length() != 0) {

                    final String email = mEmail.getText().toString();
                    final String password = mPassword.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, "Error en el ingreso", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(Login.this, "Debe rellenar los campos de email y contraseña.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        mRregistrarBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        //Presionar imagen de google
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("gungaginga","entro1");
                switch (view.getId()) {
                    case R.id.GoogleBtn:
                        Log.d("gungaginga","entro2");
                        signIn();
                        break;
                }
            }
        });



        //Presionar imagen de fb
        mFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email","public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                handleFacebookAccessToken(loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                            }
                        });            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) { //Si esta logueando con un Google.
            Log.d("gungaginga","entro4");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Log.d("gungaginga","entro5");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("gungaginga","entro5.2");
                Log.d("gungaginga","entro5.3"+account.getIdToken());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.d("gungaginga","error");
            }
        }
        else { //SI ESTA LOGUEANDO CON FACEBOOK
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Este metodo se encarga de obtener las credenciales de login de facebook. el mAuth(sesion actual) se le asignan las credencias y se da por completado.
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Ingreso Correctamente.",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(Login.this, "No puede ingresar con esta cuenta.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        Log.d("gungaginga","entro5.5");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        Log.d("gungaginga","entro6");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("gungaginga","entro7");
                        if (task.isSuccessful()) {
                            Log.d("gungaginga","entro8");

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
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

    //sign in de facebook
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Log.d("gungaginga","entro3");
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    /*
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        }
    */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(Login.this,PaginaPrincipal.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
