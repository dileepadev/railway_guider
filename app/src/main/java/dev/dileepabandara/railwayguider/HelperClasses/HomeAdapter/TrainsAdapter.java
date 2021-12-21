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

package dev.dileepabandara.railwayguider.HelperClasses.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.dileepabandara.railwayguider.R;

import java.util.ArrayList;

public class TrainsAdapter extends RecyclerView.Adapter<TrainsAdapter.FeaturedViewHolder> {

    ArrayList<TrainsHelperClass> featuredTrains;

    public TrainsAdapter(ArrayList<TrainsHelperClass> featuredTrains) {
        this.featuredTrains = featuredTrains;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trains_in_srilanka_card_design,parent,false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        TrainsHelperClass trainsHelperClass = featuredTrains.get(position);

        holder.image.setImageResource(trainsHelperClass.getImage());
        holder.title.setText(trainsHelperClass.getTitle());
        holder.desc.setText(trainsHelperClass.getDescription());

    }

    @Override
    public int getItemCount() {
        return featuredTrains.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title,desc;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            image = itemView.findViewById(R.id.train_image);
            title = itemView.findViewById(R.id.train_title);
            desc = itemView.findViewById(R.id.train_desc);

        }
    }

}
