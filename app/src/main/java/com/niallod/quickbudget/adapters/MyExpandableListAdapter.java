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

import java.util.HashMap;
import java.util.List;

/**
 * Created by nodat on 21/11/2016.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private List<String> parent_title;
    private HashMap<String, List<String>> child_parent_link;
    private Context context;

    public MyExpandableListAdapter(Context context,
                                   List<String> parent_title,
                                   HashMap<String, List<String>> child_parent_link) {
        this.context = context;
        this.parent_title = parent_title;
        this.child_parent_link = child_parent_link;
    }


    @Override
    public int getGroupCount() {
        return parent_title.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return child_parent_link.get(parent_title.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return parent_title.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return child_parent_link.get(parent_title.get(i)).get(i1);
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
        }

        TextView parent_label = (TextView) view.findViewById(R.id.parent_label);
        TextView parent_value = (TextView) view.findViewById(R.id.parent_value);
//        ImageButton expand_button = (ImageButton) view.findViewById(R.id.parent_expand_button);

        parent_label.setTypeface(null, Typeface.BOLD);
        parent_label.setText(title);

        parent_value.setTypeface(null, Typeface.BOLD);
        parent_value.setText(title);

//        Double value = calculateTotalOfValues(title);
//        String value_string = value.toString();
//        parent_value.setText(value_string);

        //TODO get the image and place it here

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String title = (String) this.getChild(i, i1);

        if(view == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.child_layout, null);
        }

        TextView child_label = (TextView) view.findViewById(R.id.item_name_child);
        TextView child_value = (TextView) view.findViewById(R.id.item_value_child);
//        ImageButton child_button = (ImageButton) view.findViewById(R.id.item_edit_button);

        child_label.setText(title);

//        ArrayList<Double> child_values = (ArrayList<Double>) values.get(child_parent_link.get(title));
//        Double theValue = child_values.get(i1);
//        String theValueString = theValue.toString();
//        child_value.setText(theValueString);

        //TODO get the image and place it here


        return view;
    }
}
