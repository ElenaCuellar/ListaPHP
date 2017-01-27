package com.example.caxidy.listaphp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context contexto;
    private List<String> cabeceras;
    private HashMap<String, List<Pelicula>> hijos;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Pelicula>> listChildData) {
        this.contexto = context;
        this.cabeceras = listDataHeader;
        this.hijos = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.hijos.get(this.cabeceras.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String textoHijo = ((Pelicula)getChild(groupPosition, childPosition)).getNombre();

        if (convertView == null) {
            LayoutInflater lyInflater = (LayoutInflater) this.contexto
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = lyInflater.inflate(R.layout.lista_hijos, null);
        }

        TextView tHijo = (TextView) convertView
                .findViewById(R.id.textoHijo);

        tHijo.setText(textoHijo);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.hijos.get(this.cabeceras.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.cabeceras.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.cabeceras.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String textoCabecera = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater lyInflater = (LayoutInflater) this.contexto
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = lyInflater.inflate(R.layout.lista_cabeceras, null);
        }

        TextView tCab = (TextView) convertView
                .findViewById(R.id.textoCab);
        tCab.setTypeface(null, Typeface.BOLD);
        tCab.setText(textoCabecera);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
