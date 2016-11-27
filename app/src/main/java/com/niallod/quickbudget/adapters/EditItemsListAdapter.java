package com.niallod.quickbudget.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.niallod.quickbudget.EditItem;
import com.niallod.quickbudget.ItemModifyActivity;
import com.niallod.quickbudget.R;
import com.niallod.quickbudget.business.Item;

import java.util.List;
import java.util.Locale;

/**
 * Created by nodat on 26/11/2016.
 */

public class EditItemsListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private int rowLayout;
    private List<Item> data;
    private boolean isEditItem;

    public EditItemsListAdapter(Context context, int rowLayout, List<Item> data, List<String> dataNames, boolean isEditItem) {
        super(context, rowLayout, dataNames);
        this.context = context;
        this.rowLayout = rowLayout;
        this.data = data;
        this.isEditItem = isEditItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        TextView label;
        TextView value;
        ImageView editButton;
        if(convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(rowLayout, viewGroup, false);

            label = (TextView) convertView.findViewById(R.id.edit_item_label);
            value = (TextView) convertView.findViewById(R.id.edit_item_value);
            editButton = (ImageView) convertView.findViewById(R.id.edit_item_button);

            if(isEditItem) {
                editButton.setImageResource(R.mipmap.ic_create_black_24dp);
            } else {
                editButton.setImageResource(R.mipmap.ic_delete_black_24dp);
            }
            label.setText(data.get(position).getName());
            value.setText(String.format(Locale.UK, "%6.2f", data.get(position).getValue()));
        }

        return convertView;
    }

}
