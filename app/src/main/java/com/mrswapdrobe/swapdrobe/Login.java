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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
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
    private TextView mTvPoliticaPrivacidad;
    int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Log.d("gungagingagunga","123");
                    Intent intent = new Intent(Login.this, PaginaPrincipal.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mIngresarBtn = (Button) findViewById(R.id.ingresarBtn);
        mRregistrarBtn = (Button) findViewById(R.id.registrarBtn);
        mFacebookBtn = (ImageView) findViewById(R.id.FacebookBtn);
        mGoogleBtn = (ImageView) findViewById(R.id.GoogleBtn);
        mPassword = (EditText) findViewById(R.id.passwordInput);
        mEmail = (EditText) findViewById(R.id.emailInput);
        cambiarContraseña = (TextView) findViewById(R.id.olvidaste);
        mTvPoliticaPrivacidad = (TextView) findViewById(R.id.TvPoliticaPrivacidad);
        //Login de google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);




        cambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, resetPassword.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mTvPoliticaPrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Login.this)
                        //.setTitle("")
                        .setMessage("Descargo de responsabilidad\n" +
                                "\n" +
                                "Última actualización: 10 de noviembre de 2020\n" +
                                "\n" +
                                "Interpretación y definiciones\n" +
                                "\n" +
                                "Interpretación\n" +
                                "Las palabras cuya letra inicial está en mayúscula tienen significados definidos bajo las siguientes condiciones. Las siguientes definiciones tendrán el mismo significado independientemente de que aparezcan en singular o en plural.\n" +
                                "Definiciones\n" +
                                "\n" +
                                "A los efectos de este descargo de responsabilidad:\n" +
                                "• Compañía (referida como \"la Compañía\", \"Nosotros\", \"Nos\" o \"Nuestro\" en este Aviso Legal) se refiere a Swapdrobe.\n" +
                                "\n" +
                                "• Servicio se refiere a la Aplicación.\n" +
                                "\n" +
                                "• Usted hace referencia a la persona que accede al Servicio, o la empresa u otra entidad legal en nombre de la cual dicha persona accede o utiliza el Servicio, según corresponda.\n" +
                                "\n" +
                                "• Aplicación significa el programa de software proporcionado por la Compañía descargado por Usted en cualquier dispositivo electrónico llamado Swapdrobe.\n" +
                                "\n" +
                                "Descargo de responsabilidad\n" +
                                "La información contenida en el Servicio es solo para fines de información general.\n" +
                                "\n" +
                                "La Compañía no asume ninguna responsabilidad por errores u omisiones en el contenido del Servicio.\n" +
                                "\n" +
                                "En ningún caso la Compañía será responsable de ningún daño especial, directo, indirecto, consecuente o incidental o cualquier daño de cualquier tipo, ya sea en una acción de contrato, negligencia u otro agravio, que surja de o en conexión con el uso del Servicio. o el contenido del Servicio. La Compañía se reserva el derecho de realizar adiciones, eliminaciones o modificaciones al contenido del Servicio en cualquier momento sin previo aviso. Este descargo de responsabilidad se ha creado con la ayuda del generador de descargo de responsabilidad.\n" +
                                "\n" +
                                "La Compañía no garantiza que el Servicio esté libre de virus u otros componentes dañinos.\n" +
                                "\n" +
                                "Descargo de responsabilidad de enlaces externos\n" +
                                "El Servicio puede contener enlaces a sitios web externos que no son proporcionados ni mantenidos por la Compañía ni están afiliados de ninguna manera con ella.\n" +
                                "\n" +
                                "Tenga en cuenta que la Compañía no garantiza la precisión, relevancia, puntualidad o integridad de la información en estos sitios web externos.\n" +
                                "\n" +
                                "Descargo de responsabilidad por errores y omisiones\n" +
                                "La información proporcionada por el Servicio es solo para orientación general sobre asuntos de interés. Incluso si la Compañía toma todas las precauciones para asegurarse de que el contenido del Servicio sea actual y preciso, pueden ocurrir errores. Además, dada la naturaleza cambiante de las leyes, reglas y regulaciones, puede haber demoras, omisiones o inexactitudes en la información contenida en el Servicio.\n" +
                                "La Compañía no se hace responsable de ningún error u omisión, ni de los resultados obtenidos del uso de esta información.\n" +
                                "\n" +
                                "Renuncia de responsabilidad por uso legítimo\n" +
                                "La Compañía puede usar material con derechos de autor que no siempre ha sido autorizado específicamente por el propietario de los derechos de autor. La Compañía pone dicho material a disposición para críticas, comentarios, informes de noticias, enseñanza, becas o investigación.\n" +
                                "\n" +
                                "La Compañía cree que esto constituye un \"uso justo\" de cualquier material protegido por derechos de autor según lo dispuesto en la sección 107 de la ley de derechos de autor de los Estados Unidos.\n" +
                                "\n" +
                                "Si desea utilizar material protegido por derechos de autor del Servicio para sus propios fines que van más allá del uso justo, debe obtener el permiso del propietario de los derechos de autor.\n" +
                                "Descargo de responsabilidad de Views Express\n" +
                                "El Servicio puede contener puntos de vista y opiniones de los autores y no reflejan necesariamente la política oficial o la posición de ningún otro autor, agencia, organización, empleador o empresa, incluida la Compañía.\n" +
                                "\n" +
                                "Los comentarios publicados por los usuarios son de su exclusiva responsabilidad y los usuarios asumirán toda la responsabilidad, responsabilidad y culpa por cualquier difamación o litigio que resulte de algo escrito en o como resultado directo de algo escrito en un comentario. La Compañía no es responsable de ningún comentario publicado por los usuarios y se reserva el derecho de eliminar cualquier comentario por cualquier motivo.\n" +
                                "\n" +
                                "Descargo de responsabilidad sin responsabilidad\n" +
                                "La información sobre el Servicio se proporciona en el entendimiento de que la Compañía no se dedica en este documento a brindar asesoramiento y servicios legales, contables, fiscales u otros servicios profesionales. Como tal, no debe utilizarse como sustituto de la consulta con asesores contables, fiscales, legales u otros profesionales competentes.\n" +
                                "En ningún caso la Compañía o sus proveedores serán responsables de ningún daño especial, incidental, indirecto o consecuente que surja de o en conexión con su acceso o uso o incapacidad para acceder o usar el Servicio.\n" +
                                "\n" +
                                "Exención de responsabilidad \"Use bajo su propio riesgo\"\n" +
                                "Toda la información en el Servicio se proporciona \"tal cual\", sin garantía de integridad, precisión, puntualidad o de los resultados obtenidos del uso de esta información, y sin garantía de ningún tipo, expresa o implícita, que incluye, entre otras, garantías de rendimiento, comerciabilidad e idoneidad para un propósito particular.\n" +
                                "La Compañía no será responsable ante usted ni ante nadie más por cualquier decisión o acción tomada en base a la información proporcionada por el Servicio o por cualquier daño consecuente, especial o similar, incluso si se le advierte de la posibilidad de tales daños.\n" +
                                "\n" +
                                "Contáctenos\n" +
                                "Si tiene alguna pregunta sobre este descargo de responsabilidad, puede contactarnos:\n" +
                                "• Por correo electrónico: mrswapdrobe@gmail.com")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Acepto", null).show();

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
                LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);
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
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);

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
    @Override
    public void onBackPressed()
    {

    }

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
