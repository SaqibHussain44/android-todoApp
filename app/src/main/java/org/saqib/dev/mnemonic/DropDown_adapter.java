package org.saqib.dev.mnemonic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Saqib on 6/18/2017.
 */

public class DropDown_adapter extends ArrayAdapter<DropDown_Item> {

    public DropDown_adapter(@NonNull Context context, @LayoutRes int resource, ArrayList<DropDown_Item> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View ListItemView = convertView;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        if(ListItemView==null){
            ListItemView = inflater.inflate(R.layout.list_item_dropdown,parent,false);
        }

        final DropDown_Item currentListItem = getItem(position);

        TextView titleTextView = (TextView) ListItemView.findViewById(R.id.txtView_dropdownItem);
        titleTextView.setText(currentListItem.getItemName());
        titleTextView.setTextColor(Color.WHITE);
        titleTextView.setTypeface(null, Typeface.BOLD);
        titleTextView.setTextSize(20);
        return ListItemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View ListItemView = convertView;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        if(ListItemView==null){
            ListItemView = inflater.inflate(R.layout.list_item_dropdown,parent,false);
        }

        final DropDown_Item currentListItem = getItem(position);

        if(position == 0){
            ImageView imageView = (ImageView)ListItemView.findViewById(R.id.imgView_dropdownItem);
            imageView.setImageResource(R.drawable.ic_home_white_24dp);

            TextView titleTextView = (TextView) ListItemView.findViewById(R.id.txtView_dropdownItem);
            titleTextView.setText(currentListItem.getItemName());
            titleTextView.setTextColor(Color.WHITE);
        }
        else{
            ImageView imageView = (ImageView)ListItemView.findViewById(R.id.imgView_dropdownItem);
            imageView.setImageResource(R.drawable.ic_list_white_24dp);

            TextView titleTextView = (TextView) ListItemView.findViewById(R.id.txtView_dropdownItem);
            titleTextView.setText(currentListItem.getItemName());
            titleTextView.setTextColor(Color.WHITE);

        }
        return ListItemView;
    }
}
