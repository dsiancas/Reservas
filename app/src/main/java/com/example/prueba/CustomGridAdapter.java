package com.example.prueba;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomGridAdapter extends ArrayAdapter<ItemGrid> {
    Context context;
    int layoutResourceId;
    ArrayList<ItemGrid> data = new ArrayList<ItemGrid>();

    public CustomGridAdapter(Context context, int layoutResourceId,
                                 ArrayList<ItemGrid> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            holder.layer = (LinearLayout) row.findViewById(R.id.capa);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        ItemGrid item = data.get(position);
        holder.txtTitle.setText(item.getTitle());
        if(item.getStatus() == 0)
            holder.layer.setBackgroundColor(Color.parseColor("#ffe7a487"));
        else
            holder.layer.setBackgroundColor(Color.parseColor("#ffa4eba2"));
        holder.imageItem.setImageBitmap(item.getImage());
        return row;

    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;
        LinearLayout layer;

    }
}