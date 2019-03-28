package com.example.farmaapp.fragments;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.farmaapp.R;
import com.example.farmaapp.adapter.InventarioAdapter;
import com.example.farmaapp.db.AppDatabase;
import com.example.farmaapp.entity.ProductoBarra;
import com.example.farmaapp.entity.ProductoInventario;
import com.example.farmaapp.util.Constantes;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventarioFragment extends Fragment{

    RecyclerView rv_inventario;
    InventarioAdapter adapter;
    AppDatabase database;

    public InventarioFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inventario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv_inventario = view.findViewById(R.id.rv_inventario);

        database = Room.databaseBuilder(getContext(), AppDatabase.class, Constantes.DB_NAME)
                .allowMainThreadQueries()
                .build();

        List<ProductoBarra> list_prodBarra = database.getProductoBarraDao().getProductoBarra();

        List<ProductoInventario> list_prodInventario = (List<ProductoInventario>) database.getProductoInventarioDao().getProductoInventario();

        adapter = new InventarioAdapter(list_prodInventario);

        rv_inventario.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_inventario.setAdapter(adapter);



    }
/*
    private List<ProductoInventario> obtenerProductos(){
        List<ProductoInventario> productos = new ArrayList<>();
        ProductoInventario producto1 = new ProductoInventario("010645","0453",
                "N",0,2,0,"65");
        ProductoInventario producto2 = new ProductoInventario("010633","0453",
                "N",0,2,0,"65");
        ProductoInventario producto3 = new ProductoInventario("010642","0453",
                "N",0,2,0,"65");
        ProductoInventario producto4 = new ProductoInventario("010637","0453",
                "N",0,2,0,"65");
        ProductoInventario producto5 = new ProductoInventario("011372","0453",
                "N",0,2,0,"65");
        ProductoInventario producto6 = new ProductoInventario("426651","0453",
                "N",0,2,0,"65");

        productos.add(producto1);
        productos.add(producto2);
        productos.add(producto3);
        productos.add(producto4);
        productos.add(producto5);
        productos.add(producto6);

        return productos;
    }*/
    /*
    private void eliminarUltimaFila(){
        adapter.eliminarUltimoProducto();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        eliminarUltimaFila();
    }*/
}
