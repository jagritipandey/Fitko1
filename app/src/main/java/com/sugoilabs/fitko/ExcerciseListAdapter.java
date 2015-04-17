package com.sugoilabs.fitko;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by trath on 1/21/2015.
 */
public class ExcerciseListAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] title;
    private Integer[] iconId;
    private ImageView iconCompleted;
    private ArrayList<Integer> iconClickedPostion;

    public ExcerciseListAdapter(Activity context, String[] title, Integer[] icon) {
        super(context, R.layout.excercise_list_item , title);
        this.context = context;
        this.title = title;
        this.iconId = icon;
        iconClickedPostion = new ArrayList<Integer>();

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.excercise_list_item, null, true);
        TextView excerciseTitle = (TextView) rowView.findViewById(R.id.excerciseText);
        final ImageView completedIcon = (ImageView) rowView.findViewById(R.id.excerciseCompletedIcon);
        excerciseTitle.setText(title[position]);
        if(iconClickedPostion.contains(position)){
            completedIcon.setImageResource(R.drawable.ic_done);
        }
        else {
            completedIcon.setImageResource(iconId[position]);
        }

        completedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View icon = v;
                    icon.setBackgroundResource(R.drawable.ic_done);
                    iconClickedPostion.add(position);
                }
        });
        return rowView;
    }

}
