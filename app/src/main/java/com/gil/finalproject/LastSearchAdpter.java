package com.gil.finalproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gil on 11/03/2018.
 */

public class LastSearchAdpter extends RecyclerView.Adapter<LastSearchAdpter.MyLastSearchHolder> {
    Context context;
    private List<Book> MyLastResults;

    public LastSearchAdpter(Context context, List<Book> myLastResults) {
        this.context = context;
        MyLastResults = myLastResults;
    }

    @Override
    public MyLastSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myV = LayoutInflater.from(context).inflate(R.layout.single_item , null);
        MyLastSearchHolder singleItem = new MyLastSearchHolder(myV);
        return singleItem;
    }

    @Override
    public void onBindViewHolder(MyLastSearchHolder holder, int position) {

        Book currentBook = MyLastResults.get(position);
        holder.onBindData(currentBook);

    }


    @Override
    public int getItemCount() {
       if(MyLastResults != null) {
            return MyLastResults.size();
        }
        return 0;
    }
    public class MyLastSearchHolder extends RecyclerView.ViewHolder {

        TextView tv;
        TextView tv1;
        View myView;


        public MyLastSearchHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }
        public void onBindData(Book curent){

            tv = (TextView) itemView.findViewById(R.id.nameTV);
            tv.setText(curent.name);
            tv1 = (TextView) itemView.findViewById(R.id.adressTV);
            tv1.setText(curent.adress);
            double keptLat = curent.lat;
            double keptLng = curent.lng;

        }
    }
}
