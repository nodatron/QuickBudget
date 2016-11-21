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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nodat on 21/11/2016.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private List<String> parent_titles;
    private HashMap<String, List<String>> child_parent_link;
    private Context context;

    private HashMap<List<String>, List<Double>> values;

    public MyExpandableListAdapter(Context context,
                                   List<String> parent_titles,
                                   HashMap<String, List<String>> child_parent_link,
                                   HashMap<List<String>, List<Double>> values) {
        this.context = context;
        this.parent_titles = parent_titles;
        this.child_parent_link = child_parent_link;
        this.values = values;
    }

    @Override
    public int getGroupCount() {
        return parent_titles.size();
    }

    @Override
    public int getChildrenCount(int i) {
        String key = parent_titles.get(i);
        return child_parent_link.get(key).size();
    }

    @Override
    public Object getGroup(int i) {
        return parent_titles.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return child_parent_link.get(parent_titles.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
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
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String title = (String) this.getGroup(i);

        if(view == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.parent_layout, null);
        }

        TextView parent_label = (TextView) view.findViewById(R.id.parent_label);
        TextView parent_value = (TextView) view.findViewById(R.id.parent_value);
        ImageButton expand_button = (ImageButton) view.findViewById(R.id.parent_expand_button);

        parent_label.setTypeface(null, Typeface.BOLD);
        parent_label.setText(title);

        Double value = calculateTotalOfValues(title);
        String value_string = value.toString();
        parent_value.setText(value_string);

        //TODO get the image and place it here

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String title = (String) this.getChild(i, i1);

        if(view == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.parent_layout, null);
        }

        TextView child_label = (TextView) view.findViewById(R.id.item_name_child);
        TextView child_value = (TextView) view.findViewById(R.id.item_value_child);
        ImageButton child_button = (ImageButton) view.findViewById(R.id.item_edit_button);

        child_label.setTypeface(null, Typeface.BOLD);
        child_label.setText(title);

        ArrayList<Double> child_values = (ArrayList<Double>) values.get(child_parent_link.get(title));
        Double theValue = child_values.get(i1);
        String theValueString = theValue.toString();
        child_value.setText(theValueString);

        //TODO get the image and place it here


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private Double calculateTotalOfValues(String key) {
        Double total = 0.0;

        List<String> child_labels = child_parent_link.get(key);

        List<Double> child_values = values.get(child_labels);

        for (Double d:
             child_values) {
            total += d;
        }

        return total;
    }

}
