package com.example.prueba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by syueh on 05/07/15.
 */
public class SalaArrayAdapter extends ArrayAdapter<Sala> {

    private static final String TAG = "SalaArrayAdapter";
    private List<Sala> SalaList = new ArrayList<Sala>();
    Context context;
    static class SalaViewHolder {
        ImageView StatusImg;
        TextView SalaName;
        TextView DetailSala;
    }

    public SalaArrayAdapter(Context context, int textViewResourceId, List<Sala> lista) {
        super(context, textViewResourceId, lista);
        this.context =  context;
    }

    @Override
    public void add(Sala object) {
        SalaList.add(object);
        super.add(object);
    }

    /*@Override
    //public int getCount() {
        return this.SalaList.size();
    }*/

   /* @Override
    public Sala getItem(int index) {
        return this.SalaList.get(index);
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sala rowItem = getItem(position);
        SalaViewHolder viewHolder = null;
        LayoutInflater mInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.listview_row_layout, null);
            viewHolder = new SalaViewHolder();
            viewHolder.StatusImg = (ImageView) convertView.findViewById(R.id.statusImg);
            viewHolder.SalaName = (TextView) convertView.findViewById(R.id.nameSala);
            viewHolder.DetailSala = (TextView) convertView.findViewById(R.id.detailSala);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SalaViewHolder)convertView.getTag();
        }
        Sala sala = getItem(position);

        viewHolder.StatusImg.setImageResource(sala.getStatus());

        viewHolder.SalaName.setText("\n"+sala.getSalaName());
        viewHolder.DetailSala.setText("Detalles:\n" + sala.getDetails() + "\n" + sala.getLocation() + "\nCapacidad: " + Integer.toString(sala.getCapacity()));
        return convertView;
    }

}