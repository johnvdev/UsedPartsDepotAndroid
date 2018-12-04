package com.johnvdev.usedpartsdepot.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListPartAdapter extends RecyclerView.Adapter<ListPartAdapter.PartViewHolder>{

    private Context mCtx;
    private List<ListPart> partList;

    public ListPartAdapter(Context mCtx, List<ListPart> partList) {
        this.mCtx = mCtx;
        this.partList = partList;
    }

    @Override
    public PartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout, null);

        return new PartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PartViewHolder holder, int position) {
        final ListPart part = partList.get(position);
        holder.textViewTitle.setText(part.getTitle());
        holder.textViewDesc.setText(part.getShortDesc());
        holder.textViewPrice.setText(String.valueOf(part.getPrice()));
        holder.textViewRating.setText(String.valueOf(part.getRating()));
        holder.partCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx ,
                        PartInfo.class);
                intent.putExtra("PART_ID", part.getId());
                mCtx.startActivity(intent);

            }
        });

        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(part.getImage()));
    }

    @Override
    public int getItemCount() {
        return partList.size();
    }

    class PartViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewTitle, textViewDesc, textViewRating , textViewPrice;
        CardView partCard;
        public PartViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            partCard = itemView.findViewById(R.id.partCard);


        }
    }
}
