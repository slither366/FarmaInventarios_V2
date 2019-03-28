package com.example.farmaapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.farmaapp.entity.ProductoBarra;
import com.example.farmaapp.entity.ProductoInventario;

import java.util.List;

@Dao
public interface ProductoInventarioDAO {

    @Insert
    public void insert(ProductoInventario... productoInventario);

    @Update
    public void update(ProductoInventario... productoInventario);

    @Delete
    public void delete(ProductoInventario... productoInventario);

    @Query("select * from ProductoInventario")
    public List<ProductoInventario> getProductoInventario();

    @Query("select * from ProductoInventario where co_producto = :codProducto")
    public ProductoInventario getProdInventariadoWithCodigo(String codProducto);

    @Query("SELECT COUNT(codigo) FROM ProductoInventario")
    int getCount();

}
