package com.johnvdev.usedpartsdepot.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Card_MyCar_Adapter extends RecyclerView.Adapter<Card_MyCar_Adapter.CarViewHolder> {

    private Context mCtx;
    private List<MyCar> carList;

    public Card_MyCar_Adapter(Context mCtx, List<MyCar> carList) {
        this.mCtx = mCtx;
        this.carList = carList;
    }

    @Override
    public Card_MyCar_Adapter.CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.card_my_vehicle, null);

        return new Card_MyCar_Adapter.CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        MyCar car = carList.get(position);
        holder.textViewTitle.setText(car.getTitle());
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(car.getImage()));
    }


    @Override
    public int getItemCount() {
        return carList.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewTitle;
        public CarViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
    }
}
