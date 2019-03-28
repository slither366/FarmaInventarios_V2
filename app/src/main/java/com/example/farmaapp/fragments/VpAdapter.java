package com.example.farmaapp.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class VpAdapter extends FragmentPagerAdapter {
    private List<Fragment> listaFragmentos = new ArrayList<>();
    private List<String> listaTitulos = new ArrayList<>();

    public void agregarFragmento(Fragment fragmento, String titulo){
        listaFragmentos.add(fragmento);
        listaTitulos.add(titulo);
    }

    public VpAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listaTitulos.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return listaFragmentos.get(i);
    }

    @Override
    public int getCount() {
        return listaFragmentos.size();
    }
}
