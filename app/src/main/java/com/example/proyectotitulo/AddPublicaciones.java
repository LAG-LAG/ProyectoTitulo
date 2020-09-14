package com.example.proyectotitulo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddPublicaciones extends AppCompatActivity {
    private DatabaseReference mCustomerDatabase;
    private static final int PICK_FROM_GALLERY = 1;
    private FirebaseAuth mAuth;
    private Button mAplicar;
    private EditText mTitulo;
    private EditText mValor;
    private Spinner mTipoPrendaSpinner;
    private Spinner mTallaSpinner;
    private Spinner mColorSpinner;
    private Spinner mEstadoPrendaSpinner;
    private EditText mDescripcion;
    private Uri resultUri;
    private Uri resultUri2;
    private Uri resultUri3;
    private Uri resultUri4;
    private Uri resultUri5;
    private Uri resultUri6;
    private DatabaseReference mClothesDatabase;

    private ImageView mPublicacionImage1;

    private ImageView mPublicacionImage2;
    private ImageView mPublicacionImage3;
    private ImageView mPublicacionImage4;
    private ImageView mPublicacionImage5;
    private ImageView mPublicacionImage6;
    private ImageView mBorrarPublicacion1,mBorrarPublicacion2,mBorrarPublicacion3,mBorrarPublicacion4,mBorrarPublicacion5,mBorrarPublicacion6;
    private int publicacion1,publicacion2,publicacion3,publicacion4,publicacion5,publicacion6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publicaciones);
        mTitulo = (EditText) findViewById(R.id.editTextTitulo);
        mValor = (EditText) findViewById(R.id.editTextValor);
        mTipoPrendaSpinner = (Spinner) findViewById(R.id.TipoPrendaSpinner);
        mTallaSpinner = (Spinner) findViewById(R.id.TallaSpinner);
        mColorSpinner = (Spinner) findViewById(R.id.ColorSpinner);
        mEstadoPrendaSpinner = (Spinner) findViewById(R.id.estadoPrendaSpinner);
        mDescripcion = (EditText) findViewById(R.id.editTextDescripcion);
        mAplicar = (Button) findViewById(R.id.publicarBtn);

        mPublicacionImage1 = (ImageView) findViewById(R.id.publicacionImageCrear1);
        mPublicacionImage2 = (ImageView) findViewById(R.id.publicacionImageCrear2);
        mPublicacionImage3 = (ImageView) findViewById(R.id.publicacionImageCrear3);
        mPublicacionImage4 = (ImageView) findViewById(R.id.publicacionImageCrear4);
        mPublicacionImage5 = (ImageView) findViewById(R.id.publicacionImageCrear5);
        mPublicacionImage6 = (ImageView) findViewById(R.id.publicacionImageCrear6);

        mBorrarPublicacion1 = (ImageView)findViewById(R.id.borrarPublicacionCrear1);
        mBorrarPublicacion2 = (ImageView)findViewById(R.id.borrarPublicacionCrear2);
        mBorrarPublicacion3 = (ImageView)findViewById(R.id.borrarPublicacionCrear3);
        mBorrarPublicacion4 = (ImageView)findViewById(R.id.borrarPublicacionCrear4);
        mBorrarPublicacion5 = (ImageView)findViewById(R.id.borrarPublicacionCrear5);
        mBorrarPublicacion6 = (ImageView)findViewById(R.id.borrarPublicacionCrear6);

        mBorrarPublicacion1.setVisibility(View.INVISIBLE);
        mBorrarPublicacion2.setVisibility(View.INVISIBLE);
        mBorrarPublicacion3.setVisibility(View.INVISIBLE);
        mBorrarPublicacion4.setVisibility(View.INVISIBLE);
        mBorrarPublicacion5.setVisibility(View.INVISIBLE);
        mBorrarPublicacion6.setVisibility(View.INVISIBLE);


        //if x ispressed then resulturi correspondiente = null y se
        mAuth = FirebaseAuth.getInstance();
        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Añadir publicacion");
        }


        mBorrarPublicacion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri!=null) {
                    final Uri imageUri = null;
                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                    resultUri = imageUri;
                    mPublicacionImage1.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion1.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBorrarPublicacion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri2!=null) {
                    final Uri imageUri = null;
                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                    resultUri2 = imageUri;
                    mPublicacionImage2.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion2.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBorrarPublicacion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri3!=null) {
                    final Uri imageUri = null;
                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                    resultUri3 = imageUri;
                    mPublicacionImage3.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion3.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBorrarPublicacion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri4!=null) {
                    final Uri imageUri = null;
                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                    resultUri4 = imageUri;
                    mPublicacionImage4.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion4.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBorrarPublicacion5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri5!=null) {
                    final Uri imageUri = null;
                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                    resultUri5 = imageUri;
                    mPublicacionImage5.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion5.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBorrarPublicacion6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultUri6!=null) {
                    final Uri imageUri = null;
                    // final Uri imageUri = Uri.parse("android.resource://ProyectoTitulo/drawable/image_name");
                    resultUri6 = imageUri;
                    mPublicacionImage6.setImageResource(R.drawable.ic_launcher_foreground);
                    mBorrarPublicacion6.setVisibility(View.INVISIBLE);
                }
            }
        });

        mPublicacionImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion1=1;
                comprobarImagen();
                mBorrarPublicacion1.setVisibility(View.VISIBLE);
            }
        });
        mPublicacionImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion2=1;
                comprobarImagen();
                mBorrarPublicacion2.setVisibility(View.VISIBLE);
            }
        });
        mPublicacionImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion3=1;
                comprobarImagen();
                mBorrarPublicacion3.setVisibility(View.VISIBLE);
            }
        });
        mPublicacionImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion4=1;
                comprobarImagen();
                mBorrarPublicacion4.setVisibility(View.VISIBLE);
            }
        });
        mPublicacionImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion5=1;
                comprobarImagen();
                mBorrarPublicacion5.setVisibility(View.VISIBLE);
            }
        });
        mPublicacionImage6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicacion6=1;
                comprobarImagen();
                mBorrarPublicacion6.setVisibility(View.VISIBLE);
            }
        });
        mAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePublicacion();
            }
        });


    }

    private void comprobarImagen() {
        if (ActivityCompat.checkSelfPermission(AddPublicaciones.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddPublicaciones.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();

            if(publicacion1==1){
                resultUri = imageUri;
                mPublicacionImage1.setImageURI(resultUri);

                publicacion1=0;
            }
            else if(publicacion2==1) {
                resultUri2 = imageUri;
                mPublicacionImage2.setImageURI(resultUri2);
                publicacion2=0;
            }
            else if(publicacion3==1) {
                resultUri3 = imageUri;
                mPublicacionImage3.setImageURI(resultUri3);
                publicacion3=0;
            }
            else if(publicacion4==1) {
                resultUri4 = imageUri;
                mPublicacionImage4.setImageURI(resultUri4);
                publicacion4=0;
            }
            else if(publicacion5==1) {
                resultUri5 = imageUri;
                mPublicacionImage5.setImageURI(resultUri5);
                publicacion5=0;
            }
            else if(publicacion6==1) {
                resultUri6 = imageUri;
                mPublicacionImage6.setImageURI(resultUri6);
                publicacion6=0;
            }
        }
    }

    private void savePublicacion() {

        String userId = mAuth.getCurrentUser().getUid();
        String id = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").push().getKey();
        DatabaseReference currentUserNamePrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("tituloPublicacion"); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo
        String titulo = mTitulo.getText().toString();
        String valor = mValor.getText().toString();
        String tipoPrenda = String.valueOf(mTipoPrendaSpinner.getSelectedItem());
        String tallaPrenda = String.valueOf(mTallaSpinner.getSelectedItem());
        String colorPrenda = String.valueOf(mColorSpinner.getSelectedItem());
        String estadoPrenda = String.valueOf(mEstadoPrendaSpinner.getSelectedItem());
        String descripcionPrenda = mDescripcion.getText().toString();
        //DatabaseReference dueño = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("idDueño");
        //dueño.setValue(userId);

        //mRegionesSpinner.getSelectedItem();
        if(titulo != "" && valor != "" && tipoPrenda != "Seleccione tipo de prenda" && tallaPrenda != "Seleccione talla" && colorPrenda != "Seleccione color" && estadoPrenda != "Seleccione estado"){
            currentUserNamePrenda.setValue(mTitulo.getText().toString()); //Aca va y le asigna el nombre al User.

            DatabaseReference currentUserValorPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("ValorPrenda");
            currentUserValorPrenda.setValue(valor);
            DatabaseReference currentUserTipoPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("TipoPrenda");
            currentUserTipoPrenda.setValue(tipoPrenda);
            DatabaseReference currentUserTallaPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("TallaPrenda");
            currentUserTallaPrenda.setValue(tallaPrenda);
            DatabaseReference currentUserColorPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("ColorPrenda");
            currentUserColorPrenda.setValue(colorPrenda);
            DatabaseReference currentUserEstadoPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("EstadoPrenda");
            currentUserEstadoPrenda.setValue(estadoPrenda);
            DatabaseReference currentUserDescripcionPrenda = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("DescripcionPrenda");
            currentUserDescripcionPrenda.setValue(descripcionPrenda);

        }
        else{
            Toast.makeText(AddPublicaciones.this, "Datos Incorrectos.", Toast.LENGTH_SHORT).show();
        }
        guardarImagen(resultUri,userId,"1",id);
        guardarImagen(resultUri2,userId,"2",id);
        guardarImagen(resultUri3,userId,"3",id);
        guardarImagen(resultUri4,userId,"4",id);
        guardarImagen(resultUri5,userId,"5",id);
        guardarImagen(resultUri6,userId,"6",id);

    }

    private void guardarImagen(Uri resultUri, String userId, final String idPrenda,String id) {
        if (resultUri != null) {
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("prendasImages").child(userId).child(id).child(idPrenda);
            Bitmap bitmap = null;
            mClothesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("clothesPhotos");

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map newImage = new HashMap();
                            newImage.put("photoId" + idPrenda, uri.toString());
                            mClothesDatabase.updateChildren(newImage);
                            //finish();
                            //return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //finish();
                            //return;
                        }
                    });
                }
            });
        }
    }

    //Crea el menu en la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //Controla los botones del menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.accountBtn:
                Intent intentAccount = new Intent(AddPublicaciones.this, VerMiCuenta.class);
                startActivity(intentAccount);
                finish();
                break;

            case R.id.chatBtn:
                Intent intentChat = new Intent(AddPublicaciones.this, Chat.class);
                startActivity(intentChat);
                finish();
                break;

            case R.id.publicacionesBtn:
                Intent intentPublicaciones = new Intent(AddPublicaciones.this, PaginaPrincipal.class);
                startActivity(intentPublicaciones);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}