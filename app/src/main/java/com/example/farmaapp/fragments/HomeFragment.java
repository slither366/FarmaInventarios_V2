package com.example.farmaapp.fragments;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farmaapp.R;
import com.example.farmaapp.db.AppDatabase;
import com.example.farmaapp.util.Constantes;
import com.example.farmaapp.util.FileUtils;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View v;
    AppDatabase database;
    TextView tv_cantTotInventariado;
    int totalProdInventariado;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);

        inicializarUI();

        database = Room.databaseBuilder(getContext(), AppDatabase.class, Constantes.DB_NAME)
                .allowMainThreadQueries()
                .build();

        importarDB();

        int a = this.getProdInventario();

        this.tv_cantTotInventariado.setText(a+"");

        return v;
    }

    private void inicializarUI() {
        this.tv_cantTotInventariado = v.findViewById(R.id.tv_cantTotInventariado);
    }

    private int getProdInventario() {
        return this.database.getProductoInventarioDao().getCount();
    }


    private void importarDB(){
        // sd: "/storage/emulated/0"
        File sd = Environment.getExternalStorageDirectory();
        // data: "/data"
        File data = Environment.getDataDirectory();

        // com.example.farmaapp
        String packageName = v.getContext().getPackageName();

        // /data/com.example.farmaapp/databases/eckerd
        String internalDBPath = "/data/" + packageName + "/databases/" + Constantes.DB_NAME;
        String externaDCIMDB = "/DCIM/" + Constantes.DB_NAME;

        File externalFileDB = new File(sd,externaDCIMDB);
        File moveToDirectory = new File(data, internalDBPath);

        try {
            FileUtils.copiarArchivo(externalFileDB,moveToDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
