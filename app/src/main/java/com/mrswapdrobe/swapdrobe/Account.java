package com.mrswapdrobe.swapdrobe;

/*Clase correspondiente a la activity_account.xml la cual corresponde a la vista de edicion de los datos del usuario..
Entrada: Esta clase se encarga de traer los elementos a la funcion getUserInfo.  entre la informacion que trae se encuentra: ubicacion, ciudad, region, nombre de usuario y foto de perfil.
Salida: al presionar guardar esta guarda la informacion y la verifica que este correcta, guardandola en la base de datos. Esta clase utiliza una funcion de inteligencia artificial para verificar que una foto no sea sexual.
*/


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nipunru.nsfwdetector.NSFWDetector;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Account extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 1;
    private DatabaseReference mCustomerDatabase;
    private FirebaseAuth mAuth;
    private Button mAplicar, mUbicacion;
    private EditText mNombre;
    private ImageView mProfileImage, mborrarFotoPerfil;
    private String nombreUsuario;
    int PLACE_PICKER_REQUEST = 1;
    private final static int REQUEST_PLACE_PICKER =1001;

    private int tieneFotoDePerfil;
    private Spinner mRegionesSpinner;
    private String comunaAnterior, profileImageUrl;
    private String regionAnterior,puntuacionGeneral;
    private ProgressDialog dialog;
    private Spinner mComunasSpinner;
    private Uri resultUri;
    private int estadoComunas, borrarFotoPerfil, existeFotoPerfil, longitudLatitudEstado;
    private double latitude, longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
