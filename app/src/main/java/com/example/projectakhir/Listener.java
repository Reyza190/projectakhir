package com.example.projectakhir;

import androidx.cardview.widget.CardView;

import com.example.projectakhir.Database.models.Berita;

public interface Listener {
    void onLongCLick(Berita berita, CardView cardView);
}
