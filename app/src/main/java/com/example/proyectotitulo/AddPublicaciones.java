package com.example.proyectotitulo;

/*Clase correspondiente a la activity_add_publicaciones.xml la cual corresponde a la vista para crear publicaciones del usuario..
Entrada: Esta clase se encarga de traer el UID del usuario actualmente logueado para luego guardar publicacion enlazada a ese usuario.
Salida: al presionar guardar esta guarda la informacion y la verifica que este correcta, guardandola en la base de datos. Esta clase utiliza una funcion de inteligencia artificial para verificar que una foto no sea sexual.
*/
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nipunru.nsfwdetector.NSFWDetector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPublicaciones extends AppCompatActivity {
    private DatabaseReference mCustomerDatabase;
    private static int PICK_FROM_GALLERY = 1;
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
    private int puedeSubir;
    private ImageView mPublicacionImage1;

    private ImageView mPublicacionImage2;
    private ImageView mPublicacionImage3;
    private ImageView mPublicacionImage4;
    private ImageView mPublicacionImage5;
    private ImageView mPublicacionImage6;
    private ImageView mBorrarPublicacion1,mBorrarPublicacion2,mBorrarPublicacion3,mBorrarPublicacion4,mBorrarPublicacion5,mBorrarPublicacion6;
    private int publicacion1,publicacion2,publicacion3,publicacion4,publicacion5,publicacion6;
    ProgressDialog dialog;
    private StorageReference mStorageRef;
    private static final int IMAGE_CODE = 1;
    private Button selectBtn;
    RecyclerView recyclerView;
    List<Imagen> modalClassList;
    addImagenesAdapter customAdapter;



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
        selectBtn = findViewById(R.id.button2);
        recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStorageRef = FirebaseStorage.getInstance().getReference();
        modalClassList = new ArrayList<>();

        //if x ispressed then resulturi correspondiente = null y se
        mAuth = FirebaseAuth.getInstance();
        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Añadir Aviso");
        }


        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, IMAGE_CODE);

            }
        });

        mAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!modalClassList.isEmpty()) {
                    savePublicacion();
                }
                else{
                    Toast.makeText(AddPublicaciones.this, "Debe subir al menos una foto", Toast.LENGTH_SHORT).show();
                }

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


        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                int totalitem = data.getClipData().getItemCount();

                for (int i = 0; i < totalitem; i++) {

                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    String imagename = getFileName(imageUri);


                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);


                        float confidenceThreshold = (float) 0.68; //radio de margen, mientras mas cercano al 1 permite foto mas nsfw.
                        NSFWDetector.INSTANCE.isNSFW(bitmap, confidenceThreshold, (isNSFW, confidence, image) -> {
                            if (isNSFW) {
                                Toast.makeText(this, "FOTO NO PERMITIDA.", Toast.LENGTH_SHORT).show();
                                puedeSubir = 0;
                            } else {
                                Imagen modalClass = new Imagen(imagename,imageUri);
                                modalClassList.add(modalClass);
                                customAdapter = new addImagenesAdapter(AddPublicaciones.this, modalClassList);
                                recyclerView.setAdapter(customAdapter);
                            }
                            return kotlin.Unit.INSTANCE;
                        });
                }


            } else if (data.getData() != null) {
                //Toast.makeText(this, "single", Toast.LENGTH_SHORT).show();
                Uri imageUri = data.getData();
                String imagename = getFileName(imageUri);



                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                float confidenceThreshold = (float) 0.68; //radio de margen, mientras mas cercano al 1 permite foto mas nsfw.
                NSFWDetector.INSTANCE.isNSFW(bitmap, confidenceThreshold, (isNSFW, confidence, image) -> {
                    if (isNSFW) {
                        Toast.makeText(this, "FOTO NO PERMITIDA. ", Toast.LENGTH_SHORT).show();
                        puedeSubir = 0;
                    } else {
                        Imagen modalClass = new Imagen(imagename,imageUri);
                        modalClassList.add(modalClass);
                        customAdapter = new addImagenesAdapter(AddPublicaciones.this, modalClassList);
                        recyclerView.setAdapter(customAdapter);
                    }
                    return kotlin.Unit.INSTANCE;
                });

//
            }

        }

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
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
            if (titulo != "" && valor != "" && tipoPrenda != "Seleccione tipo de prenda" && tallaPrenda != "Seleccione talla" && colorPrenda != "Seleccione color" && estadoPrenda != "Seleccione estado") {
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

            } else {
                Toast.makeText(AddPublicaciones.this, "Datos Incorrectos.", Toast.LENGTH_SHORT).show();
            }

            for (int i = 0; i < modalClassList.size(); i++) {
                String imagename = modalClassList.get(i).getImagename();
                final int tamanoLogico = modalClassList.size() - 1;
                final int posicion = i;
                Log.d("verveces", "tamañoLogico:" + tamanoLogico + " Posicion: " + posicion);
                final String idPrenda;
                int numero = i + 1;
                idPrenda = "" + numero;


                final StorageReference mRef = mStorageRef.child("prendasImages").child(userId).child(id).child(idPrenda);
                ;
                Uri imageUri = modalClassList.get(i).getImage();

                mClothesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("clothesPhotos");


                mClothesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("clothes").child(id).child("clothesPhotos");
                dialog = new ProgressDialog(AddPublicaciones.this);
                dialog.setMessage("Cargando");
                dialog.show();


                mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                dialog.dismiss();
                                Map newImage = new HashMap();
                                newImage.put("photoId" + idPrenda, uri.toString());
                                mClothesDatabase.updateChildren(newImage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                        Toast.makeText(AddPublicaciones.this, "Terminado.", Toast.LENGTH_SHORT).show();
                        if (tamanoLogico == posicion) {
                            Intent intentAccount = new Intent(AddPublicaciones.this, PaginaPrincipal.class);
                            startActivity(intentAccount);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPublicaciones.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }



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
                            finish();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            finish();
                            return;
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
                Intent intentChat = new Intent(AddPublicaciones.this, misChats.class);
                startActivity(intentChat);
                finish();
                break;

            case R.id.publicacionesBtn:
                Intent intentPublicaciones = new Intent(AddPublicaciones.this, PaginaPrincipal.class);
                startActivity(intentPublicaciones);
                finish();
                break;

            case R.id.cerrarSesionBtn:
                mAuth.signOut(); //desconecta
                //las lineas de abajo mandan de la ventana actual(mainactiviy) a la de chooseloginregistration que es la de antes de estar loguado.
                Intent intent = new Intent(AddPublicaciones.this,Login.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}