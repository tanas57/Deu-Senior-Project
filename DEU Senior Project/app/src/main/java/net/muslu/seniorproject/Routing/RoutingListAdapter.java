package net.muslu.seniorproject.Routing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import net.muslu.seniorproject.Algorithm.Chromosome;
import net.muslu.seniorproject.R;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RoutingListAdapter extends RecyclerView.Adapter<RoutingListAdapter.MyViewHolder> {
    private Context mContext;
    static private ArrayList<Chromosome> data;
    private final ClickListener listener;
    private static WeakReference<ClickListener> listenerRef;

    public interface ClickListener {
        void onPositionClicked(View view, int pos);
    }

    public RoutingListAdapter(Context mContext, ArrayList<Chromosome> data, ClickListener listener) {
        this.mContext = mContext;
        this.data = data;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_popup_listview_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view, listener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int i) {

        Chromosome chromosome = this.data.get(i);

        holder.name.setText("Rota " + (i+1) + " ");
        holder.distance.setText(" " + chromosome.getMetres());
        holder.duration.setText(" " + chromosome.getMetres());
        holder.count.setText(chromosome.getBarcodeReadModels().size() + " adet");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView count, distance, duration, name;


        public MyViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);
            this.name = itemView.findViewById(R.id.route_name);
            this.distance = itemView.findViewById(R.id.route_distance);
            this.duration = itemView.findViewById(R.id.route_time);
            this.count = itemView.findViewById(R.id.route_num);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onPositionClicked(v, getAdapterPosition());
        }

    }
}