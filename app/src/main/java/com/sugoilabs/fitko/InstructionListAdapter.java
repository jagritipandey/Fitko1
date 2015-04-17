package com.sugoilabs.fitko;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by trath on 1/21/2015.
 */
public class InstructionListAdapter extends ArrayAdapter<String> {

    private Activity context;
    private String[] title;

    public InstructionListAdapter(Activity context, String[] title) {
        super(context, R.layout.instructions_list_item , title);
        this.context = context;
        this.title = title;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.instructions_list_item, null, true);
        TextView instructionTitle = (TextView) rowView.findViewById(R.id.instructionText);
        instructionTitle.setText(title[position]);
        return rowView;
    }

}


