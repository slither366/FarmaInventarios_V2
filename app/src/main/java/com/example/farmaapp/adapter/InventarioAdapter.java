package com.example.farmaapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farmaapp.R;
import com.example.farmaapp.entity.Producto;
import com.example.farmaapp.entity.ProductoInventario;
import com.example.farmaapp.entity.ProductoInventario;

import java.util.List;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioAdapter.ViewHolder>{

    List<ProductoInventario> productosInventario;

    public InventarioAdapter(List<ProductoInventario> ProductoInventario) {
        this.productosInventario = ProductoInventario;
    }

    public void eliminarUltimoProducto(){
        productosInventario.remove(productosInventario.size()-1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_inventario, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ProductoInventario ProductoInventario = productosInventario.get(i);

        viewHolder.tv_codigo.setText(ProductoInventario.getCoproducto());
        viewHolder.tv_nombre.setText(ProductoInventario.getDeproducto());
        viewHolder.tv_anaquel.setText(ProductoInventario.getNuAnaquelConcat());
        viewHolder.tv_entero.setText(ProductoInventario.getCaEntero().toString());
        viewHolder.tv_fraccion.setText(ProductoInventario.getCaFraccion().toString());
    }

    @Override
    public int getItemCount() {
        return productosInventario.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_codigo, tv_nombre, tv_anaquel, tv_entero, tv_fraccion;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tv_codigo = itemView.findViewById(R.id.tv_codigo);
            tv_nombre = itemView.findViewById(R.id.tv_nombre);
            tv_anaquel = itemView.findViewById(R.id.tv_anaquel);
            tv_entero = itemView.findViewById(R.id.tv_entero);
            tv_fraccion = itemView.findViewById(R.id.tv_fraccion);
        }
    }



}
