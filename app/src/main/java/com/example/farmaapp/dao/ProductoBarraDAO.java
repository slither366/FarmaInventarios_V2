package com.example.farmaapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.farmaapp.entity.ProductoBarra;

import java.util.List;

@Dao
public interface ProductoBarraDAO {

    @Insert
    public void insert(ProductoBarra... productoBarras);

    @Update
    public void update(ProductoBarra... productoBarras);

    @Delete
    public void delete(ProductoBarra... productoBarras);

    @Query("select * from ProductoBarra")
    public List<ProductoBarra> getProductoBarra();

    @Query("select * from ProductoBarra where co_barra = :codBarra")
    public ProductoBarra getProductoWithBarra(String codBarra);

}
