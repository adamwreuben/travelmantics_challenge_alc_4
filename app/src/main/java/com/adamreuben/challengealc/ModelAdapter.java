package com.adamreuben.challengealc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.MainViewHolder>{

    private List<ModelClass> list;
    private Context context;
    private StorageReference mStorage;
    private FirebaseFirestore mFirestore;


    public ModelAdapter(List<ModelClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new MainViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, final int position) {

        final ModelClass modelClass = list.get(position);
        holder.bindData(modelClass);


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MainViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView hotenametxt,hotelInfotxt,pricetxt;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.hotelImage);
            hotenametxt = itemView.findViewById(R.id.hotelNametxt);
            hotelInfotxt = itemView.findViewById(R.id.hotelInfotxt);
            pricetxt = itemView.findViewById(R.id.hotelPricetxt);



        }

        public void bindData(ModelClass modelClass){

            hotenametxt.setText(modelClass.getHotelName());
            hotelInfotxt.setText(modelClass.getHotelInfo());
            pricetxt.setText(modelClass.getHotelPrice());

            Picasso.get().load(modelClass.getHotelImage()).into(imageView);
        }


    }




}
