package com.example.francisco.sql;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSql {


        public static Connection ConnectionHelper(String user,String pass) {
            String puvlica="200.44.166.238";
            String pubHome = "192.168.50.190";
            String lokal="10.0.11.35";
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection connection = null;
            String ConnectionURL = null;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnectionURL = "jdbc:jtds:sqlserver://"+pubHome+";port=1433;databaseName=School;user="+user+";password="+pass+";";
                connection = DriverManager.getConnection(ConnectionURL);
            } catch (SQLException se) {
                Log.e("ERROR", se.getMessage());
            } catch (ClassNotFoundException e) {
                Log.e("ERROR", e.getMessage());
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
            return connection;
        }
}
