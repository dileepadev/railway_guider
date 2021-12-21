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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.dileepabandara.railwayguider.Databases.SchedulesHelperClass;
import dev.dileepabandara.railwayguider.R;
import dev.dileepabandara.railwayguider.User.BookATrain;

import java.util.ArrayList;

public class SchedulesAdapter extends RecyclerView.Adapter<SchedulesAdapter.MyViewHolder> {

    Context context;
    ArrayList<SchedulesHelperClass> schedulesHelperClasses;

    public SchedulesAdapter(Context c, ArrayList<SchedulesHelperClass> p){
        context = c;
        schedulesHelperClasses = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_schedules, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.trainName.setText(schedulesHelperClasses.get(position).getTrainName());
        holder.startTime.setText(schedulesHelperClasses.get(position).getStartTime());
        holder.endTime.setText(schedulesHelperClasses.get(position).getEndTime());

        final String getTrainName = schedulesHelperClasses.get(position).getTrainName();
        final String getTrainStart = schedulesHelperClasses.get(position).getStartTime();
        final String getTrainEnd = schedulesHelperClasses.get(position).getEndTime();
        final String getTrainNo = schedulesHelperClasses.get(position).getTrainNo();

        //Book selected train
        holder.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, BookATrain.class);
                i.putExtra("trainName", getTrainName);
                i.putExtra("trainStart", getTrainStart);
                i.putExtra("trainEnd", getTrainEnd);
                i.putExtra("trainNo", getTrainNo);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return schedulesHelperClasses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView trainName, startTime, endTime;
        Button btnBook;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            trainName = (TextView) itemView.findViewById(R.id.trainName);
            startTime = (TextView) itemView.findViewById(R.id.trainFromTime);
            endTime = (TextView) itemView.findViewById(R.id.trainToTime);
            btnBook = (Button) itemView.findViewById(R.id.bookTrain);

        }
    }

}
