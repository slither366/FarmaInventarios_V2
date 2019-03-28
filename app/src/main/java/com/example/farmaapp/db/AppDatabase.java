package com.example.farmaapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.farmaapp.dao.ProdInventarioJoinDAO;
import com.example.farmaapp.dao.ProductoBarraDAO;
import com.example.farmaapp.dao.ProductoDAO;
import com.example.farmaapp.dao.ProductoInventarioDAO;
import com.example.farmaapp.entity.Producto;
import com.example.farmaapp.entity.ProductoBarra;
import com.example.farmaapp.entity.ProductoInventario;

@Database(entities = {ProductoBarra.class, ProductoInventario.class, Producto.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProductoBarraDAO getProductoBarraDao();

    public abstract ProductoInventarioDAO getProductoInventarioDao();

    public abstract ProductoDAO getProductoDao();

   //public abstract ProdInventarioJoinDAO ProdInventarioJoinDAO();

}
