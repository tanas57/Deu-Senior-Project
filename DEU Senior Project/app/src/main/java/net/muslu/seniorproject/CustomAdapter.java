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

import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<BarcodeReadModel> data;

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

    public CustomAdapter(Context mContext, ArrayList<BarcodeReadModel> data) {
        this.mContext = mContext;
        this.data = data;
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

        BarcodeReadModel barcodeReadModel = this.data.get(i);

        holder.customerName.setText(barcodeReadModel.getCustomer().getCustomerFullName());
        holder.customerAddress.setText(barcodeReadModel.getCustomer().getCustomerAddress());
        Picasso.with(mContext).load(barcodeReadModel.getBarcodeImgApiURL()).into(holder.packageBarcode);

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}