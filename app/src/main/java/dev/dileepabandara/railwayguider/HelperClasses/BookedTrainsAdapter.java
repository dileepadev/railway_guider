/*
   --------------------------------------
      Developed by
      Dileepa Bandara
      https://dileepabandara.github.io
      contact.dileepabandara@gmail.com
      ©dileepabandara.dev
      2020
   --------------------------------------
*/

package dev.dileepabandara.railwayguider.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.dileepabandara.railwayguider.Databases.BookedTrainsHelperClass;
import dev.dileepabandara.railwayguider.R;

import dev.dileepabandara.railwayguider.User.BookedTicketView;

import java.util.ArrayList;

public class BookedTrainsAdapter extends RecyclerView.Adapter<BookedTrainsAdapter.MyViewHolder> {

    Context context;
    ArrayList<BookedTrainsHelperClass> bookedTrainsHelperClasses;

    public BookedTrainsAdapter(Context c, ArrayList<BookedTrainsHelperClass> p){
        context = c;
        bookedTrainsHelperClasses = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_booked_trains, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.ticketBookedTrainID.setText(bookedTrainsHelperClasses.get(position).getTicketID());
        holder.ticketBookedTrainDate.setText(bookedTrainsHelperClasses.get(position).getBookedDate());
        holder.ticketBookedTrainFromTime.setText(bookedTrainsHelperClasses.get(position).getTicketTrainFromTime());
        holder.ticketBookedTrainToTime.setText(bookedTrainsHelperClasses.get(position).getTicketTrainToTime());
        holder.ticketBookedTrainRoute.setText(bookedTrainsHelperClasses.get(position).getTicketTrainFrom() + " - " + bookedTrainsHelperClasses.get(position).getTicketTrainTo());

        final String getTicketID = bookedTrainsHelperClasses.get(position).getTicketID();
        final String getTicketTrainName = bookedTrainsHelperClasses.get(position).getTicketTrainName();
        final String getTicketTrainNo = bookedTrainsHelperClasses.get(position).getTicketTrainNo();
        final String getTicketTrainFrom = bookedTrainsHelperClasses.get(position).getTicketTrainFrom();
        final String getTicketTrainFromTime = bookedTrainsHelperClasses.get(position).getTicketTrainFromTime();
        final String getTicketTrainTo = bookedTrainsHelperClasses.get(position).getTicketTrainTo();
        final String getTicketTrainToTime = bookedTrainsHelperClasses.get(position).getTicketTrainToTime();
        final String getTrainBookedDate = bookedTrainsHelperClasses.get(position).getBookedDate();
        final String getTicketClass = bookedTrainsHelperClasses.get(position).getTicketClass();
        final String getTicketPrice = bookedTrainsHelperClasses.get(position).getTicketPrice();
        final String getTicketQty = bookedTrainsHelperClasses.get(position).getTicketQty();
        final String getTicketTotalPrice = bookedTrainsHelperClasses.get(position).getTicketTotalPrice();

        //Book selected train
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent i = new Intent(context, BookedTicketView.class);
                    i.putExtra("ticketID", getTicketID);
                    i.putExtra("ticketTrainName", getTicketTrainName);
                    i.putExtra("ticketTrainNo", getTicketTrainNo);
                    i.putExtra("ticketTrainFrom", getTicketTrainFrom);
                    i.putExtra("ticketTrainFromTime", getTicketTrainFromTime);
                    i.putExtra("ticketTrainTo", getTicketTrainTo);
                    i.putExtra("ticketTrainToTime", getTicketTrainToTime);
                    i.putExtra("bookedDate", getTrainBookedDate);
                    i.putExtra("ticketClass", getTicketClass);
                    i.putExtra("ticketPrice", getTicketPrice);
                    i.putExtra("ticketQty", getTicketQty);
                    i.putExtra("ticketTotalPrice", getTicketTotalPrice);
                    context.startActivity(i);
                } catch (Exception e){
                    Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(context, ""+getTicketID+" "+getTicketTrainName+" "+getTicketTrainNo+" "+getTicketTrainFrom+" "+getTicketTrainFromTime+" "+getTicketTrainTo
//                        +" "+getTicketTrainToTime+" "+getTrainBookedDate+" "+getTicketClass+" "+getTicketPrice+" "+getTicketQty+" "+getTicketTotalPrice, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookedTrainsHelperClasses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ticketBookedTrainID, ticketBookedTrainDate, ticketBookedTrainFromTime, ticketBookedTrainToTime, ticketBookedTrainRoute;
        Button btnView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ticketBookedTrainID = (TextView) itemView.findViewById(R.id.ticketBookedTrainID);
            ticketBookedTrainDate = (TextView) itemView.findViewById(R.id.ticketBookedTrainDate);
            ticketBookedTrainFromTime = (TextView) itemView.findViewById(R.id.ticketBookedTrainFromTime);
            ticketBookedTrainToTime = (TextView) itemView.findViewById(R.id.ticketBookedTrainToTime);
            ticketBookedTrainRoute = (TextView) itemView.findViewById(R.id.bookedTicketRoute);
            btnView = (Button) itemView.findViewById(R.id.bookedTicketView);

        }
    }

}
