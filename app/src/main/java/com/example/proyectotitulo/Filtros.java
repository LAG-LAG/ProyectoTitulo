package com.example.proyectotitulo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Filtros extends AppCompatActivity {
    private DatabaseReference mCustomerDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private Spinner mRegionesSpinner,mComunasSpinner,mTalla,mTipoPrenda,mEstado;
    private String comunaBusqueda, tallaBusqueda, estadoBusqueda, tipoPrendaBusqueda,regionBusqueda,currentUId;
    private int estadoComunas,valorKm;
    private Button mAplicar;
    private Switch mSwitch;
    private TextView KmSeekBar;
    private boolean isChecked;
    private SeekBar mSeekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUId = user.getUid();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users"); //esto obtiene todos los usuarios de la bd
        mRegionesSpinner = (Spinner) findViewById(R.id.regionesSpinnerFiltros);
        mComunasSpinner = (Spinner) findViewById(R.id.comunasSpinnerFiltros);
        mTalla = (Spinner) findViewById(R.id.TallaSpinnerFiltrar);
        mTipoPrenda = (Spinner) findViewById(R.id.tipoPrendaSpinnerFiltrar);
        mEstado = (Spinner) findViewById(R.id.estadoSpinnerFiltrar);
        mAplicar = (Button) findViewById(R.id.filtrarDatosBtn);
        mSwitch = (Switch) findViewById(R.id.switch2);
        KmSeekBar = (TextView) findViewById(R.id.textProgressSeek);
        mSeekbar = (SeekBar) findViewById(R.id.seekBar);
        mSeekbar.setProgress(0);
        mSeekbar.setMax(100);
        estadoComunas = 1;
        comunaBusqueda = getIntent().getExtras().getString("comunaAnterior");
        tallaBusqueda = getIntent().getExtras().getString("tallaAnterior");
        tipoPrendaBusqueda = getIntent().getExtras().getString("tipoPrendaAnterior");
        regionBusqueda = getIntent().getExtras().getString("regionAnterior");
        estadoBusqueda = getIntent().getExtras().getString("estadoAnterior");

        isChecked = mSwitch.isChecked(); //ESTO HAY QUE CAMBIARLO POR EL VALOR DEL SWITCH EN LA BD.
        obtenerValoresSeekbar();
        if(isChecked == false) {
            mSwitch.setText("Busqueda por Km.");
            getFiltroInfo();
            getSpinnerDatos();
            llenarComboBoxRegiones();
            mSeekbar.setVisibility(View.INVISIBLE);
            KmSeekBar.setVisibility(View.INVISIBLE);
            mTalla.setVisibility(View.VISIBLE);
            mTipoPrenda.setVisibility(View.VISIBLE);
            mEstado.setVisibility(View.VISIBLE);
            mRegionesSpinner.setVisibility(View.VISIBLE);
            mComunasSpinner.setVisibility(View.VISIBLE);
        }
        else{

            mSeekbar.setProgress(0); // esto hay que reemplazarlo por el valor en la bd.

            mSwitch.setText("Busqueda por Ciudad.");

            mSeekbar.setVisibility(View.VISIBLE);
            KmSeekBar.setVisibility(View.VISIBLE);
            mTalla.setVisibility(View.INVISIBLE);
            mTipoPrenda.setVisibility(View.INVISIBLE);
            mEstado.setVisibility(View.INVISIBLE);
            mRegionesSpinner.setVisibility(View.INVISIBLE);
            mComunasSpinner.setVisibility(View.INVISIBLE);
        }
        //Toast.makeText(this, regionBusqueda, Toast.LENGTH_SHORT).show();

        //Toolbar Menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Filtros de Busqueda");
        }
