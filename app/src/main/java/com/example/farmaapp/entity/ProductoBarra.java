package com.example.farmaapp.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "productobarra")
public class ProductoBarra {

    @PrimaryKey
    @ColumnInfo(name = "co_barra")
    @NonNull
    private String coBarra;

    @ColumnInfo(name = "co_producto")
    @NonNull
    private String coProducto;

    public String getCoBarra() {
        return coBarra;
    }

    public void setCoBarra(String coBarra) {
        this.coBarra = coBarra;
    }

    public String getCoProducto() {
        return coProducto;
    }

    public void setCoProducto(String coProducto) {
        this.coProducto = coProducto;
    }
}
