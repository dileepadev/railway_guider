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

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.FeaturedViewHolder> {

    ArrayList<AboutHelperClass> featuredAbout;

    public AboutAdapter(ArrayList<AboutHelperClass> featuredAboutHelp) {
        this.featuredAbout = featuredAboutHelp;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_srilanka_card_design,parent,false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        AboutHelperClass aboutHelperClass = featuredAbout.get(position);

        holder.image.setImageResource(aboutHelperClass.getImage());
        holder.title.setText(aboutHelperClass.getTitle());
        holder.desc.setText(aboutHelperClass.getDescription());

    }

    @Override
    public int getItemCount() {
        return featuredAbout.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title,desc;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            image = itemView.findViewById(R.id.about_img);
            title = itemView.findViewById(R.id.about_title);
            desc = itemView.findViewById(R.id.about_desc);

        }
    }



}