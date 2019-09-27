package com.example.csapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UploadPackageMedia extends RecyclerView.Adapter<UploadPackageMedia.ViewHolder> {

    public List<String> fileNameList;
    public List<String> fileDoneList;

    public UploadPackageMedia(List<String> fileNameList, List<String> fileDoneList) {
        this.fileNameList = fileNameList;
        this.fileDoneList = fileDoneList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_single, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String fileName = fileNameList.get(i);
        viewHolder.fileNameView.setText(fileName);
//        String fileDone = fileDoneList.get(i);
//        if (fileDone.equals("uploading")) {
//            viewHolder.fileDoneView.setImageResource(R.drawable.ic_add_media_progress);
//        } else {
//            viewHolder.fileDoneView.setImageResource(R.drawable.ic_add_media_check);
//        }
    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fileNameView;
        public ImageView fileDoneView;
        View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            fileNameView = mView.findViewById(R.id.tv_upload_filename);
            fileDoneView = mView.findViewById(R.id.iv_upload_done);
        }
    }
}
