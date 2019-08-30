package com.example.francisco.sql;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.francisco.sql.R.layout.spinner_item_changed;

public class MainActivity extends AppCompatActivity {
    TextView txtview, txtview1, txtview2, txtview3;
    EditText EditText;
    Button btnBuscar, btnMap;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>SIACGUB</font>"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtview = findViewById(R.id.txtview);
        txtview1 = findViewById(R.id.txtview1);
        txtview2 = findViewById(R.id.txtview2);
        txtview3 = findViewById(R.id.txtview3);
        EditText = findViewById(R.id.EditText);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnMap = findViewById(R.id.btnMap);

        spin = findViewById(R.id.spinner);

        ArrayList<String> dato = ObtenerId();

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User = getIntent().getStringExtra("user");
                String Pass = getIntent().getStringExtra("pass");

                Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                i.putExtra("user",User);
                i.putExtra("pass",Pass);
                startActivity(i);
            }
        });

        //********************************************Buscar*****************************************************
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = EditText.getText().toString();
                //Toast.makeText(getApplicationContext(),res, Toast.LENGTH_SHORT).show();
                ArrayList<String> dato = ObtenerDatosXNombre(res);

                //------------------------------------Spinner----------------------------------------------
                spin.setAdapter(new ArrayAdapter<String>(MainActivity.this,spinner_item_changed, dato));

                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
                    {
                        //Toast.makeText(adapterView.getContext(),(String) adapterView.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                        String st = (String)adapterView.getItemAtPosition(pos);
                        //Toast.makeText(adapterView.getContext(),st, Toast.LENGTH_SHORT).show();
                        if(!st.equals("Selecciona")) {
                            ObtenerDatos(st);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {    }
                });
                //----------------------------------------END--------------------------------------------------


            }
        });
        //********************************************Buscar*****************************************************

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                LogOut();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public ArrayList ObtenerId() {

        String u = getIntent().getStringExtra("user");
        String p = getIntent().getStringExtra("pass");

        ArrayList<String> ids = new ArrayList<>();
        try {
            Connection connect = ConnectionSql.ConnectionHelper(u,p);
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery("select * from estudiante");
            while (rs.next()){
                ids.add(rs.getString("id_estudiante"));
            }
            connect.close();
            return ids;
            //return true;
        } catch (SQLException e){
            Toast.makeText(getApplicationContext(),
                    e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            //return false;
            return ids;
        }
    }

    public boolean ObtenerDatos(String s) {
        String datosConsultado = "";
        String dato1 = "";
        String dato2 = "";
        String dato3 = "";
        String u = getIntent().getStringExtra("user");
        String p = getIntent().getStringExtra("pass");
        try {
            //Se obtiene la conexión
            Connection connect = ConnectionSql.ConnectionHelper(u,p);
            //Se genera la consulta
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery("select * from estudiante where id_estudiante = "+s);
            while (rs.next()) {

                //Se extraen los datos
                datosConsultado = rs.getString("Nombre");
                dato1 =rs.getString("Domicilio");
                dato2 =rs.getString("Telefono");
                dato3 =rs.getString("Grado");

                //Toast.makeText(getApplicationContext(),datosConsultado, Toast.LENGTH_SHORT).show();
            }
            //Se cierra la conexión
            connect.close();
            //Mostramos los datos obtenidos
            //Toast.makeText(getApplicationContext(),
                    //datosConsultado, Toast.LENGTH_SHORT).show();
            //String joined = TextUtils.join(",",compl);
            txtview.setText(datosConsultado);
            txtview1.setText(dato1);
            txtview2.setText(dato2);
            txtview3.setText(dato3);
            return  true;
        } catch (SQLException e) {
            //Mostramos el error en caso de no conectarse
            Toast.makeText(getApplicationContext(),
                    e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public ArrayList<String> ObtenerDatosXNombre(String s) {
        String consulta = "Select * From School.dbo.estudiante Where Nombre LIKE '"+s+"%'";
        ArrayList<String> ids = new ArrayList<>();
        String u = getIntent().getStringExtra("user");
        String p = getIntent().getStringExtra("pass");
        try {
            //Se obtiene la conexión
            Connection connect = ConnectionSql.ConnectionHelper(u,p);
            //Se genera la consulta
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                ids.add(rs.getString("id_estudiante"));
                //Se extraen los datos
            }
            //Se cierra la conexión
            connect.close();
            //Mostramos los datos obtenidos
            //String joined = TextUtils.join(",",compl);
            return  ids;
        } catch (SQLException e) {
            //Mostramos el error en caso de no conectarse
            Toast.makeText(getApplicationContext(),
                    e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            return ids;
        }
    }

    public void LogOut(){
        SharedPreferences sharedPreferences = getSharedPreferences("MY_PREFERENCES_FILE_KEY" ,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        finish();
    }
}
