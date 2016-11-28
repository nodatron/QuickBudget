package com.niallod.quickbudget.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.niallod.quickbudget.R;
import com.niallod.quickbudget.business.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by nodat on 21/11/2016.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private String parentTitle;
    private HashMap<String, List<Item>> childItems;
    private Context context;

    public MyExpandableListAdapter(Context context,
                                   String parentTitle,
                                   HashMap<String, List<Item>> childItems) {
        this.context = context;
        this.parentTitle = parentTitle;
        this.childItems = childItems;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int i) {
        return childItems.get(parentTitle).size();
    }

    @Override
    public Object getGroup(int i) {
        return parentTitle;
    }

    @Override
    public Object getChild(int i, int i1) {
        return childItems.get(parentTitle).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String title = (String) this.getGroup(i);

        if(view == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.parent_layout, null);


            TextView parent_label = (TextView) view.findViewById(R.id.parent_label);
            TextView parent_value = (TextView) view.findViewById(R.id.parent_value);

            parent_label.setTypeface(null, Typeface.BOLD);
            parent_label.setText(title);

            double value = calculateTotalOfValues();
            String value_string = String.format(Locale.UK, "%6.2f", value);
            parent_value.setText(value_string);
        }

        //TODO get the image and place it here

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Item item = (Item) getChild(i, i1);
        String title = item.getName();
        float value = item.getValue();

        if(view == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.child_layout, null);


            TextView child_label = (TextView) view.findViewById(R.id.item_name_child);
            TextView child_value = (TextView) view.findViewById(R.id.item_value_child);

            child_label.setText(title);
            String valueString = String.format(Locale.UK, "%6.2f", value);
            child_value.setText(valueString);


            //TODO get the image and place it here
        }


        return view;
    }

    private double calculateTotalOfValues() {
        double value = 0.0;
        List<Item> items = childItems.get(parentTitle);

        for(Item i : items) {
            value +=  (double) i.getValue();
        }

        return value;
    }
}
