package com.example.farmaapp.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "producto")
public class Producto {

    @PrimaryKey
    @ColumnInfo(name = "co_producto")
    @NonNull
    private String coProducto;

    @ColumnInfo(name = "de_producto")
    @NonNull
    private String deProducto;

    @ColumnInfo(name = "de_unidad")
    @NonNull
    private String deUnidad;

    @ColumnInfo(name = "de_unidad_fraccion")
    @NonNull
    private String deUnidadFraccion;

    @ColumnInfo(name = "in_prod_fraccionado")
    @NonNull
    private String inProdFraccionado;

    @ColumnInfo(name = "va_fraccion")
    @NonNull
    private Integer vaFraccion;

    @ColumnInfo(name = "co_laboratorio")
    @NonNull
    private String coLaboratorio;

    @ColumnInfo(name = "de_laboratorio")
    @NonNull
    private String deLaboratorio;

    public String getCoProducto() {
        return coProducto;
    }

    public void setCoProducto(String coProducto) {
        this.coProducto = coProducto;
    }

    public String getDeProducto() {
        return deProducto;
    }

    public void setDeProducto(String deProducto) {
        this.deProducto = deProducto;
    }

    public String getDeUnidad() {
        return deUnidad;
    }

    public void setDeUnidad(String deUnidad) {
        this.deUnidad = deUnidad;
    }

    public String getDeUnidadFraccion() {
        return deUnidadFraccion;
    }

    public void setDeUnidadFraccion(String deUnidadFraccion) {
        this.deUnidadFraccion = deUnidadFraccion;
    }

    public String getInProdFraccionado() {
        return inProdFraccionado;
    }

    public void setInProdFraccionado(String inProdFraccionado) {
        this.inProdFraccionado = inProdFraccionado;
    }

    public Integer getVaFraccion() {
        return vaFraccion;
    }

    public void setVaFraccion(Integer vaFraccion) {
        this.vaFraccion = vaFraccion;
    }

    public String getCoLaboratorio() {
        return coLaboratorio;
    }

    public void setCoLaboratorio(String coLaboratorio) {
        this.coLaboratorio = coLaboratorio;
    }

    public String getDeLaboratorio() {
        return deLaboratorio;
    }

    public void setDeLaboratorio(String deLaboratorio) {
        this.deLaboratorio = deLaboratorio;
    }
}
