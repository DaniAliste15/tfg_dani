package com.dani.firedetection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.math.BigDecimal;
import java.math.MathContext;

public class DatosIncendioActivity extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    public TextView tvCoord;
    private Spinner spinnerHumo,spinnerVegetacion,spinnerColumna;
    private Button enviarAviso;
    public String humo,vegetacion,columna;
    private Double latitud,longitud;
    public BigDecimal latiRecortada,longiRecortada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        recibirDatos();

        mAuth = FirebaseAuth.getInstance();

        spinnerHumo = (Spinner) findViewById(R.id.spiHumo);
        spinnerVegetacion = (Spinner) findViewById(R.id.spiVegetacion);
        spinnerColumna = (Spinner) findViewById(R.id.spiColumna);
        enviarAviso = (Button) findViewById(R.id.enviarAviso);

        //HUMO
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.humos,R.layout.spinner_modificacion);
        spinnerHumo.setAdapter(adapter);

        spinnerHumo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(), "Seleccion:"+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                humo = parent.getItemAtPosition(position).toString();
                //Toast.makeText(DatosIncendioActivity.this, ""+humo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //FIN HUMO

        //VEGETACION
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.vegetacion,R.layout.spinner_modificacion);
        spinnerVegetacion.setAdapter(adapter1);

        spinnerVegetacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(), "Seleccion:"+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                vegetacion = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //FIN VEGETACION

        //COLUMNA
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.columna,R.layout.spinner_modificacion);
        spinnerColumna.setAdapter(adapter2);

        spinnerColumna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(), "Seleccion:"+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                columna = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //FIN COLUMNA

        enviarAviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(humo.equals("Seleccione") || vegetacion.equals("Seleccione") || columna.equals("Seleccione")) {
                    Toast.makeText(DatosIncendioActivity.this, "Debe seleccionar alguna opcion", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent datos = new Intent(DatosIncendioActivity.this,ValidacionActivity.class);
                    datos.putExtra("humo",humo);
                    datos.putExtra("vegetacion",vegetacion);
                    datos.putExtra("columna",columna);
                    datos.putExtra("latitud",latitud);
                    datos.putExtra("longitud",longitud);
                    startActivity(datos);
                }
                
            }
        });


    }

    private void recibirDatos() {
        tvCoord = (TextView) findViewById(R.id.tvCoord);
        Bundle extras = getIntent().getExtras();
        latitud = extras.getDouble("lati");
        longitud = extras.getDouble("longi");

        /************Ajuste de deciamles***********/
        BigDecimal b1 = new BigDecimal(latitud);
        BigDecimal b2 = new BigDecimal(longitud);
        MathContext m = new MathContext(8);
        /*******************************************/

        latiRecortada = b1.round(m);
        longiRecortada = b2.round(m);

        tvCoord.setText(latiRecortada+" / "+longiRecortada);
    }

    public boolean onCreateOptionsMenu(Menu mimenu) {
          getMenuInflater().inflate(R.menu.menu_en_activity,mimenu);
          return true;
    }

    public boolean onOptionsItemSelected(MenuItem opcion_menu) {
        int id = opcion_menu.getItemId();
        if(id == R.id.infoDatos){
            startActivity(new Intent(DatosIncendioActivity.this, InfoDatosIncendioActivity.class));
            return true;
        }

        if(id == R.id.salir){
            AlertDialog.Builder alerta = new AlertDialog.Builder(DatosIncendioActivity.this);
            alerta.setMessage("??Desea cerrar sesion?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            startActivity(new Intent(DatosIncendioActivity.this,LoginActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("Atenci??n");
            titulo.show();

            return true;
        }

        return super.onOptionsItemSelected(opcion_menu);
    }
}
