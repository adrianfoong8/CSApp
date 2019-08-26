package com.example.csapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewPackageHolder  extends RecyclerView.ViewHolder {

    View view;
    private ViewHolder.ClickListener clickListener;

    public ViewPackageHolder(View itemView) {
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

    public void setDetails(Context ctx, String packageName, String packagePrice, String packageDescription) {
        TextView tvPackageName = view.findViewById(R.id.cv_package_name);
        TextView tvPackagePrice = view.findViewById(R.id.cv_package_price);
        TextView tvPackageDescription = view.findViewById(R.id.cv_package_description);

        tvPackageName.setText(packageName);
        tvPackagePrice.setText(packagePrice);
        tvPackageDescription.setText(packageDescription);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