private boolean addLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        puntuacionGeneral = "-1";
        addLocation = false;
        setContentView(R.layout.activity_account);
        mAplicar = (Button) findViewById(R.id.aplicar);
        mNombre = (EditText) findViewById(R.id.name);
        mProfileImage = (ImageView) findViewById(R.id.profileImageUrl);
        mborrarFotoPerfil = (ImageView) findViewById(R.id.borrarFotoPerfil);
        mRegionesSpinner = (Spinner) findViewById(R.id.regionesSpinner);
        mComunasSpinner = (Spinner) findViewById(R.id.comunasSpinner);
        estadoComunas = 0;
        existeFotoPerfil = 0;
        tieneFotoDePerfil = 0;
        borrarFotoPerfil = 0;
        mborrarFotoPerfil.setVisibility(View.INVISIBLE);
        longitudLatitudEstado = 0;
        mUbicacion = (Button) findViewById(R.id.ubicacionButton);
        mUbicacion.setVisibility(View.VISIBLE);
        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Editar cuenta");
        }

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid(); //aca obtiene el uid de la cuenta, para la ropa abria que hacer esto, cada ropa tiene su uid y linkearla de alguna forma.
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        getUserInfo();

        llenarComboBoxRegiones();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLocation = true;
                showPlacePicker();
                /*
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    addLocation = true;
                    startActivityForResult(builder.build(Account.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }


                 */
                /*
                if (ActivityCompat.checkSelfPermission(Account.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(Account.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }

                 */
            }


        });


        mborrarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultUri != null) {
                    final Uri imageUri = null;
                    resultUri = imageUri;
                    borrarFotoPerfil=1;
                    mProfileImage.setImageResource(R.drawable.sinfotodeperfil);
                    mborrarFotoPerfil.setVisibility(View.INVISIBLE);
                }
                if (existeFotoPerfil == 1) {
                    borrarFotoPerfil=1;
                    mProfileImage.setImageResource(R.drawable.sinfotodeperfil);
                    mborrarFotoPerfil.setVisibility(View.INVISIBLE);
                }
            }
        });


        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(Account.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Account.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                }

            }
        });


        mAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mNombre.getText().toString().trim().length() <= 20){
                    saveUserInfo();
                }
                else{
                    Toast.makeText(Account.this, "Nombre sobrepasa el limite de carateres.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        mRegionesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String nombreRegion = mRegionesSpinner.getSelectedItem().toString();
                if(!nombreRegion.equals("Seleccione Región")) {

                    final String regionAnterior;
                    //txt_region.setText(nombreRegion);

                    String jsonFileString = jsonLector.getJsonFromAssets(getApplicationContext(), "cities.json");
                    Log.i("data", jsonFileString);

                    Gson gson = new Gson();
                    Type listUserType = new TypeToken<List<cities>>() {
                    }.getType();

                    List<cities> cities = gson.fromJson(jsonFileString, listUserType);

                    List<String> list = new ArrayList<String>();

                    if (estadoComunas == 1) {
                        int posicionComuna = 0;
                        List<String> comunas = cities.get(position - 1).getComunas(); //esto esta bien, lo revise con toast y posicionregion-1 corresponde a lo que buscamos.
                        //Toast.makeText(Account.this,cities.get(posicionRegion-1).region,Toast.LENGTH_SHORT).show();

                        for (int x = 0; x < comunas.size(); x++) {
                            if (comunas.get(x).equals(comunaAnterior)) {
                                posicionComuna = x;
                                break;
                            }
                        }

                        //Toast.makeText(Account.this, posicionComuna, Toast.LENGTH_SHORT).show();
                        ArrayAdapter<String> arrayAdapterComunas = new ArrayAdapter<>(Account.this, android.R.layout.simple_expandable_list_item_1, comunas);
                        arrayAdapterComunas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mComunasSpinner.setAdapter(arrayAdapterComunas);
                        mComunasSpinner.setSelection(posicionComuna);
                    } else if (position != 0) {
                        List<String> comunas = cities.get(position - 1).getComunas();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, comunas);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mComunasSpinner.setAdapter(adapter);
                    } else {
                        List<String> listVacia = new ArrayList<String>();
                        listVacia.add("Seleccione Comuna");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, listVacia);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                        mComunasSpinner.setAdapter(adapter);
                    }
                }
                else{
                    List<String> listVacia = new ArrayList<String>();
                    listVacia.add("Seleccione Comuna");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, listVacia);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mComunasSpinner.setAdapter(adapter);
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //aca hay que añadir que cuando no seleccione nada, se borre el spinner de comunas y solo deje seleccione comuna.
            }
        });

    }

    private void showPlacePicker() {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("YOUR_ANDROID_API_KEY")
                .setMapsApiKey("YOUR_MAPS_API_KEY");

        // If you want to set a initial location rather then the current device location.
        // NOTE: enable_nearby_search MUST be true.
        // builder.setLatLng(new LatLng(37.4219999, -122.0862462))

        try {
            Intent placeIntent = builder.build(this);
            startActivityForResult(placeIntent, REQUEST_PLACE_PICKER);
        }
        catch (Exception ex) {
            // Google Play services is not available...
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(Account.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLatitude(), 1);
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                        Log.d("latitud","latitud "+latitude+" longitude "+longitude);
                        longitudLatitudEstado=1;
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void saveUserInfo() {
        String userId = mAuth.getCurrentUser().getUid();
        String regionGuardar = String.valueOf(mRegionesSpinner.getSelectedItem());
        String comunaGuardar = String.valueOf(mComunasSpinner.getSelectedItem());


        DatabaseReference currentUserName = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("nameUser"); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo
        //mRegionesSpinner.getSelectedItem();
        if(regionGuardar != "Seleccione Región" && comunaGuardar != "Seleccione Comuna" && mNombre.getText().toString() != ""){
            DatabaseReference currentUserRegion = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("region"); //busca al usuario que va a crear y lo guarda como una variable que se le agregan las cosas y se manda al a db de nuevo
            currentUserRegion.setValue(regionGuardar);
            DatabaseReference currentUserComuna = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("comuna");
            currentUserComuna.setValue(comunaGuardar);
            currentUserName.setValue(mNombre.getText().toString()); //Aca va y le asigna el nombre al User.
            //aca guarda la latitud y longitud.
            if(puntuacionGeneral != null) {
                if (puntuacionGeneral.equals("-1")) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("puntuacionGeneral").setValue("-1");
                }
            }
            if(longitudLatitudEstado==1) {
                DatabaseReference currentUserLatitude = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("latitudeVenta");
                currentUserLatitude.setValue(latitude);
                DatabaseReference currentUserLongitude = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("longitudeVenta");
                currentUserLongitude.setValue(longitude);
            }


            //aca lo de la imagen

            if(resultUri != null){
                final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
                Bitmap bitmap = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }


//            nsfwDetector.isNSFW(bitmap,confidenceThreshold)






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

                // codigo para detectar fotos sexuales.
                float confidenceThreshold = (float) 0.68; //radio de margen, mientras mas cercano al 1 permite foto mas nsfw.
                NSFWDetector.INSTANCE.isNSFW(bitmap,confidenceThreshold, (isNSFW, confidence, image) -> {
                    if (isNSFW){
                        Toast.makeText(this, "FOTO NO PERMITIDA.", Toast.LENGTH_SHORT).show();
                        borrarFotoPerfil= 0;
                    } else {
                        //Toast.makeText(this, "SFW with confidence: " + confidence, Toast.LENGTH_SHORT).show();
                        dialog = new ProgressDialog(Account.this);
                        dialog.setMessage("Cargando...");
                        dialog.show();

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map newImage = new HashMap();

                                        newImage.put("profileImageUrl", uri.toString());
                                        mCustomerDatabase.updateChildren(newImage);
                                        dialog.dismiss();
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
                    return kotlin.Unit.INSTANCE;
                });




            }
            else { //este if significa que no subio nada, entonces ve si antes era nulo, si lo era lo borra.
                mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) { //si existe y tiene algo ya guardado dentro lo muestra, para eso lo trae y lo castea al mapa.
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if (map.get("profileImages") == null ) {
                                String userId = mAuth.getCurrentUser().getUid();
                                if(dataSnapshot.hasChild("profileImageUrl")) {
                                    //FirebaseStorage.getInstance().getReference().child("profileImages").child(dataSnapshot.child("profileImageUrl").getValue().toString()).delete();
                                    FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("profileImages").getStorage();
                                    StorageReference photo = mFirebaseStorage.getReferenceFromUrl(dataSnapshot.child("profileImageUrl").getValue().toString());
                                    photo.delete();
                                    if(borrarFotoPerfil==1) {
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("profileImageUrl").removeValue();
                                    }

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            Toast.makeText(Account.this, "Guardado Con Exito", Toast.LENGTH_SHORT).show();


        }
        else{
            Toast.makeText(Account.this, "Datos Incorrectos.", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            mborrarFotoPerfil.setVisibility(View.VISIBLE);
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
        }

        if(requestCode==REQUEST_PLACE_PICKER && resultCode == Activity.RESULT_OK){

            if(addLocation == true) {
                com.google.android.libraries.places.api.model.Place place = PingPlacePicker.getPlace(data);
                if(place!=null) {
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    longitudLatitudEstado = 1;
                }
            }
        }

    }

    private void llenarComboBoxRegiones() {
        String jsonFileString = jsonLector.getJsonFromAssets(getApplicationContext(), "cities.json");
        Log.i("data", jsonFileString);
        int posicionRegion = 0;
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<cities>>() {
        }.getType();

        List<cities> cities = gson.fromJson(jsonFileString, listUserType);

        List<String> list = new ArrayList<String>();
        list.add("Seleccione Región");
        for (int i = 0; i < cities.size(); i++) {
            list.add(cities.get(i).region);
            if (cities.get(i).region.equals(regionAnterior)) {
                posicionRegion = i+1;
            }
        }
        //Toast.makeText(this, regionAnterior,Toast.LENGTH_LONG).show();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRegionesSpinner.setAdapter(dataAdapter);
        mRegionesSpinner.setSelection(posicionRegion);


    }

    private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) { //si existe y tiene algo ya guardado dentro lo muestra, para eso lo trae y lo castea al mapa.a
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(dataSnapshot.hasChild("puntuacionGeneral")){
                        puntuacionGeneral = dataSnapshot.child("puntuacionGeneral").getValue().toString();
                    }
                    else{
                        puntuacionGeneral = "-1";
                    }
                    if (map.get("nameUser") != null) {
                        nombreUsuario = map.get("nameUser").toString();
                        mNombre.setText(nombreUsuario);
                    }

                    if (map.get("latitudeVenta") != null) {
                        latitude = Double.parseDouble(map.get("latitudeVenta").toString());
                        longitudLatitudEstado =1;
                    }

                    if (map.get("longitudeVenta") != null) {
                        longitude = Double.parseDouble(map.get("longitudeVenta").toString());
                        longitudLatitudEstado =1;
                    }

                    if (map.get("region") != null) {
                        regionAnterior = map.get("region").toString();
                        comunaAnterior = map.get("comuna").toString();
                        ArrayList<String> regiones = new ArrayList<>();
                        regiones.add(regionAnterior);

                        String jsonFileString = jsonLector.getJsonFromAssets(getApplicationContext(), "cities.json");
                        Log.i("data", jsonFileString);
                        int posicionRegion = 0;
                        Gson gson = new Gson();
                        Type listUserType = new TypeToken<List<cities>>() {
                        }.getType();

                        List<cities> cities = gson.fromJson(jsonFileString, listUserType);

                        List<String> list = new ArrayList<String>();
                        list.add("Seleccione Región");
                        for (int i = 0; i < cities.size(); i++) {
                            list.add(cities.get(i).region);
                            if (cities.get(i).region.equals(regionAnterior)) {
                                posicionRegion = i+1;
                            }
                        }
                        //Toast.makeText(this, regionAnterior,Toast.LENGTH_LONG).show();
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Account.this, android.R.layout.simple_expandable_list_item_1,list);
                        mRegionesSpinner.setAdapter(arrayAdapter);
                        mRegionesSpinner.setSelection(posicionRegion);
                        estadoComunas=1;
                    }


//esto de aca es para cargar la foto de perfil del usuario
                    Glide.with(getApplication()).clear(mProfileImage);
                    if(map.get("profileImageUrl")!=null){
                        mborrarFotoPerfil.setVisibility(View.VISIBLE);
                        tieneFotoDePerfil = 1;
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                //Picasso.get().setLoggingEnabled(true);
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mProfileImage);
                                //Picasso.get().load(R.mipmap.ic_launcher).fit().centerCrop().into(mProfileImage);
                                break;
                            default:
                                //Picasso.get().setLoggingEnabled(true);
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                //Picasso.get().load(profileImageUrl).fit().centerCrop().into(mProfileImage);
                                break;

                        }
                        //Picasso.get().setLoggingEnabled(true);
                        Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                        //Picasso.get().load(profileImageUrl).fit().centerCrop().into(mProfileImage);
                        existeFotoPerfil =1;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed()
    {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    //toolbar
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), VerMiCuenta.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}