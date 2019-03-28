package com.example.farmaapp.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/*
@Entity(tableName = "productoinventario",
        foreignKeys = @ForeignKey(entity = Producto.class,
        parentColumns = "co_producto",
        childColumns = "codi_producto"
))*/

@Entity(tableName = "productoinventario")
public class ProductoInventario {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int codigo;

    @ColumnInfo(name = "co_producto")
    @NonNull
    private String coproducto;

    @ColumnInfo(name = "de_producto")
    @NonNull
    private String deproducto;

    @ColumnInfo(name = "co_laboratorio")
    @NonNull
    private String coLaboratorio;

    @ColumnInfo(name = "in_prod_fraccionado")
    @NonNull
    private String inProdFraccionado;

    @ColumnInfo(name = "va_fraccion")
    @NonNull
    private Integer vaFraccion;

    @ColumnInfo(name = "ca_entero")
    private Integer caEntero;

    @ColumnInfo(name = "ca_fraccion")
    private Integer caFraccion;

    @ColumnInfo(name = "nu_anaquel")
    private String nuAnaquel;

    @ColumnInfo(name = "nu_anaquel_concat")
    private String nuAnaquelConcat = "";

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    @NonNull
    public String getCoproducto() {
        return coproducto;
    }

    public void setCoproducto(@NonNull String coproducto) {
        this.coproducto = coproducto;
    }

    @NonNull
    public String getDeproducto() {
        return deproducto;
    }

    public void setDeproducto(@NonNull String deproducto) {
        this.deproducto = deproducto;
    }

    @NonNull
    public String getCoLaboratorio() {
        return coLaboratorio;
    }

    public void setCoLaboratorio(@NonNull String coLaboratorio) {
        this.coLaboratorio = coLaboratorio;
    }

    @NonNull
    public String getInProdFraccionado() {
        return inProdFraccionado;
    }

    public void setInProdFraccionado(@NonNull String inProdFraccionado) {
        this.inProdFraccionado = inProdFraccionado;
    }

    @NonNull
    public Integer getVaFraccion() {
        return vaFraccion;
    }

    public void setVaFraccion(@NonNull Integer vaFraccion) {
        this.vaFraccion = vaFraccion;
    }

    public Integer getCaEntero() {
        return caEntero;
    }

    public void setCaEntero(Integer caEntero) {
        this.caEntero = caEntero;
    }

    public Integer getCaFraccion() {
        return caFraccion;
    }

    public void setCaFraccion(Integer caFraccion) {
        this.caFraccion = caFraccion;
    }

    public String getNuAnaquel() {
        return nuAnaquel;
    }

    public void setNuAnaquel(String nuAnaquel) {
        this.nuAnaquel = nuAnaquel;
    }

    public String getNuAnaquelConcat() {
        return nuAnaquelConcat;
    }

    public void setNuAnaquelConcat(String nuAnaquelConcat) {
        this.nuAnaquelConcat = nuAnaquelConcat;
    }
}
