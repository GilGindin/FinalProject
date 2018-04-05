package com.gil.finalproject.Favorite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gil.finalproject.R;

import java.util.List;

/**
 * Created by gil on 22/03/2018.
 */

public class CustomArrayAdpter extends RecyclerView.Adapter<CustomArrayAdpter.MyHolder> {

    Context context;
  public   List<Favorites> allfev;

    public CustomArrayAdpter(Context context, List<Favorites> allfev) {
        this.context = context;
        this.allfev = allfev;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.favorite_single_layout , parent , false);
        MyHolder single = new MyHolder(v);

        return single;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        Favorites current = allfev.get(position);
        holder.bindData(current);
    }


    @Override
    public int getItemCount() {
        return allfev.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder{

        TextView text1;
        TextView text2;
        View  view;

        public MyHolder(View itemView) {
            super(itemView);
            view = itemView;
        }


        public void bindData(Favorites currentFave){

            text1 = (TextView) itemView.findViewById(R.id.textView);
            text1.setText(currentFave.name);
            text2 = (TextView) itemView.findViewById(R.id.textView2);
            text2.setText(currentFave.adress);
        }
    }
}
