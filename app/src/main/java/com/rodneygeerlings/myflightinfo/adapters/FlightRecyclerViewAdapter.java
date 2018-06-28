package com.rodneygeerlings.myflightinfo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.example.rodneygeerlings.myflightinfo.R;
import com.rodneygeerlings.myflightinfo.activities.FlightDetailActivity;
import com.rodneygeerlings.myflightinfo.models.Flight;
import java.util.ArrayList;

public class FlightRecyclerViewAdapter extends RecyclerView.Adapter<FlightRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    public ArrayList<Flight> flights;

    public FlightRecyclerViewAdapter(Context context, ArrayList<Flight> flights) {
        this.context = context;
        this.flights = flights;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.flight_list_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlightRecyclerViewAdapter.MyViewHolder holder, final int position) {
        // Set click listener
        holder.btnViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Init new intend and pass the movie ID data
                Intent intent = new Intent(context, FlightDetailActivity.class);
                intent.putExtra("FlightId", flights.get(position).getFlightId());

                Log.d("clicked flight id", "" + flights.get(position).getFlightId());
                // Start the new movie detail activity
                context.startActivity(intent);
            }
        });

        holder.tvFlightName.setText(flights.get(position).getFlightName());
    }

    public int getItemCount() {
        return flights.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llFlightItem;
        TextView tvFlightName;
        Button btnViewDetail;

        public MyViewHolder(View itemView) {
            super(itemView);
            // Binds the current view item's components
            llFlightItem = itemView.findViewById(R.id.ll_flight_list_item);
            tvFlightName = itemView.findViewById(R.id.tv_flight_item_name);
            btnViewDetail = itemView.findViewById(R.id.btn_view_detail);
        }
    }
}

