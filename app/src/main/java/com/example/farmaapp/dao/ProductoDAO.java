package com.example.farmaapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.farmaapp.entity.Producto;

import java.util.List;

@Dao
public interface ProductoDAO {

    @Insert
    public void insert(Producto... productos);

    @Update
    public void update(Producto... productos);

    @Delete
    public void delete(Producto... productos);

    @Query("select * from Producto")
    public List<Producto> getProducto();

    @Query("select * from producto where co_producto = :codProducto")
    public Producto getProductoWithCodigo(String codProducto);

}
