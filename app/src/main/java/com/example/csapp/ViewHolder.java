package com.example.csapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View view;
    private ViewHolder.ClickListener clickListener;

    public ViewHolder(View itemView) {
        super(itemView);
        view = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
    }

    public void setDetails(Context ctx, String title, String description, String image) {
        TextView tvName = view.findViewById(R.id.cv_name);
        TextView tvDescription = view.findViewById(R.id.cv_description);
        ImageView ivProfile = view.findViewById(R.id.iv_profile);

        tvName.setText(title);
        tvDescription.setText(description);
        Picasso.get().load(image).into(ivProfile);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}