package com.example.csapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewCLPackageHolder extends RecyclerView.ViewHolder {

    View view;
    private ViewCLPackageHolder.ClickListener clickListener;

    public ViewCLPackageHolder(View itemView) {
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

    public void setDetails(Context ctx, String packagePhoto, String packageName, String packagePrice) {
        ImageView ivPackagePhoto = view.findViewById(R.id.cv_package_photo);
        TextView tvPackageName = view.findViewById(R.id.cv_package_name);
        TextView tvPackagePrice = view.findViewById(R.id.cv_package_price);

        Picasso.get().load(packagePhoto).into(ivPackagePhoto);
        tvPackageName.setText(packageName);
        tvPackagePrice.setText(packagePrice);
    }

    public void setOnClickListener(ViewCLPackageHolder.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
