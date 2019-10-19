package net.muslu.seniorproject;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context mContext;
    private Integer[] mImage;
    private String[] mTitle;
    private String[] msubTitle;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView customerName;
        TextView customerAddress;
        ImageView packageBarcode;


        public MyViewHolder(View itemView) {
            super(itemView);

            this.customerAddress = (TextView) itemView.findViewById(R.id.CustomerAddress);
            this.customerName = (TextView) itemView.findViewById(R.id.CustomerName);
            this.packageBarcode = (ImageView) itemView.findViewById(R.id.PackageBarcode);
        }
    }

    public CustomAdapter(Context mContext, Integer[] image,String[] title,String[] subTitle) {
        this.mContext = mContext;
        this.mImage = image;
        this.mTitle = title;
        this.msubTitle = subTitle;
    }
    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.read_barcode_view, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int i) {


        holder.customerName.setText(mTitle[i]);
        holder.customerAddress.setText(msubTitle[i]);
        Picasso.with(mContext).load(mImage[i]).into(holder.packageBarcode);


    }

    @Override
    public int getItemCount() {
        return mTitle.length;
    }
}