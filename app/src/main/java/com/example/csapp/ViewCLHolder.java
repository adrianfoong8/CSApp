package com.example.csapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ViewCLHolder extends RecyclerView.ViewHolder {

    View view;
    private ViewCLHolder.ClickListener clickListener;

    public ViewCLHolder(View itemView) {
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

    public void setDetails(Context ctx, String firstName, String lastName, String image, String state) {
        TextView tvFirstName = view.findViewById(R.id.cv_first_name);
        TextView tvLastName = view.findViewById(R.id.cv_last_name);
        ImageView ivProfilePicture = view.findViewById(R.id.cv_profile_picture);
        TextView tvState = view.findViewById(R.id.cv_state);

        tvFirstName.setText(firstName);
        tvLastName.setText(lastName);
        Picasso.get().load(image).into(ivProfilePicture);
        tvState.setText(state);
    }

    public void setOnClickListener(ViewCLHolder.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
