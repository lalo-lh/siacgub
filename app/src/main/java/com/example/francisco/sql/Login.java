package com.example.francisco.sql;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends AppCompatActivity {
    EditText editUsr, editPass;
    Button btnLog;
    CheckBox chklog;
    public static final String MY_PREFERENCES = "MY_PREFERENCES_FILE_KEY";
    public static final String USERNAME = "usuario";
    public static final String PASSWORD = "contrasena";
    public static final Boolean MANTENER = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsr = findViewById(R.id.EditUsr);
        editPass = findViewById(R.id.EditPass);
        btnLog = findViewById(R.id.btnLog);
        chklog = findViewById(R.id.chekLog);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(USERNAME,null);
        String password = sharedPreferences.getString(PASSWORD,null);
        final Boolean siniciada = sharedPreferences.getBoolean("sesion_iniciada", false);

        if (siniciada.equals(true)){
            if(validUsr(username,password)) {
                Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                i.putExtra("user", username);
                i.putExtra("pass", password);
                startActivity(i);
            }
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().apply();
        }

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User = editUsr.getText().toString();
                String Pass = editPass.getText().toString();

                if(validUsr(User,Pass)){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(USERNAME,User);
                    editor.putString(PASSWORD,Pass);
                    editor.putBoolean("sesion_iniciada",chklog.isChecked());
                    editor.apply();
                    //editor.clear().commit();

                    Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                    i.putExtra("user",User);
                    i.putExtra("pass",Pass);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(),"Datos de acceso invalidos",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean validUsr(String u, String p){
        Connection connect = ConnectionSql.ConnectionHelper(u,p);
        if(connect==null){
            return false;
        } else {
            return true;
        }
    }
}
