package net.muslu.seniorproject;

import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import net.muslu.seniorproject.Reader.Barcode.BarcodeReadModel;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context mContext;
    static private ArrayList<BarcodeReadModel> data;
    private final ClickListener listener;
    private static WeakReference<ClickListener> listenerRef;

    public interface ClickListener {

        void onPositionClicked(View view, BarcodeReadModel model, int pos);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView customerName;
        TextView customerAddress;
        ImageView packageBarcode, phone, remove;


        public MyViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);
            this.customerAddress = (TextView) itemView.findViewById(R.id.CustomerAddress);
            this.customerName = (TextView) itemView.findViewById(R.id.CustomerName);
            this.packageBarcode = (ImageView) itemView.findViewById(R.id.PackageBarcode);
            this.phone = itemView.findViewById(R.id.makeCall);
            this.remove = itemView.findViewById(R.id.delete);

            phone.setOnClickListener(this);
            remove.setOnClickListener(this);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onPositionClicked(v, data.get(getAdapterPosition()),getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public CustomAdapter(Context mContext, ArrayList<BarcodeReadModel> data, ClickListener listener) {
        this.mContext = mContext;
        this.data = data;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.read_barcode_view, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view, listener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int i) {

        BarcodeReadModel barcodeReadModel = this.data.get(i);

        holder.customerName.setText(barcodeReadModel.getCustomer().getFullName());
        holder.customerAddress.setText(barcodeReadModel.getCustomer().getAddress());
        Picasso.with(mContext).load(barcodeReadModel.getBarcodeImgApiURL()).into(holder.packageBarcode);

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }
}