/*
        getFiltroInfo();
        getSpinnerDatos();
        llenarComboBoxRegiones();

*/

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                KmSeekBar.setText(String.valueOf(progress)+" KM.");
                valorKm = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked = b;
                if(isChecked == false){
                    mSwitch.setText("Busqueda por Km.");
                    Log.d("switchh", "xd");
                    getFiltroInfo();
                    getSpinnerDatos();
                    llenarComboBoxRegiones();
                    mSeekbar.setVisibility(View.INVISIBLE);
                    KmSeekBar.setVisibility(View.INVISIBLE);

                    mTalla.setVisibility(View.VISIBLE);
                    mTipoPrenda.setVisibility(View.VISIBLE);
                    mEstado.setVisibility(View.VISIBLE);
                    mRegionesSpinner.setVisibility(View.VISIBLE);
                    mComunasSpinner.setVisibility(View.VISIBLE);
                }
                else{
                     //esto hay que reemplazarlo por el valor en al bd.
                    mSeekbar.setVisibility(View.VISIBLE);
                    KmSeekBar.setVisibility(View.VISIBLE);
                    mSwitch.setText("Busqueda por Ciudad.");
                    //mTalla.setVisibility(View.INVISIBLE);
                    //mTipoPrenda.setVisibility(View.INVISIBLE);
                    //mEstado.setVisibility(View.INVISIBLE);
                    mRegionesSpinner.setVisibility(View.INVISIBLE);
                    mComunasSpinner.setVisibility(View.INVISIBLE);
                }
            }
        });

        mRegionesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String nombreRegion = mRegionesSpinner.getSelectedItem().toString();
                final String regionAnterior;
                //txt_region.setText(nombreRegion);

                String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cities.json");
                Log.i("data", jsonFileString);

                Gson gson = new Gson();
                Type listUserType = new TypeToken<List<cities>>() {
                }.getType();

                List<cities> cities = gson.fromJson(jsonFileString, listUserType);

                List<String> list = new ArrayList<String>();

                if (estadoComunas == 1) {
                    int posicionComuna = 0;
                    List<String> comunas = cities.get(position).getComunas(); //esto esta bien, lo revise con toast y posicionregion-1 corresponde a lo que buscamos.
                    if(!comunaBusqueda.equals("")){
                        for (int x = 0; x < comunas.size(); x++) {
                            if (comunas.get(x).equals(comunaBusqueda)) {
                                posicionComuna = x;
                                break;
                            }
                        }

                    }
                    ArrayAdapter<String> arrayAdapterComunas = new ArrayAdapter<>(Filtros.this, android.R.layout.simple_expandable_list_item_1, comunas);
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


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //aca hay que añadir que cuando no seleccione nada, se borre el spinner de comunas y solo deje seleccione comuna.
            }
        });


                mAplicar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isChecked==false) {
                            String regionGuardar = String.valueOf(mRegionesSpinner.getSelectedItem());
                            String comunaGuardar = String.valueOf(mComunasSpinner.getSelectedItem());
                            String tipoPrendaAnterior = String.valueOf(mTipoPrenda.getSelectedItem());
                            String estadoAnterior = String.valueOf(mEstado.getSelectedItem());
                            String tallaAnterior = String.valueOf(mTalla.getSelectedItem());
                            if (tipoPrendaAnterior.equals("Seleccione tipo de prenda")) {
                                tipoPrendaAnterior = "";
                            }
                            if (estadoAnterior.equals("Seleccione estado")) {
                                estadoAnterior = "";
                            }
                            if (tallaAnterior.equals("Seleccione talla")) {
                                tallaAnterior = "";
                            }
                            usersDb.child(mAuth.getCurrentUser().getUid()).child("filtros").child("tipoBusqueda").setValue("0"); //si es 0 busca por comuna y direccion.
                            usersDb.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("filtros").child("comunaAnterior").setValue(comunaGuardar);
                            usersDb.child(mAuth.getCurrentUser().getUid()).child("filtros").child("regionAnterior").setValue(regionGuardar);
                            usersDb.child(mAuth.getCurrentUser().getUid()).child("filtros").child("tipoPrendaAnterior").setValue(tipoPrendaAnterior);
                            usersDb.child(mAuth.getCurrentUser().getUid()).child("filtros").child("estadoAnterior").setValue(estadoAnterior);
                            usersDb.child(mAuth.getCurrentUser().getUid()).child("filtros").child("tallaAnterior").setValue(tallaAnterior);
                            Intent intentPaginaPrincipal = new Intent(Filtros.this, PaginaPrincipal.class);/*
                        intentPaginaPrincipal.putExtra("comunaAnterior",comunaGuardar);
                        intentPaginaPrincipal.putExtra("regionAnterior",regionGuardar);
                        intentPaginaPrincipal.putExtra("tipoPrendaAnterior",tipoPrendaAnterior);
                        intentPaginaPrincipal.putExtra("estadoAnterior",estadoAnterior);
                        intentPaginaPrincipal.putExtra("tallaAnterior",tallaAnterior);*/


                            startActivity(intentPaginaPrincipal);
                            finish();
                            return;
                        }
                        else{
                            String tipoPrendaAnterior = String.valueOf(mTipoPrenda.getSelectedItem());
                            String estadoAnterior = String.valueOf(mEstado.getSelectedItem());
                            String tallaAnterior = String.valueOf(mTalla.getSelectedItem());
                            if (tipoPrendaAnterior.equals("Seleccione tipo de prenda")) {
                                tipoPrendaAnterior = "";
                            }
                            if (estadoAnterior.equals("Seleccione estado")) {
                                estadoAnterior = "";
                            }
                            if (tallaAnterior.equals("Seleccione talla")) {
                                tallaAnterior = "";
                            }
                            usersDb.child(mAuth.getCurrentUser().getUid()).child("filtros").child("tipoBusqueda").setValue("1"); //si es uno busca por km.
                            usersDb.child(mAuth.getCurrentUser().getUid()).child("filtros").child("KMBusqueda").setValue(valorKm);
                            usersDb.child(mAuth.getCurrentUser().getUid()).child("filtros").child("tipoPrendaAnterior").setValue(tipoPrendaAnterior);
                            usersDb.child(mAuth.getCurrentUser().getUid()).child("filtros").child("estadoAnterior").setValue(estadoAnterior);
                            usersDb.child(mAuth.getCurrentUser().getUid()).child("filtros").child("tallaAnterior").setValue(tallaAnterior);
                            Intent intentPaginaPrincipal = new Intent(Filtros.this, PaginaPrincipal.class);
                            startActivity(intentPaginaPrincipal);
                            finish();
                            return;
                        }
                    }
                });
    }

    private void obtenerValoresSeekbar() {
        Log.d("switchhh","xd'");
        DatabaseReference usersDb2 = FirebaseDatabase.getInstance().getReference().child("Users");
        usersDb2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("switchhh","xd0,5");
                if(dataSnapshot.exists() && dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid())){
                    Log.d("switchhh","xd0.8");
                    if(dataSnapshot.hasChild("filtros")){
                        Log.d("switchhh","xd1");
                        if(dataSnapshot.child("filtros").hasChild("KMBusqueda") ){
                            valorKm = Integer.valueOf(dataSnapshot.child("filtros").child("KMBusqueda").getValue().toString());
                            //valorKm = (int) dataSnapshot.child("filtros").child("KMBusqueda").getValue();
                            Log.d("switchhh","xd2");
                            mSeekbar.setProgress(valorKm);
                            KmSeekBar.setText(String.valueOf(valorKm)+" KM.");
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getSpinnerDatos() {

        Adapter adapter = mTalla.getAdapter();
        int n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            String elementoSpinner = (String) adapter.getItem(i);
            if (elementoSpinner.equals(tallaBusqueda)) {
                mTalla.setSelection(i);
            }
        }

        adapter = mTipoPrenda.getAdapter();
        n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            String elementoSpinner = (String) adapter.getItem(i);
            if (elementoSpinner.equals(tipoPrendaBusqueda)) {
                mTipoPrenda.setSelection(i);
            }
        }

        adapter = mEstado.getAdapter();
        n = adapter.getCount();
        for (int i = 0; i < n; i++) {
            String elementoSpinner = (String) adapter.getItem(i);
            if (elementoSpinner.equals(estadoBusqueda)){
                mEstado.setSelection(i);
            }
        }

    }

    private void llenarComboBoxRegiones() {
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cities.json");
        Log.i("data", jsonFileString);
        int posicionRegion = 0;
        Log.d("weaweawea",""+posicionRegion);
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<cities>>() {
        }.getType();
        List<cities> cities = gson.fromJson(jsonFileString, listUserType);
        List<String> list = new ArrayList<String>();
        //if(!regionBusqueda.equals("")){
           for (int i = 0; i < cities.size(); i++) {
                list.add(cities.get(i).region);
                if (cities.get(i).region.equals(regionBusqueda)) {
                    posicionRegion = i ;
                }
          //  }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Filtros.this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRegionesSpinner.setAdapter(dataAdapter);
        mRegionesSpinner.setSelection(posicionRegion);
        //
    }

    private void getFiltroInfo() {
        if (!regionBusqueda.equals("")) {
            ArrayList<String> regiones = new ArrayList<>();
            regiones.add(regionBusqueda);
            String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cities.json");
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
                if (cities.get(i).region.equals(regionBusqueda)) {
                    posicionRegion = i+1;
                }
            }
            //Toast.makeText(this, regionAnterior,Toast.LENGTH_LONG).show();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Filtros.this, android.R.layout.simple_expandable_list_item_1,list);
            mRegionesSpinner.setAdapter(arrayAdapter);
            mRegionesSpinner.setSelection(posicionRegion);
            estadoComunas=1;
        }
        else{
            ArrayList<String> regiones = new ArrayList<>();
            //regiones.add(regionBusqueda);
            String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "cities.json");
            Log.i("data", jsonFileString);
            Gson gson = new Gson();
            Type listUserType = new TypeToken<List<cities>>() {
            }.getType();
            List<cities> cities = gson.fromJson(jsonFileString, listUserType);
            List<String> list = new ArrayList<String>();
            list.add("Seleccione Región");
            for (int i = 0; i < cities.size(); i++) {
                list.add(cities.get(i).region);
            }
            //Toast.makeText(this, regionAnterior,Toast.LENGTH_LONG).show();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Filtros.this, android.R.layout.simple_expandable_list_item_1,list);
            mRegionesSpinner.setAdapter(arrayAdapter);
            mRegionesSpinner.setSelection(1);
            estadoComunas=1;
        }
    }




    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), PaginaPrincipal.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}