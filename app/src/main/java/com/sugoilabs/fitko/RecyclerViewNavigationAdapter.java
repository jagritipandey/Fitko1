package com.sugoilabs.fitko;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by trath on 1/18/2015.
 */
public class RecyclerViewNavigationAdapter extends RecyclerView.Adapter<RecyclerViewNavigationAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<RecyclerViewNavigationItem> recyclerViewNavigationItemList = Collections.emptyList();
    Context contextnew;
    private ClickListener clickListner;

    public RecyclerViewNavigationAdapter(Context context , List<RecyclerViewNavigationItem> recyclerViewNavigationItemList) {
        inflater = LayoutInflater.from(context);
        contextnew=context;
        this.recyclerViewNavigationItemList = recyclerViewNavigationItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.navigation_row_item, parent,false);
        MyViewHolder holder =  new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        RecyclerViewNavigationItem currentItem = recyclerViewNavigationItemList.get(position);
        viewHolder.title.setText(currentItem.title);
        viewHolder.icon.setImageResource(currentItem.iconid);

    }

    @Override
    public int getItemCount() {
        return recyclerViewNavigationItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.navigationTitle);
            icon = (ImageView) itemView.findViewById(R.id.navigationIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            if(clickListner != null){
                TextView t = (TextView) v.findViewById(R.id.navigationTitle);
                t.setTextColor(contextnew.getResources().getColor(R.color.yellowColor));
                clickListner.itemClicked(v, getPosition());
            }

        }


    }

    public interface ClickListener{
        public void itemClicked(View view , int position);

    }

    public void setClickListener(ClickListener clickListner){
        this.clickListner = clickListner;
    }
}
