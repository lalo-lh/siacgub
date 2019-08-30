package com.example.francisco.sql;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ObFragment extends Fragment {
    private ArrayList<Estudiante> itemArrayList;  //List items Array
    private ObFragment.MyAppAdapter myAppAdapter; //Array Adapter
    private ListView listView; // Listview
    private boolean success = false; // boolean


    public ObFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ob, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        listView = (ListView) getView().findViewById(R.id.listob);
        itemArrayList = new ArrayList<Estudiante>(); // Arraylist Initialization
        ObFragment.SyncData orderData = new ObFragment.SyncData();
        orderData.execute("");
    }

    @Override
    public void onResume(){
        super.onResume();
        ((Main2Activity) getActivity()).setActionBarTitle("OBRAS");
    }

    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String>
    {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() //Starts the progress dailog
        {
            progress = ProgressDialog.show(getActivity(), "Cargando",
                    "Cargando datos! por favor Espere...", true);
        }

        @Override
        protected String doInBackground(String... strings)  // Connect to the database, write query and add items to array list
        {
            String u = getActivity().getIntent().getStringExtra("user");
            String p = getActivity().getIntent().getStringExtra("pass");
            try
            {
                Connection conn = ConnectionSql.ConnectionHelper(u,p);
                if (conn == null)
                {
                    success = false;
                }
                else {
                    // Change below query according to your own database.
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("select * from estudiante");
                    if (rs != null) // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new Estudiante(rs.getString("Nombre"),rs.getString("Domicilio"),rs.getString("Telefono"),rs.getInt("Grado")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No se encontraron datos!";
                        success = false;
                    }
                }
            } catch (Exception e)
            {
                Toast.makeText(getActivity(),
                        e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) // disimissing progress dialoge, showing error and setting up my listview
        {
            progress.dismiss();
            //Toast.makeText(ResultActivity.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter = new ObFragment.MyAppAdapter(itemArrayList, getActivity());
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listView.setAdapter(myAppAdapter);
                } catch (Exception ex)
                {

                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter         //has a class viewholder which holds
    {
        public class ViewHolder
        {
            TextView nombre; //findViewById(R.id.txtnom);
            TextView domicilio; //findViewById(R.id.txtdom);
            TextView telefono;//findViewById(R.id.txttel);
            TextView grado; //.findViewById(R.id.txtgrad);
        }

        public List<Estudiante> parkingList;

        public Context context;
        ArrayList<Estudiante> arraylist;

        private MyAppAdapter(List<Estudiante> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<Estudiante>();
            arraylist.addAll(parkingList);
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) // inflating the layout and initializing widgets
        {

            View rowView = convertView;
            ObFragment.MyAppAdapter.ViewHolder viewHolder= null;
            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.table_obr, parent, false);
                viewHolder = new ObFragment.MyAppAdapter.ViewHolder();
                viewHolder.nombre = (TextView) rowView.findViewById(R.id.txtob0);
                viewHolder.domicilio = (TextView) rowView.findViewById(R.id.txtob1);
                viewHolder.telefono = (TextView) rowView.findViewById(R.id.txtob2);
                viewHolder.grado = (TextView) rowView.findViewById(R.id.txtob3);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ObFragment.MyAppAdapter.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.nombre.setText(parkingList.get(position).getNombre()+"");
            viewHolder.domicilio.setText(parkingList.get(position).getDomicilio()+"");
            viewHolder.telefono.setText(parkingList.get(position).getTelefono()+"");
            viewHolder.grado.setText(parkingList.get(position).getGrado()+"");

            return rowView;
        }
    }
}
