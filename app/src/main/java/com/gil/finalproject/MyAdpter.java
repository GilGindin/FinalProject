package com.gil.finalproject;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gil on 11/02/2018.
 */

public class MyAdpter extends RecyclerView.Adapter<MyAdpter.MyHolder> {

   public List<Book> lastBook = null;
    private List<MyModels> allResults;
    Context context;
    String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    String key ="&key=AIzaSyBQKf7VwlWtSsDHKdyFfVI5AvGSZS1dlW8";

    public MyAdpter(List<MyModels> allResults, Activity context) {
        this.allResults = allResults;
        this.context = context;
    }



    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent , false);
        MyHolder mySingle = new MyHolder(v);
        return mySingle;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        MyModels current = allResults.get(position);
        holder.bindData(current);
    }

    @Override
    public int getItemCount() {

        return allResults.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView tv;
        TextView tv1;
        ImageView modelsIV;
        View myView;

        public MyHolder(View itemView) {
            super(itemView);
            myView = itemView;

        }
        public void bindData(final MyModels currentModel) {

            tv = (TextView) itemView.findViewById(R.id.nameTV);
            tv.setText(currentModel.name);
            tv1 = (TextView) itemView.findViewById(R.id.adressTV);
            tv1.setText(currentModel.formatted_address);
            modelsIV = (ImageView) itemView.findViewById(R.id.modelIV);
            if(currentModel.photos != null) {
                String photoString = urlPhoto + currentModel.photos.get(0).photo_reference + key;
                Picasso.with(context).load(photoString).into(modelsIV);
            }

            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double lat = currentModel.geometry.location.lat ;
                    double lng = currentModel.geometry.location.lng;
                    myMapChnger changer = (myMapChnger) context;
                    changer.changeFragment(lat , lng);

                    Book.deleteAll(Book.class);

                    for (int position = 0; position < allResults.size(); position++) {
                        String nameDB = allResults.get(position).name;
                        String adressDB = allResults.get(position).formatted_address;
                        double latDB = allResults.get(position).geometry.location.lat;
                        double lngDB = allResults.get(position).geometry.location.lng;

                        List<Book> books = new ArrayList<>();
                        books.add(new Book(nameDB , adressDB , latDB , lngDB));
                        Book.saveInTx(books);

                        lastBook = Book.listAll(Book.class);

                        ArrayList<Book> lastBook = new ArrayList<>();
                        lastBook.addAll(lastBook);

                    }

                }

            });
            myView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                   // Intent intent = new Intent(context , );
                  //  intent.putExtra("name" , currentModel.name);
                    //intent.putExtra("adress" , currentModel.formatted_address);
                    //context.startActivity(intent);

                    return true;
                }
            });

        }
    }
}
