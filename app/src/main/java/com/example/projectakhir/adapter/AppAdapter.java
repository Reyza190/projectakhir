package com.example.projectakhir.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectakhir.Database.models.Berita;
import com.example.projectakhir.DetailActivity;
import com.example.projectakhir.Listener;
import com.example.projectakhir.R;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.Viewholder> {
    private Activity context;
    private ArrayList<Berita> beritaList = new ArrayList<>();
    private Listener listener;

    public AppAdapter(Activity context, ArrayList<Berita> beritaList, Listener listener) {
        this.context = context;
        this.beritaList = beritaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_list, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppAdapter.Viewholder holder, int position) {
        Berita berita = beritaList.get(position);
        holder.judul.setText(berita.getJudul());
        Glide.with(context).load(berita.getImage_berita()).into(holder.image);
        holder.desc.setText(berita.getDetail_article());
        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("BERITA", berita.getId());
            context.startActivity(intent);
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongCLick(beritaList.get(holder.getAdapterPosition()), holder.cardView);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return beritaList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView judul, desc;
        private CardView cardView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            judul = itemView.findViewById(R.id.txt_article_title);
            image = itemView.findViewById(R.id.img_article);
            desc = itemView.findViewById(R.id.txt_article_desc);
        }
    }
}
