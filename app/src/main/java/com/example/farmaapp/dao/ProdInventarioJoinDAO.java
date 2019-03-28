package com.example.farmaapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProdInventarioJoinDAO {

    @Query("SELECT ProductoInventario.co_producto AS co_producto, Producto.de_producto AS de_producto," +
            "ProductoInventario.nu_anaquel_concat AS nu_anaquel_concat, ProductoInventario.ca_entero AS ca_entero," +
            "ProductoInventario.ca_fraccion AS ca_fraccion " +
            "FROM ProductoInventario, Producto " +
            "WHERE ProductoInventario.co_producto = Producto.co_producto")
    public LiveData<List<ProdInventarioJoin>> loadAllProductos();

    // You can also define this class in a separate file, as long as you add the
    // "public" access modifier.
    static class ProdInventarioJoin {
        public String co_producto;
        public String de_producto;
        public String nu_anaquel_concat;
        public int ca_entero;
        public int ca_fraccion;
    }



}
