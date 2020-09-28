package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    private Button mIngresarBtn,mRregistrarBtn;
    private ImageView mFacebookBtn,mGoogleBtn; //Correspondiente a la imagen de Facebook y Google respectivamente
    private CallbackManager callbackManager = CallbackManager.Factory.create(); //Utilizado para login y registro con Facebook.
    private EditText mEmail,mPassword;
    int RC_SIGN_IN = 0; //"Constante" utilizada para identificar que cuando se esta Logueado con google, se identifica con el 0.
    private FirebaseAuth mAuth; //Referencias a la base de datos de firebase
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener; //Referencia al auth de sistemas de cuenta de firebase
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Se asignan los botones del xml a los botones de esta clase.
        mIngresarBtn = (Button) findViewById(R.id.ingresarBtn);
        mRregistrarBtn = (Button) findViewById(R.id.registrarBtn);
        mFacebookBtn = (ImageView) findViewById(R.id.FacebookBtn);
        mGoogleBtn = (ImageView) findViewById(R.id.GoogleBtn);
        mPassword = (EditText) findViewById(R.id.passwordInput);
        mEmail = (EditText) findViewById(R.id.emailInput);

        //Login de google, se asigna, ve si esta logueado y obtiene el cliente para que este este iniciado en caso de que haya tenido una sesion activa.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        mAuth = FirebaseAuth.getInstance();
        //Revisa si esta loguado en firebase.
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){  //si esta logueado en la aplicacion, manda a la pagina principal.
                    Intent intent = new Intent(Login.this, PaginaPrincipal.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        //hacer lick en boton de ingresar, al poner email y password.
        mIngresarBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(Login.this, "Error en el ingreso", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        //hacer click en registrar, envia a la vista de registrar.
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
                switch (view.getId()) {
                    case R.id.GoogleBtn:
                        signIn(); //ejecuta el metodo signIn para loguearse/registrarse con Google.
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
                                handleFacebookAccessToken(loginResult.getAccessToken()); //si logra conectarse con su token, envia un token para loguearse.
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

    //este metodo trabaja con el login de facebook y el de google, recibe un request code que puede ser de facebook o de goog.e
    //si el requestcode es igual a 0, es el correspondiente a google, si el codigo que recibe es cualquier otro, corresponde a facebook,
    //llama al callback y se loguea.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) { //Si esta logueando con un Google.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken()); //envia el token de la cuenta de google a firebase
            } catch (ApiException e) {

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

    //Este metodo envia el token de la cuenta de google a firebase para ser logueado en el sistema.
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();  //asigna a user la sesion obtenida de firebase.
                        } else {
                        }
                    }
                });
    }

//Metodos necesarios para el authstatelistener
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

//Metodos necesarios para el authstatelistener
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    //sign in de Google.
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Metodo no usado actualmente para sign in de google (Deprecated).
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
